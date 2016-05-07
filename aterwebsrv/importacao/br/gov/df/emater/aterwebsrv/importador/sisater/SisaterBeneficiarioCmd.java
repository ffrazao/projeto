package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.util.ArquivoConstantes;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EstadoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.MunicipioDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PaisDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaNacionalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pais;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

@Service
public class SisaterBeneficiarioCmd extends _Comando {

	private static final String SQL = "SELECT BENEF.* FROM BENEF00 BENEF ORDER BY BENEF.BFNOME";

	private static final String SQL_FOTO = "SELECT BENEF.BFFOTO FROM BENEF02 BENEF WHERE BENEF.IDBEN = ?";

	private Calendar agora;

	@Autowired
	private ArquivoDao arquivoDao;

	private DbSater base;

	private Pais brasil;

	private Connection con;

	private Estado distritoFederal;

	@Autowired
	private EstadoDao estadoDao;

	private ImpUtil impUtil;

	@Autowired
	private MunicipioDao municipioDao;

	@Autowired
	private PaisDao paisDao;

	private PreparedStatement psFoto;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");
		brasil = (Pais) contexto.get("brasil");
		distritoFederal = (Estado) contexto.get("distritoFederal");

		agora = (Calendar) contexto.get("agora");

		psFoto = con.prepareStatement(SQL_FOTO);

