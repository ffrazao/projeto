package br.gov.df.emater.aterwebsrv.importador.apoio;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.dao.ater.BaciaHidrograficaDao;
import br.gov.df.emater.aterwebsrv.dao.ater.ComunidadeDao;
import br.gov.df.emater.aterwebsrv.dao.ater.SistemaProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.formulario.FormularioVersaoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoViDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.CidadeDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ProfissaoDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.modelo.ater.BaciaHidrografica;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.OrganizacaoTipo;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoSetor;
import br.gov.df.emater.aterwebsrv.modelo.ater.Setor;
import br.gov.df.emater.aterwebsrv.modelo.ater.SistemaProducao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ConfirmacaoDap;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.EstadoCivil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PropriedadeRuralVinculoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoCategoria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PublicoAlvoSegmento;
import br.gov.df.emater.aterwebsrv.modelo.dominio.RegimeCasamento;
import br.gov.df.emater.aterwebsrv.modelo.dominio.SituacaoFundiaria;
import br.gov.df.emater.aterwebsrv.modelo.formulario.FormularioVersao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.EmpregoVi;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Cidade;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Profissao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service
public class ImpUtil {

	@Autowired
	private BaciaHidrograficaDao baciaHidrograficaDao;

	private List<BaciaHidrografica> baciaHidrograficaList = new ArrayList<BaciaHidrografica>();

	@Autowired
	private CidadeDao cidadeDao;

	@Autowired
	private ComunidadeDao comunidadeDao;

	private List<Comunidade> comunidadeList = new ArrayList<Comunidade>();

	@Autowired
	private EmpregoViDao empregoViDao;

	@Autowired
	private FormularioVersaoDao formularioVersaoDao;

	private List<FormularioVersao> formularioVersaoList;

	protected final Log logger = LogFactory.getLog(getClass());

	private Map<String, OrganizacaoTipo> organizacaoTipoMap = null;

	@Autowired
	private ProfissaoDao profissaoDao;

	private Map<String, Profissao> profissaoMap = new HashMap<String, Profissao>();

	private Map<String, Setor> setorMap = null;

	@Autowired
	private SistemaProducaoDao sistemaProducaoDao;

	private List<SistemaProducao> sistemaProducaoList = new ArrayList<SistemaProducao>();

	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	private List<UnidadeOrganizacional> unidadeOrganizacionalList = new ArrayList<UnidadeOrganizacional>();

	@Autowired
	private UsuarioDao usuarioDao;

	private String ajustaNomeCidade(String cidade, String estado) {
		switch (estado.trim().toLowerCase()) {
		case "df":
			switch (UtilitarioString.semAcento(cidade.trim().toLowerCase())) {
			case "scia":
				return "Guará";
			case "alexandre gusmao":
				return "Brazlândia";
			case "aguas claras":
				return "Taguatinga";
			case "riacho fundo i":
			case "riacho fundo ii":
				return "Riacho Fundo";
			case "park way":
				return "Núcleo Bandeirante";
			}
		case "go":
			switch (UtilitarioString.semAcento(cidade.trim().toLowerCase())) {
			case "sao marcos":
				return "Cristalina";
			case "cocalzinho":
				return "Cocalzinho de Goiás";
			case "planaltina de goias":
				return "Planaltina";
			}
			break;
		}
		return cidade;
	}

	public BigDecimal captaBigDecimal(Double valor) {
		if (valor == null) {
			return null;
		}
		return new BigDecimal(valor);
	}

	public Calendar captaData(Date data) {
		if (data == null) {
			return null;
		}
		Calendar result = new GregorianCalendar();
		result.setTime(data);
		return result;
	}

	public String captaUtm(String coord) {
		if (StringUtils.containsAny(coord, '°', '\'', '\"')) {
			return coord;
		}
		if (coord == null || coord.trim().length() == 0) {
			return null;
		}
		StringBuilder temp = new StringBuilder();
		int totPonto = 0;
		for (int i = 0; i < coord.length(); i++) {
			if ((i == 0 && coord.charAt(i) == '-') || Character.isDigit(coord.charAt(i)) || coord.charAt(i) == ','
					|| coord.charAt(i) == '.') {
				temp.append(coord.charAt(i));
			}
			if (coord.charAt(i) == '.') {
				totPonto++;
			}
		}
		if (totPonto > 1) {
			coord = UtilitarioString.substituirTudo(coord, ".", "");
		}

		if (temp.toString().trim().length() == 0) {
			return coord;
		}
		coord = temp.toString();
		return coord;
	}

