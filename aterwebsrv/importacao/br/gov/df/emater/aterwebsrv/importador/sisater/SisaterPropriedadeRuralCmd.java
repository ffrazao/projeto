package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.util.ArquivoConstantes;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.ColetaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRuralArquivo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralSituacao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service
public class SisaterPropriedadeRuralCmd extends _Comando {

	private class CheckDiagnosticoList {

		private PropriedadeRural propriedadeRural;

		private ResultSet rs;

		void avaliaPatrimonioDivida() throws SQLException, Exception {
			// Captar informações sociais
			Map<String, Object> formulario = new HashMap<String, Object>();
			formulario.put("usoDoSolo", createUsoDoSoloMap());
			formulario.put("pastagens", createPastagensMap());
			formulario.put("areasIrrigadas", createAreasIrrigadasMap());
			formulario.put("moradoresDaPropriedade", createMoradoresDaPropriedadeMap());
			formulario.put("maoDeObra", createMaoDeObraMap());
			formulario.put("benfeitoriaList", createBenfeitoriaList());
			formulario.put("observacoes", rs.getString("PPOBS2"));

			Coleta coleta = new Coleta(null, impUtil.getFormularioVersao("avaliacaoDaPropriedade"), ematerUsuario, agora, Confirmacao.S, null, propriedadeRural, null, new ObjectMapper().writeValueAsString(formulario));
			coleta.setChaveSisater(impUtil.chaveColetaFormulario(base, rs.getString("IDUND"), rs.getString("IDPRP"), false, "avaliacaoDaPropriedade"));
			Coleta coletaSalvo = coletaDao.findOneByChaveSisater(coleta.getChaveSisater());
			if (coletaSalvo != null) {
				coleta.setId(coletaSalvo.getId());
			}
			coletaDao.save(coleta);
		}

		private Map<String, Object> createAreasIrrigadasMap() throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("aspersaoConvencional", rs.getDouble("PPIASP"));
			result.put("autoPropelido", rs.getDouble("PPIAUT"));
			result.put("pivoCentral", rs.getDouble("PPIPIV"));
			result.put("gotejamento", rs.getDouble("PPIGOT"));
			result.put("microAspersao", rs.getDouble("PPIMIC"));
			result.put("superficie", rs.getDouble("PPISUP"));
			result.put("outros", rs.getDouble("PPIOUT"));
			return result;
		}

		private List<Map<String, Object>> createBenfeitoriaList() throws SQLException {
			psBenfeitoria.setString(1, rs.getString("IDPRP"));
			ResultSet rsBenfeitoria = psBenfeitoria.executeQuery();
			List<Map<String, Object>> result = null;
			while (rsBenfeitoria.next()) {
				Map<String, Object> benfeitoriaMap = createBenfeitoriaMap(rsBenfeitoria);
				if (result == null) {
					result = new ArrayList<Map<String, Object>>();
				}
				result.add(benfeitoriaMap);
			}
			return result;
		}