		impUtil.criarMarcaTabela(con, "BENEF00");

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL);) {
			while (rs.next()) {
				Pessoa pessoa = null;

				String natureza = UtilitarioString.semAcento(rs.getString("BFNATUREZA"));
				if (natureza.equalsIgnoreCase("fisica")) {
					pessoa = novoPessoaFisica(rs);
				} else if (natureza.equalsIgnoreCase("juridica")) {
					pessoa = novoPessoaJuridica(rs);
				} else {
					throw new BoException("Natureza da Pessoa não identificada %s", natureza);
				}
				if (pessoa == null) {
					throw new BoException("Não foi possível identificar os dados pessoais");
				}
				pessoa.setChaveSisater(impUtil.chaveBeneficiario(base, rs.getString("IDUND"), rs.getString("IDBEN")));
				pessoa.setApelidoSigla(rs.getString("BFAPELIDO"));
				pessoa.setNome(rs.getString("BFNOME"));
				pessoa.setPublicoAlvoConfirmacao(Confirmacao.S);
				pessoa.setPublicoAlvo(novoPublicoAlvo(pessoa, rs));

				// pessoa.setEnderecoList(captarEnderecoList(rs));
				// pessoa.setTelefoneList(captarTelefoneList(rs));
				// pessoa.setEmailList(captarEmailList(rs));
				// pessoa.setGrupoSocialList(captarGrupoSocialList(rs));
				// pessoa.setRelacionamentoList(captarRelacionamentoList(rs));
				// pessoa.setDiagnosticoList(captarDiagnosticoList(rs));

				if (rs.getString("BFINSCEST") != null) {
					pessoa.setInscricaoEstadualUf(distritoFederal.getSigla());
					pessoa.setInscricaoEstadual(rs.getString("BFINSCEST"));
				}
				pessoa.setObservacoes(rs.getString("OBSERVACAO"));

				pessoa.setPerfilArquivo(perfilArquivoCaptar(rs.getString("IDBEN")));
				pessoa.setSituacao(PessoaSituacao.A);
				pessoa.setSituacaoData(agora);
			}
		}
		return false;
	}

	private EntidadeBase[] localNascimentoCapta(String naturalidade) {
		EntidadeBase[] result = new EntidadeBase[2];
		if (naturalidade == null || naturalidade.trim().length() == 0) {
			return result;
		}
		naturalidade = naturalidade.trim().replaceAll("  ", " ").replaceAll(" -", "-").replaceAll("- ", "-").replaceAll(" /", "/").replaceAll("/ ", "/");
		localNascimentoCaptaEstadoMunicipio(result, naturalidade, "-");
		if (result[0] == null) {
			localNascimentoCaptaEstadoMunicipio(result, naturalidade, "/");
		}
		if (result[0] == null) {
			List<Municipio> municipioList = municipioDao.findByNomeLike(naturalidade.trim());
			if (municipioList != null && municipioList.size() == 1) {
				result[0] = municipioList.get(0);
				result[1] = ((Municipio) result[0]).getEstado().getPais();
			} else {
				List<Pais> paisList = paisDao.findByNomeLike(naturalidade.trim());
				if (paisList != null && paisList.size() == 1) {
					result[1] = paisList.get(0);
				}
			}
		}
		return result;
	}

	private void localNascimentoCaptaEstadoMunicipio(EntidadeBase[] result, String naturalidade, String separador) {
		if (naturalidade.indexOf(separador) >= 0) {
			String temp[] = naturalidade.split(separador);
			String siglaEstado = temp[temp.length - 1];
			StringBuilder nomeMunicipio = new StringBuilder();
			for (int i = 0; i <= temp.length - 2; i++) {
				if (nomeMunicipio.length() > 0) {
					nomeMunicipio.append("-");
				}
				nomeMunicipio.append(temp[i].trim());
			}
			localNascimentoPesquisa(result, brasil, siglaEstado, nomeMunicipio.toString());
		}
	}

	private void localNascimentoPesquisa(EntidadeBase[] result, Pais pais, String siglaEstado, String nomeMunicipio) {
		Estado estado = estadoDao.findOneByPaisAndSigla(pais, siglaEstado.trim());
		if (estado != null) {
			List<Municipio> municipioList = municipioDao.findByEstadoAndNomeLike(estado, nomeMunicipio.trim());
			if (municipioList != null && municipioList.size() == 1) {
				result[0] = municipioList.get(0);
				result[1] = ((Municipio) result[0]).getEstado().getPais();
			}
		}
	}

	private PessoaFisica novoPessoaFisica(ResultSet rs) throws SQLException {
		PessoaFisica result = new PessoaFisica();

		// result.setCamNumero(rs.getString("camNumero"));
		// result.setCamOrgao(rs.getString("camOrgao"));
		// result.setCamSerie(rs.getString("camSerie"));
		// result.setCamUnidadeMilitar(rs.getString("camUnidadeMilitar"));
		// result.setCertidaoCasamentoCartorio(rs.getString("certidaoCasamentoCartorio"));
		// result.setCertidaoCasamentoFolha(rs.getString("certidaoCasamentoFolha"));
		// result.setCertidaoCasamentoLivro(rs.getString("certidaoCasamentoLivro"));
		// result.setCertidaoCasamentoRegime(rs.getString("certidaoCasamentoRegime"));
		// result.setCnhCategoria(rs.getString("cnhCategoria"));
		// result.setCnhNumero(rs.getString("cnhNumero"));
		// result.setCnhPrimeiraHabilitacao(rs.getString("cnhPrimeiraHabilitacao"));
		// result.setCnhValidade(rs.getString("cnhValidade"));
		result.setCpf(rs.getString("BFCPF"));
		// result.setCtpsNumero(rs.getString("ctpsNumero"));
		// result.setCtpsSerie(rs.getString("ctpsSerie"));
		result.setEscolaridade(impUtil.deParaEscolaridade(rs.getString("BFINS")));
		result.setEstadoCivil(impUtil.deParaEstadoCivil(rs.getString("BFCIVIL")));
		result.setGenero(impUtil.deParaGenero(rs.getString("BFGENERO")));
		result.setNascimento(impUtil.captaData(rs.getDate("BFDTNASC")));

		String naturalidade = rs.getString("BFNATURALIDADE");
		if (naturalidade != null) {
			EntidadeBase[] nascimento = localNascimentoCapta(naturalidade);

			result.setNascimentoMunicipio((Municipio) nascimento[0]);
			result.setNascimentoPais((Pais) nascimento[1]);

			if (result.getNascimentoPais() != null) {
				result.setNacionalidade(Confirmacao.S.equals(result.getNascimentoPais().getPadrao()) ? PessoaNacionalidade.BN : PessoaNacionalidade.ES);
			}
		}

		result.setNisNumero(rs.getString("BFNIS"));
		result.setProfissao(impUtil.deParaProfissao(rs.getString("BFPROFISSAO")));
		result.setRgDataEmissao(impUtil.captaData(rs.getDate("BFIDEEXP")));

		result.setRgNumero(rs.getString("BFIDENR"));
		result.setRgOrgaoEmissor(rs.getString("BFIDEORG"));
		// result.setTituloNumero(rs.getString("tituloNumero"));
		// result.setTituloSecao(rs.getString("tituloSecao"));
		// result.setTituloZona(rs.getString("tituloZona"));

		return result;
	}

	private PessoaJuridica novoPessoaJuridica(ResultSet rs) throws SQLException {
		PessoaJuridica result = new PessoaJuridica();
		result.setCnpj(rs.getString("BFCPF"));
		return result;
	}

	private PublicoAlvo novoPublicoAlvo(Pessoa pessoa, ResultSet rs) {
		PublicoAlvo result = new PublicoAlvo();
		return result;
	}

	private Arquivo perfilArquivoCaptar(String idben) throws SQLException, IOException {
		Arquivo result = new Arquivo();

		psFoto.setString(1, idben);
		try (ResultSet rs = psFoto.executeQuery()) {
			if (rs.next()) {				
				result.setConteudo(ConexaoFirebird.lerCampoBlob(rs, "BFFOTO"));
			}
		}
		if (result.getConteudo() == null) {
			return null;
		}
		result.setMd5(Criptografia.MD5_FILE(result.getConteudo()));
		Arquivo salvo = arquivoDao.findByMd5(result.getMd5());
		if (salvo != null) {
			return salvo;
		}
		result.setExtensao(".jpg");
		result.setDataUpload(agora);
		result.setMimeTipo("image/jpeg");
		result.setNomeOriginal(String.format("perfil-%s.jpg", idben));
		result.setTipo(ArquivoTipo.P);
		result.setTamanho(result.getConteudo().length);
		result.setLocalDiretorioWeb(ArquivoConstantes.DIRETORIO_PERFIL.concat(File.separator).concat(result.getMd5()));
		result = arquivoDao.save(result);
		return result;
	}

}