	public String[] captaUtmNE(String c1, String c2) {
		String[] result = new String[4];

		c1 = captaUtm(c1);
		c2 = captaUtm(c2);
		if (c1 != null && c2 != null) {
			try {
				Double c1n = Double.parseDouble(c1.replaceAll(",", "."));
				Double c2n = Double.parseDouble(c2.replaceAll(",", "."));
				if (c1n > c2n) {
					String temp = c1;
					c1 = c2;
					c2 = temp;
				}
				if ((c1.startsWith("8") || c1.startsWith("2")) && c1.length() < 7) {
					c1 = UtilitarioString.zeroDireita(c1, 7);
				}
				if ((c2.startsWith("8") || c2.startsWith("2")) && c2.length() < 7) {
					c2 = UtilitarioString.zeroDireita(c2, 7);
				}
				result[0] = c1;
				result[1] = c2;
			} catch (NullPointerException | NumberFormatException e) {
				result[2] = c1.trim();
				result[3] = c2.trim();
			}
		}
		return result;
	}

	public void chaveAterWebAtualizar(Connection con, Integer id, Calendar agora, String tabelaSisater,
			String clausuaWhere, Object... parametroList) throws SQLException {
		PreparedStatement ps = con.prepareStatement(String
				.format("UPDATE %s SET CHAVE_ATER_WEB = ?, DATA_ATER_WEB = ? WHERE %s", tabelaSisater, clausuaWhere));
		ps.setInt(1, id);
		ps.setDate(2, new Date(agora.getTime().getTime()));
		int cont = 3;
		if (parametroList != null) {
			for (Object parametro : parametroList) {
				ps.setObject(cont++, parametro);
			}
		}
		ps.executeUpdate();
	}

	public String chaveAtividadeAntes2014(DbSater base, String idUnd, int idAtr, String tabela) {
		return String.format("%s=[IDUND=%s,IDATR=%d][TABELA=%s]", base.name(), idUnd, idAtr, tabela);
	}

	public String chaveAtividadeDepois2014(DbSater base, String idUnd, int idAtr, Date aterDt, String idEmp,
			String idMet, String idTema, String idAcao, String tabela) {
		return String.format("%s=[IDUND=%s,IDATR=%d,ATERDT=%s,IDEMP=%s,IDMET=%s,IDTEMA=%s,IDACAO=%s][TABELA=%s]",
				base.name(), idUnd, idAtr, UtilitarioData.getInstance().formataData(aterDt), idEmp, idMet, idTema,
				idAcao, tabela);
	}

	public String chaveBaciaHidrografica(DbSater base, String baciaHidrograficaNome) {
		return String.format("%s=[BACIA=%s]", base.name(), baciaHidrograficaNome);
	}

	public String chaveBeneficiario(DbSater base, String idund, String idbem) {
		return String.format("%s=[IDUND=%s,IDBEN=%s]", base.name(), idund, idbem);
	}

	public String chaveColetaFormulario(DbSater base, String idund, String idBemIdPrp, boolean beneficiario,
			String nomeCampo) {
		if (beneficiario) {
			return String.format("%s[CAMPO=%s]", chaveBeneficiario(base, idund, idBemIdPrp), nomeCampo);
		} else {
			return String.format("%s[CAMPO=%s]", chavePropriedadeRural(base, idund, idBemIdPrp), nomeCampo);
		}
	}

	public String chaveComunidade(DbSater base, String idund, String idcom) {
		return String.format("%s=[IDUND=%s,IDCOM=%s]", base.name(), idund, idcom);
	}

	public String chaveEmpregado(DbSater base, String idEmp) {
		return String.format("%s=[IDEMP=%s]", base.name(), idEmp);
	}

	public String chavePessoaEmail(DbSater base, String idund, String idbem) {
		return chaveBeneficiario(base, idund, idbem);
	}

	public String chavePessoaEndereco(DbSater base, String idund, String idbem, String[] nomeCampo) {
		return String.format("%s[CAMPO=%s][CAMPO=%s]", chaveBeneficiario(base, idund, idbem), nomeCampo[0],
				nomeCampo[1]);
	}