		private Map<String, Object> createBenfeitoriaMap(ResultSet rsBenfeitoria) throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("discriminacaoDasBenfeitorias", rsBenfeitoria.getString("ITEM"));
			result.put("caracteristica", rsBenfeitoria.getString("CARACTERISTICA"));
			result.put("unidade", rsBenfeitoria.getString("UNID"));
			result.put("quantidade", rsBenfeitoria.getDouble("QUANT"));
			result.put("valorUnitario", rsBenfeitoria.getDouble("VALORUND"));
			return result;
		}

		private Map<String, Object> createMaoDeObraMap() throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("familiar", rs.getString("PPPMOF"));
			result.put("contratada", rs.getString("PPPEMP"));
			result.put("temporaria", rs.getString("PPMOT"));
			return result;
		}

		private Map<String, Object> createMoradoresDaPropriedadeMap() throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("familias", rs.getString("PPPFAM"));
			result.put("pessoas", rs.getString("PPPPES"));
			return result;
		}

		private Map<String, Object> createPastagensMap() throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("areaDeCanavial", rs.getDouble("PPAREACANA"));
			result.put("areaDeCapineira", rs.getDouble("PPAREACAPI"));
			result.put("areaDeSilagem", rs.getDouble("PPAREASILA"));
			result.put("areaDeFeno", rs.getDouble("PPAREAFENO"));
			result.put("areaDePastagemNatural", rs.getDouble("PPAREAPASN"));
			result.put("areaDePastagemArtificial", rs.getDouble("PPAREAPASA"));
			result.put("areaDePastagemRotacionada", rs.getDouble("PPAREAPASR"));
			result.put("areaIlpIlpf", rs.getDouble("PPAREAILPF"));
			return result;
		}

		private Map<String, Object> createUsoDoSoloMap() throws SQLException {
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("culturasPerenesArea", rs.getDouble("PpAPer"));
			result.put("culturasPerenesValorUnitario", rs.getDouble("PPUPER"));
			result.put("culturasTemporariasArea", rs.getDouble("PpATem"));
			result.put("culturasTemporariasValorUnitario", rs.getDouble("PPUTEM"));
			result.put("pastagensArea", rs.getDouble("PpAPas"));
			result.put("pastagensValorUnitario", rs.getDouble("PPUPAS"));
			result.put("benfeitoriasArea", rs.getDouble("PpABen"));
			result.put("benfeitoriasValorUnitario", rs.getDouble("PPUBEN"));
			result.put("reservaLegalArea", rs.getDouble("PPALEG"));
			result.put("reservaLegalValorUnitario", rs.getDouble("PPULEG"));
			result.put("preservacaoPermanenteArea", rs.getDouble("PPAPRE"));
			result.put("preservacaoPermanenteValorUnitario", rs.getDouble("PPUPRE"));
			result.put("outrasArea", rs.getDouble("PPAOUT"));
			result.put("outrasValorUnitario", rs.getDouble("PPUOUT"));
			return result;
		}

		void set(ResultSet rs, PropriedadeRural propriedadeRural) {
			this.rs = rs;
			this.propriedadeRural = propriedadeRural;
		}
	}

	private static final String SISATER_CAMPO = "PPNOME";

	private static final String SISATER_TABELA = "PROP00";

	private static final String SQL = String.format("SELECT T.*, C.COMUNIDADE, C.BACIA, C.REGIAO FROM %s T LEFT JOIN COM00 C ON C.IDUND = T.IDUND AND C.IDCOM = T.IDCOM ORDER BY T.%s", SISATER_TABELA, SISATER_CAMPO);

	private static final String SQL_BENFEITORIA = "SELECT * FROM PROP01 WHERE IDPRP = ? ORDER BY ITEM";

	private static final String SQL_FOTO = "SELECT T.* FROM PROP02 T WHERE T.IDPRP = ?";

	private Calendar agora;

	@Autowired
	private ArquivoDao arquivoDao;

	private DbSater base;

	private CheckDiagnosticoList checkDiagnosticoList = new CheckDiagnosticoList();

	@Autowired
	private ColetaDao coletaDao;

	private Connection con;

	private Usuario ematerUsuario;

	private FacadeBo facadeBo;

	private ImpUtil impUtil;

	@Autowired
	private PropriedadeRuralArquivoDao propriedadeRuralArquivoDao;
	@Autowired
	private PropriedadeRuralDao propriedadeRuralDao;

	private PreparedStatement psBenfeitoria;

	private PreparedStatement psFoto;

	private List<PropriedadeRuralArquivo> captarArquivoList(String idPrp, PropriedadeRural propriedadeRural) throws SQLException, IOException {
		List<PropriedadeRuralArquivo> result = null;

		psFoto.setString(1, idPrp);
		try (ResultSet rs = psFoto.executeQuery()) {
			while (rs.next()) {
				Arquivo arquivo = new Arquivo();
				arquivo.setConteudo(ConexaoFirebird.lerCampoBlob(rs, "FOTO"));

				if (arquivo.getConteudo() != null) {

					arquivo.setMd5(Criptografia.MD5_FILE(arquivo.getConteudo()));
					Arquivo arqSalvo = arquivoDao.findByMd5(arquivo.getMd5());
					if (arqSalvo != null) {
						arquivo.setId(arqSalvo.getId());
					}
					arquivo.setExtensao(".jpg");
					arquivo.setDataUpload(agora);
					arquivo.setMimeTipo("image/jpeg");
					arquivo.setNomeOriginal(String.format("arq-%s.jpg", idPrp));
					arquivo.setTipo(ArquivoTipo.A);
					arquivo.setTamanho(arquivo.getConteudo().length);
					arquivo.setLocalDiretorioWeb(ArquivoConstantes.DIRETORIO_UPLOAD.concat(File.separator).concat(arquivo.getMd5()));
					arquivo = arquivoDao.save(arquivo);

					if (result == null) {
						result = new ArrayList<PropriedadeRuralArquivo>();
					}

					PropriedadeRuralArquivo propriedadeRuralArquivo = new PropriedadeRuralArquivo(propriedadeRural, arquivo, Confirmacao.N);
					propriedadeRuralArquivo.setChaveSisater(impUtil.chavePropriedadeRuralArquivo(base, rs.getString("IDPRP"), rs.getString("DESCRICAO")));
					propriedadeRuralArquivo.setDescricao(rs.getString("DESCRICAO"));

					PropriedadeRuralArquivo salvo = propriedadeRuralArquivoDao.findOneByChaveSisater(propriedadeRuralArquivo.getChaveSisater());
					if (salvo != null) {
						propriedadeRuralArquivo.setId(salvo.getId());
					}
					result.add(propriedadeRuralArquivo);
				}
			}
		}
		return result;
	}

	private void captarDiagnosticoList(ResultSet rs, PropriedadeRural propriedadeRural) throws Exception {
		checkDiagnosticoList.set(rs, propriedadeRural);
		checkDiagnosticoList.avaliaPatrimonioDivida();
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		facadeBo = (FacadeBo) contexto.get("facadeBo");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");
		ematerUsuario = (Usuario) contexto.get("ematerUsuario");

		agora = (Calendar) contexto.get("agora");

		impUtil.criarMarcaTabela(con, SISATER_TABELA);

		psFoto = con.prepareStatement(SQL_FOTO);

		psBenfeitoria = con.prepareStatement(SQL_BENFEITORIA);

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL);) {
			int cont = 0;
			while (rs.next()) {
				cont++;
				PropriedadeRural propriedadeRural = novoPropriedadeRural(rs);

				// recuperar os IDs
				PropriedadeRural salvo = propriedadeRuralDao.findOneByChaveSisater(propriedadeRural.getChaveSisater());
				if (salvo != null) {
					propriedadeRural.setId(salvo.getId());
				}

				// Este campo no sisater está vinculado ao publico alvo, no caso
				// ele será alimentado ao importar os beneficiários viculados a
				// esta propriedade
				// propriedadeRural.setFormaUtilizacaoEspacoRuralList(rs.getString(""));

				propriedadeRural.setObservacoes(rs.getString("PPOBS1"));

				propriedadeRural.setSituacao(PropriedadeRuralSituacao.A);
				propriedadeRural.setSituacaoData(agora);
				propriedadeRural.setArquivoList(captarArquivoList(rs.getString("IDPRP"), propriedadeRural));

				// salvar no MySQL e no Firebird
				propriedadeRural.setId((Integer) facadeBo.propriedadeRuralSalvar(contexto.getUsuario(), propriedadeRural).getResposta());

				impUtil.chaveAterWebAtualizar(con, propriedadeRural.getId(), SISATER_TABELA, "IDUND = ? AND IDPRP = ?", rs.getString("IDUND"), rs.getString("IDPRP"));

				captarDiagnosticoList(rs, propriedadeRural);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("[%s] importado %d propriedades rurais", base.name(), cont));
			}
		}
		return false;
	}

	private PropriedadeRural novoPropriedadeRural(ResultSet rs) throws SQLException, BoException {
		PropriedadeRural result = new PropriedadeRural();
		result.setChaveSisater(impUtil.chavePropriedadeRural(base, rs.getString("IDUND"), rs.getString("IDPRP")));
		result.setAreaTotal(impUtil.captaBigDecimal(rs.getDouble("PPATOT")));
		result.setBaciaHidrografica(impUtil.getBaciaHidrografica(rs.getString("BACIA")));
		result.setCartorioDataRegistro(impUtil.captaData(rs.getDate("PPDTREG")));
		result.setCartorioInformacao(rs.getString("PPDOC"));
		result.setComunidade(impUtil.getComunidade(rs.getString("COMUNIDADE")));
		result.setEndereco(impUtil.deParaEndereco(base, Confirmacao.S, rs.getString("PPNOME"), rs.getString("PPEND"), rs.getString("PPCEP"), rs.getString("REGIAO"), rs.getString("PPROT")));
		result.setNome(rs.getString("PPNOME"));
		result.setNumeroRegistro(rs.getString("PPREG"));
		result.setObservacoes(rs.getString("PPOBS1"));
		result.setOutorga(impUtil.deParaConfirmacaoObj(rs.getString("PPOUTORGA")));
		result.setOutorgaNumero(rs.getString("PPOUTORGANR"));
		result.setOutorgaValidade(impUtil.captaData(rs.getDate("PPOUTORGADT")));
		// result.setPrincipaisAtividadesProdutivas(rs.getString(""));
		result.setRoteiroAcesso(rs.getString("PPROT"));
		result.setSistemaProducao(impUtil.getSistemaProducao(rs.getString("SISTEMA")));
		result.setSituacao(PropriedadeRuralSituacao.A);
		result.setSituacaoData(agora);
		result.setSituacaoFundiaria(impUtil.deParaSituacaoFundiaria(rs.getString("PPFUND")));

		return result;
	}

}