	public String chavePessoaGrupoSocial(DbSater base, String idund, String idbem, String nomeCampo) {
		return String.format("%s[CAMPO=%s]", chaveBeneficiario(base, idund, idbem), nomeCampo);
	}

	public String chavePessoaRelacionamento(DbSater base, String idund, String idbem, String nomeCampo) {
		return String.format("%s[CAMPO=%s]", chaveBeneficiario(base, idund, idbem), nomeCampo);
	}

	public String chavePessoaTelefone(DbSater base, String idund, String idbem, String nomeCampo) {
		return String.format("%s[CAMPO=%s]", chaveBeneficiario(base, idund, idbem), nomeCampo);
	}

	public String chaveProducaoAgricola(DbSater base, String idUnd, int idIpa, String idBen, String idPrp,
			String nomeCampo) {
		return String.format("%s=[IDUND=%s,IDIPA=%d,IDBEN=%s,IDPRP=%s][TABELA=%s]", base.name(), idUnd, idIpa, idBen,
				idPrp, nomeCampo);
	}

	public String chaveProducaoAgricolaGeral(DbSater base, String idUnd, int idIpa, String safra, String nomeCampo) {
		return String.format("%s=[IDUND=%s,IDIPA=%d,SAFRA=%s][TABELA=%s]", base.name(), idUnd, idIpa, safra, nomeCampo);
	}

	public String chavePropriedadeRural(DbSater base, String idund, String idprp) {
		return String.format("%s=[IDUND=%s,IDPRP=%s]", base.name(), idund, idprp);
	}

	public String chavePropriedadeRuralArquivo(DbSater base, String idPrp, String descricao) {
		return String.format("%s=[IDPRP=%s,DESCRICAO=%s]", base.name(), idPrp, descricao);
	}

	public String chavePublicoAlvoPropriedadeRural(DbSater base, String idprp, String idben) {
		return String.format("%s=[IDPRP=%s,IDBEN=%s]", base.name(), idprp, idben);
	}

	public void criarMarcaTabelaSisater(Connection con, String tabela) throws SQLException {
		try (Statement st = con.createStatement();) {
			try {
				st.execute(String.format("ALTER TABLE %s DROP DATA_ATER_WEB", tabela));
			} catch (Exception e) {
			}
			try {
				st.execute(String.format("ALTER TABLE %s DROP CHAVE_ATER_WEB", tabela));
			} catch (Exception e) {
			}
			try {
				st.execute(String.format("ALTER TABLE %s ADD CHAVE_ATER_WEB INT", tabela));
			} catch (Exception e) {
			}
			try {
				st.execute(String.format("ALTER TABLE %s ADD DATA_ATER_WEB TIMESTAMP", tabela));
			} catch (Exception e) {
			}
			try {
				con.commit();
			} catch (Exception e) {
			}
		}
	}

	public String deNomeBaciaHidrograficaSisaterParaAterWeb(String registro) throws BoException {
		if (registro == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "pedreira - contagem":
			return "Pedreira Contagem";
		case "cachoerinha":
		case "cachoeirinha - papuda":
			return "Cachoeirinha";
		default:
			return registro;
		}
	}

	public Cidade deNomeCidadeComunidadeSisaterParaAterWeb(String registro, DbSater base,
			Municipio[] municipioAtendimentoList) throws BoException {
		if (registro == null) {
			return null;
		}

		registro = ajustaNomeCidade(registro, base.getSiglaEstado());

		List<Cidade> cidadeList = null;
		for (Municipio municipio : municipioAtendimentoList) {
			if (municipio.getEstado().getSigla().equals(base.getSiglaEstado())) {
				cidadeList = cidadeDao.findByNomeLikeAndMunicipio(registro, municipio);
				if (cidadeList != null && cidadeList.size() > 0) {
					break;
				}
			}
		}

		if (cidadeList == null || cidadeList.size() == 0) {
			// throw new BoException("Cidade não encontrada - %s", registro);
			logger.error(String.format("Cidade não encontrada - %s", registro));
			return null;
		} else if (cidadeList.size() > 1) {
			// throw new BoException("Mais de uma cidade encontrada - %s",
			// registro);
			logger.error(String.format("Mais de uma cidade encontrada - %s", registro));
			return null;
		}
		return cidadeList.get(0);
	}

	public String deNomeComunidadeSisaterParaAterWeb(String registro) throws BoException {
		if (registro == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		default:
			return registro;
		}
	}

	public String deParaConfirmacao(String registro) {
		if (registro == null) {
			return null;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "sim":
			return "S";
		case "nao":
			return "N";
		default:
			return null;
		}
	}

	public Confirmacao deParaConfirmacaoObj(String registro) {
		if (registro == null) {
			return null;
		}
		registro = deParaConfirmacao(registro);
		if (registro == null) {
			return null;
		}
		return Confirmacao.valueOf(Confirmacao.class, registro);
	}

	public ConfirmacaoDap deParaDapSituacao(String registro) {
		if (registro == null) {
			return null;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "ndp":
			return ConfirmacaoDap.D;
		case "nao":
			return ConfirmacaoDap.N;
		case "sim":
			return ConfirmacaoDap.S;
		default:
			return null;
		}
	}

	public String deParaDividaFinalidade(String registro) {
		if (registro == null) {
			return null;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "custeio":
			return "C";
		case "investimento":
			return "I";
		case "outras":
			return "O";
		case "securitizacao":
			return "S";
		default:
			return null;
		}
	}

	public Endereco deParaEndereco(DbSater base, Confirmacao propriedadeRuralConfirmacao,
			String nomePropriedadeRuralOuEstabelecimento, String logradouro, String cep, String regiao,
			String roteiroAcessoOuEnderecoInternacional) {
		Endereco result = new Endereco();
		result.setPropriedadeRuralConfirmacao(propriedadeRuralConfirmacao);
		result.setNomePropriedadeRuralOuEstabelecimento(nomePropriedadeRuralOuEstabelecimento);
		result.setLogradouro(logradouro);
		result.setCep(cep);
		result.setBairro(regiao);
		Cidade cidade = getEnderecoLocalizacao(regiao, base);
		if (cidade != null) {
			result.setCidade(cidade);
			result.setMunicipio(cidade.getMunicipio());
			result.setEstado(cidade.getMunicipio().getEstado());
			result.setPais(cidade.getMunicipio().getEstado().getPais());
		}
		result.setRoteiroAcessoOuEnderecoInternacional(roteiroAcessoOuEnderecoInternacional);
		result.setEnderecoSisater(String.format("%s\n%s", logradouro, cep == null ? "" : cep));
		result.setEnderecoAtualizado(Confirmacao.N);
		return result;
	}

	public Escolaridade deParaEscolaridade(String registro) {
		if (registro == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "analfabeto":
			return Escolaridade.AI;
		case "ensino fundamental":
		case "fundamental completo":
		case "primario completo":
			return Escolaridade.FC;
		case "primeiro grau incompleto":
		case "primeiro incompleto":
		case "fundamental incompleto":
		case "primario incompleto":
			return Escolaridade.FI;
		case "medio completo":
			return Escolaridade.MC;
		case "medio incompleto":
			return Escolaridade.MI;
		case "pos garduado":
			return Escolaridade.ES;
		case "superior completo":
			return Escolaridade.SC;
		case "superior incompleto":
			return Escolaridade.SI;
		default:
			return null;
		}
	}

	public EstadoCivil deParaEstadoCivil(String registro) {
		if (registro == null) {
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "amasiado":
		case "uniao esta":
		case "juntado":
			return EstadoCivil.U;
		case "casada":
		case "casado":
			return EstadoCivil.C;
		case "desquitado":
		case "separado":
			return EstadoCivil.P;
		case "divorciado":
			return EstadoCivil.D;
		case "solteira":
		case "solteiro":
			return EstadoCivil.S;
		case "falecida":
		case "viuva":
		case "viuvo":
			return EstadoCivil.V;
		default:
			return null;
		}
	}

	public PessoaGenero deParaGenero(String registro) {
		if (registro == null) {
			return PessoaGenero.M;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "feminino":
			return PessoaGenero.F;
		case "masculino":
			return PessoaGenero.M;
		default:
			return PessoaGenero.M;
		}
	}

	public OrganizacaoTipo deParaOrganizacaoTipo(String registro) throws BoException {
		if (registro == null || registro.trim().length() == 0) {
			return null;
		}
		if (organizacaoTipoMap == null) {
			organizacaoTipoMap = new HashMap<String, OrganizacaoTipo>();
			organizacaoTipoMap.put("associacao", new OrganizacaoTipo(1, "Associação"));
			organizacaoTipoMap.put("condominio", new OrganizacaoTipo(2, "Condomínio"));
			organizacaoTipoMap.put("conselho", new OrganizacaoTipo(3, "Conselho"));
			organizacaoTipoMap.put("cooperativa", new OrganizacaoTipo(4, "Cooperativa"));
			organizacaoTipoMap.put("empresa", new OrganizacaoTipo(5, "Empresa"));
			organizacaoTipoMap.put("escola rural", new OrganizacaoTipo(6, "Escola Rural"));
			organizacaoTipoMap.put("escola urbana", new OrganizacaoTipo(7, "Escola Urbana"));
			organizacaoTipoMap.put("grupos", new OrganizacaoTipo(8, "Grupos"));
			organizacaoTipoMap.put("individual", new OrganizacaoTipo(9, "Individual"));
			organizacaoTipoMap.put("institucional", new OrganizacaoTipo(10, "Institucional"));
			organizacaoTipoMap.put("ong´s", new OrganizacaoTipo(11, "ONG\"s"));
			organizacaoTipoMap.put("outro", new OrganizacaoTipo(12, "Outro"));
		}
		OrganizacaoTipo result = organizacaoTipoMap.get(UtilitarioString.semAcento(registro).toLowerCase());
		if (result == null) {
			// throw new BoException("Setor não identificado");
			logger.error(String.format(("Setor não identificado")));
			return null;
		}
		return result;

	}

	public Profissao deParaProfissao(String registro) {
		if (registro == null || registro.trim().length() == 0 || "null".equals(registro.trim().toLowerCase())) {
			return null;
		}
		if (profissaoMap.containsKey(registro)) {
			return profissaoMap.get(registro);
		}
		Profissao result = profissaoDao.findOneByNome(registro);
		if (result == null) {
			result = new Profissao();
			result.setNome(registro);
			result = profissaoDao.save(result);
		}
		profissaoMap.put(result.getNome(), result);
		return result;
	}

	public PropriedadeRuralVinculoTipo deParaPropriedadeRuralVinculoTipo(String registro) throws BoException {
		if (registro == null) {
			// throw new BoException("Vinculo com a propriedade não informado");
			logger.error(String.format("Vinculo com a propriedade não informado"));
			return null;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "arrendamento":
			return PropriedadeRuralVinculoTipo.AR;
		case "parceria":
			return PropriedadeRuralVinculoTipo.PA;
		case "proprio":
		case "ex-proprietario":
			return PropriedadeRuralVinculoTipo.PR;
		default:
			// throw new BoException("Categoria não identificada [%s]",
			// registro);
			logger.error(String.format("Categoria não identificada [%s]", registro));
			return null;
		}
	}

	public PublicoAlvoCategoria deParaPublicoAlvoCategoria(String registro) throws BoException {
		if (registro == null) {
			// throw new BoException("Categoria não informada");
			logger.error(String.format("Categoria não informada"));
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "empreendedor":
			return PublicoAlvoCategoria.E;
		case "habitante":
			return PublicoAlvoCategoria.H;
		case "trabalhador":
			return PublicoAlvoCategoria.T;
		default:
			// throw new BoException("Categoria não identificada [%s]",
			// registro);
			logger.error(String.format("Categoria não identificada [%s]", registro));
			return null;
		}
	}

	public PublicoAlvoSegmento deParaPublicoAlvoSegmento(String registro) throws BoException {
		if (registro == null) {
			// throw new BoException("Segmento não informado");
			logger.error(String.format("Segmento não informado"));
			return null;
		}

		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "familiar":
			return PublicoAlvoSegmento.F;
		case "patronal":
			return PublicoAlvoSegmento.P;
		default:
			// throw new BoException("Segmento não identificado [%s]",
			// registro);
			logger.error(String.format("Segmento não identificado [%s]", registro));
			return null;
		}
	}

	public List<PublicoAlvoSetor> deParaPublicoAlvoSetorList(String setor1, String setor2) throws BoException {
		List<PublicoAlvoSetor> publicoAlvoSetorList = new ArrayList<PublicoAlvoSetor>();
		if (setor1 != null) {
			Setor setor = deParaSetor(setor1);
			if (setor != null) {
				publicoAlvoSetorList.add(new PublicoAlvoSetor(null, setor));
			}
		}
		if (setor2 != null && !setor2.equals(setor1)) {
			Setor setor = deParaSetor(setor2);
			if (setor != null) {
				publicoAlvoSetorList.add(new PublicoAlvoSetor(null, setor));
			}
		}
		return publicoAlvoSetorList;
	}

	public RegimeCasamento deParaRegimeCasamento(String registro) {
		if (registro == null) {
			return null;
		}
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "comunhao parcial":
			return RegimeCasamento.P;
		case "comunhao total":
			return RegimeCasamento.T;
		case "nao se aplica":
			return RegimeCasamento.N;
		case "separacao de bens":
			return RegimeCasamento.S;
		case "uniao estavel":
			return RegimeCasamento.U;
		default:
			return null;
		}
	}

	public Setor deParaSetor(String registro) throws BoException {
		if (registro == null || registro.trim().length() == 0) {
			return null;
		}
		if (setorMap == null) {
			setorMap = new HashMap<String, Setor>();
			setorMap.put("agroindustria", new Setor(1, "Agroindústria"));
			setorMap.put("agropecuaria", new Setor(2, "Agropecuária"));
			setorMap.put("artesanato", new Setor(3, "Artesanato"));
			setorMap.put("comercio", new Setor(4, "Comércio"));
			setorMap.put("proc. artesanal", new Setor(5, "Processamento Artesanal"));
			setorMap.put("servico", new Setor(6, "Serviço"));
			setorMap.put("turismo", new Setor(7, "Turismo Rural"));
			setorMap.put("outros", new Setor(8, "Outro"));
		}
		Setor result = setorMap.get(UtilitarioString.semAcento(registro).toLowerCase());
		if (result == null) {
			// throw new BoException("Setor não identificado");
			logger.error(String.format("Setor não identificado"));
			return null;
		}
		return result;
	}

	public SituacaoFundiaria deParaSituacaoFundiaria(String registro) {
		if (registro == null || registro.trim().length() == 0) {
			return null;
		}
		// PPFUND
		switch (UtilitarioString.semAcento(registro.trim().toLowerCase())) {
		case "concessao de uso":
			return SituacaoFundiaria.C;
		case "escritura definitiva":
			return SituacaoFundiaria.E;
		case "posse":
			return SituacaoFundiaria.P;
		default:
			return null;
		}
	}

	public UnidadeOrganizacional deParaUnidadeOrganizacional(PessoaJuridica empresa, String registro)
			throws BoException {
		for (UnidadeOrganizacional uo : unidadeOrganizacionalList) {
			if (uo.getSigla().equalsIgnoreCase(registro)) {
				return uo;
			}
		}
		UnidadeOrganizacional uo = unidadeOrganizacionalDao.findOneByPessoaJuridicaAndSigla(empresa, registro);
		if (uo != null) {
			unidadeOrganizacionalList.add(uo);
		} else {
			// throw new BoException("Unidade Organizacional não encontrada");
			logger.error(String.format("Unidade Organizacional não encontrada"));
			return null;
		}
		return uo;
	}

	public Usuario deParaUsuario(String registro) {
		registro = UtilitarioString.soNumero(registro, 'X', 'x');
		if (registro == null || registro.trim().length() == 0) {
			return null;
		}
		registro = UtilitarioString.zeroEsquerda(registro.toUpperCase(), 8);
		List<EmpregoVi> empregoList = empregoViDao.findByMatricula(registro);
		if (empregoList == null || empregoList.size() != 1) {
			return null;
		}
		List<Usuario> result = usuarioDao.findByPessoa(new PessoaFisica(empregoList.get(0).getEmpregadoId()));
		if (result == null || result.size() != 1) {
			return null;
		}
		return result.get(0);
	}

	public BaciaHidrografica getBaciaHidrografica(String nome) throws BoException {
		if (nome == null || nome.trim().length() == 0) {
			// throw new BoException("Bacia não informada");
			logger.error("Bacia não informada");
			return null;
		}
		for (BaciaHidrografica baciaHidrografica : baciaHidrograficaList) {
			if (baciaHidrografica.getNome().equalsIgnoreCase(nome)) {
				return baciaHidrografica;
			}
		}
		List<BaciaHidrografica> baciaHidrografica = baciaHidrograficaDao.findByNomeOrderByNomeAsc(nome);
		if (baciaHidrografica == null || baciaHidrografica.size() != 1) {
			// throw new BoException("Bacia inexistente [%s]", nome);
			logger.error(String.format("Bacia inexistente [%s]", nome));
			return null;
		}
		baciaHidrograficaList.add(baciaHidrografica.get(0));

		return baciaHidrografica.get(0);
	}

	public Comunidade getComunidade(String nome) throws BoException {
		if (nome == null) {
			throw new BoException("Comunidade não informada");
			// logger.error("Comunidade não informada");
			// return null;
		}
		for (Comunidade comunidade : comunidadeList) {
			if (comunidade.getNome().equalsIgnoreCase(nome)) {
				return comunidade;
			}
		}
		List<Comunidade> comunidade = comunidadeDao.findByNomeOrderByNomeAsc(nome);
		if (comunidade == null || comunidade.size() != 1) {
			throw new BoException("Comunidade inexistente [%s]", nome);
			// logger.error(String.format("Comunidade inexistente [%s]", nome));
			// return null;
		}
		comunidadeList.add(comunidade.get(0));

		return comunidade.get(0);
	}

	private Cidade getEnderecoLocalizacao(String local, DbSater base) {
		if (local == null || local.trim().length() == 0) {
			return null;
		}
		local = ajustaNomeCidade(local, base.getSiglaEstado());
		List<Cidade> cidadeList = cidadeDao.findByNomeLikeAndMunicipioEstadoSiglaIn(local, "DF", "GO");
		if (cidadeList != null && cidadeList.size() == 1) {
			return cidadeList.get(0);
		}
		return null;
	}

	public FormularioVersao getFormularioVersao(String codigoFormulario) {
		if (formularioVersaoList == null) {
			formularioVersaoList = new ArrayList<FormularioVersao>();
		}
		for (FormularioVersao formularioVersao : formularioVersaoList) {
			if (formularioVersao.getFormulario().getCodigo().equals(codigoFormulario)) {
				return formularioVersao;
			}
		}
		FormularioVersao formularioVersao = formularioVersaoDao.findOneByFormularioCodigoAndVersao(codigoFormulario, 1);
		formularioVersaoList.add(formularioVersao);
		return formularioVersao;
	}

	public SistemaProducao getSistemaProducao(String nome) throws BoException {
		if (nome == null || nome.trim().length() == 0) {
			// throw new BoException("Sistema de Produção não informada");
			logger.error(String.format("Sistema de Produção não informada"));
			return null;
		}
		switch (nome) {
		case "Em transição":
		case "Transição Agroecológica":
		case "Transição agroecológica":
			nome = "Convencional (Transição)";
			break;

		case "Base Agroecológica":
			nome = "Agroecológico";
			break;

		case "Convencional (Prát. Presev.+Trans.)":
			nome = "Convencional (Prática Preservacionista + Transição)";
			break;

		case "Convenc. + Prát. Preserv.":
		case "Convencional (Prát. Preserv.)":
		case "Convencional":
			nome = "Convencional (Prática Preservacionista)";
			break;
		}

		for (SistemaProducao sistemaProducao : sistemaProducaoList) {
			if (sistemaProducao.getNome().equalsIgnoreCase(nome)) {
				return sistemaProducao;
			}
		}
		SistemaProducao sistemaProducao = sistemaProducaoDao.findOneByNomeIgnoreCase(nome);
		if (sistemaProducao == null) {
			throw new BoException("Sistema de Produção não cadastrado [%s]", nome);
			// logger.error(String.format("Sistema de Produção não cadastrado
			// [%s]", nome));
			// return null;
		}
		sistemaProducaoList.add(sistemaProducao);

		return sistemaProducao;
	}

	public void pessoaConfiguraNome(Pessoa pessoa, String nome, String apelidoSigla) {
		pessoa.setNome(UtilitarioString.formataNomeProprio(nome));
		pessoa.setApelidoSigla(apelidoSigla);
		if (pessoa.getNome() != null) {
			pessoa.setNome(pessoa.getNome().trim());
			if (pessoa.getNome().length() > 0 && pessoa.getApelidoSigla() == null
					|| pessoa.getApelidoSigla().trim().length() == 0) {
				pessoa.setApelidoSigla(pessoa.getNome().split("\\s")[0]);
			}
		}
	}
}