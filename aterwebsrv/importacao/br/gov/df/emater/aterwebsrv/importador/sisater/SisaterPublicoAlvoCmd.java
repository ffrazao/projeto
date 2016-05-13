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

import org.apache.commons.lang3.EnumUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.util.ArquivoConstantes;
import br.gov.df.emater.aterwebsrv.dao.formulario.ColetaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.EstadoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.GrupoSocialDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.MunicipioDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PaisDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoFuncaoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.RelacionamentoTipoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.CadastroAcao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaNacionalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.TelefoneTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UF;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Coleta;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Email;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Estado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.GrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Municipio;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pais;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEmail;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaEndereco;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaGrupoSocial;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaTelefone;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Relacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Telefone;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service
public class SisaterPublicoAlvoCmd extends _Comando {

	private class CheckDiagnosticoList {

		private Pessoa pessoa;

		private ResultSet rs;

		void avaliaBeneficioSocial() throws SQLException, JsonProcessingException {
			// Captar informações sociais
			Map<String, Object> formulario = new HashMap<String, Object>();
			formulario.put("carteiraTrabalho", impUtil.deParaConfirmacao(rs.getString("BENEFSOC1")));
			formulario.put("aposentadoriaPensaoEfetivada", impUtil.deParaConfirmacao(rs.getString("BENEFSOC2")));
			formulario.put("beneficiarioProgramaSocial", impUtil.deParaConfirmacao(rs.getString("BENEFSOC3")));
			formulario.put("portadorNecessidadeEspecial", impUtil.deParaConfirmacao(rs.getString("BENEFSOC4")));
			try {
				formulario.put("salarioMaternidade", impUtil.deParaConfirmacao(rs.getString("FR_SALARIO_MATERNIDADE")));
				formulario.put("auxilioDoenca", impUtil.deParaConfirmacao(rs.getString("FR_AUXILIO_DOENCA")));
			} catch (SQLException e) {
			}
			formulario.put("eventualDiaHomemAno", rs.getDouble("BFEMPEVEN"));
			formulario.put("trabalhadorPermanente", rs.getDouble("BFEMPPERM"));
			formulario.put("salarioMensal", rs.getDouble("BFEMPSALARIO"));

			Coleta coleta = new Coleta(null, impUtil.getFormularioVersao("beneficioSocialForcaTrabalho"), ematerUsuario, agora, Confirmacao.S, pessoa, null, null, new ObjectMapper().writeValueAsString(formulario));
			coleta.setChaveSisater(impUtil.chaveColetaFormulario(base, rs.getString("IDUND"), rs.getString("IDBEN"), true, "beneficioSocialForcaTrabalho"));
			Coleta coletaSalvo = coletaDao.findOneByChaveSisater(coleta.getChaveSisater());
			if (coletaSalvo != null) {
				coleta.setId(coletaSalvo.getId());
			}
			coletaDao.save(coleta);
		}

		void avaliaPatrimonioDivida() throws SQLException, Exception {
			// Captar máquinas e equipamentos
			psPatrimEqp.setString(1, rs.getString("IDBEN"));
			ResultSet rsPatrimEqp = psPatrimEqp.executeQuery();
			List<Map<String, Object>> maquinaEquipamentoList = null;
			while (rsPatrimEqp.next()) {
				Map<String, Object> linha = new HashMap<String, Object>();
				linha.put("discriminacao", rsPatrimEqp.getString("ITEM"));
				linha.put("marca", rsPatrimEqp.getString("MARCA"));
				linha.put("chassi", rsPatrimEqp.getString("CHASSI"));
				linha.put("ano", rsPatrimEqp.getInt("ANO"));
				linha.put("quantidade", rsPatrimEqp.getInt("QUANT"));
				linha.put("valorUnitario", rsPatrimEqp.getString("VALORUND"));

				if (maquinaEquipamentoList == null) {
					maquinaEquipamentoList = new ArrayList<Map<String, Object>>();
				}
				maquinaEquipamentoList.add(linha);
			}

			// Captar máquinas e equipamentos
			psPatrimAni.setString(1, rs.getString("IDBEN"));
			ResultSet rsPatrimAni = psPatrimAni.executeQuery();
			List<Map<String, Object>> semoventeList = null;
			while (rsPatrimAni.next()) {
				Map<String, Object> linha = new HashMap<String, Object>();
				linha.put("discriminacao", rsPatrimAni.getString("ITEM"));
				linha.put("quantidade", rsPatrimAni.getInt("QUANT"));
				linha.put("unidade", rsPatrimAni.getString("UNID"));
				linha.put("valorUnitario", rsPatrimAni.getString("VALORUND"));

				if (semoventeList == null) {
					semoventeList = new ArrayList<Map<String, Object>>();
				}
				semoventeList.add(linha);
			}

			// Captar outros bens
			psPatrimOut.setString(1, rs.getString("IDBEN"));
			ResultSet rsPatrimOut = psPatrimOut.executeQuery();
			List<Map<String, Object>> outroPatrimonioList = null;
			while (rsPatrimOut.next()) {
				Map<String, Object> linha = new HashMap<String, Object>();
				linha.put("discriminacao", rsPatrimOut.getString("ITEM"));
				linha.put("quantidade", rsPatrimOut.getInt("QUANT"));
				linha.put("unidade", rsPatrimOut.getString("UNID"));
				linha.put("valorUnitario", rsPatrimOut.getString("VALORUND"));
				if (outroPatrimonioList == null) {
					outroPatrimonioList = new ArrayList<Map<String, Object>>();
				}
				outroPatrimonioList.add(linha);
			}

			// Captar outros bens
			psDivida.setString(1, rs.getString("IDBEN"));
			ResultSet rsDivida = psDivida.executeQuery();
			List<Map<String, Object>> dividaList = null;
			while (rsDivida.next()) {
				Map<String, Object> linha = new HashMap<String, Object>();
				linha.put("finalidade", impUtil.deParaDividaFinalidade(rsDivida.getString("FINALIDADE")));
				linha.put("especificacao", rsDivida.getString("ESPECIFIC"));
				linha.put("contratacao", impUtil.captaData(rsDivida.getDate("DTCONTRAT")));
				linha.put("vencimento", impUtil.captaData(rsDivida.getDate("DTVENC")));
				linha.put("juroAnual", rsDivida.getDouble("ENCARGO"));
				linha.put("valorContratado", rsDivida.getDouble("SALDODEV"));
				linha.put("amortizacaoAnual", rsDivida.getDouble("AMORTIZA"));
				linha.put("saldoDevedor", rsDivida.getDouble("VALCONTRAT"));
				if (dividaList == null) {
					dividaList = new ArrayList<Map<String, Object>>();
				}
				dividaList.add(linha);
			}

			if (maquinaEquipamentoList != null || semoventeList != null || outroPatrimonioList != null || dividaList != null) {
				Map<String, Object> formulario = new HashMap<String, Object>();
				formulario.put("maquinaEquipamentoList", maquinaEquipamentoList);
				formulario.put("semoventeList", semoventeList);
				formulario.put("outroPatrimonioList", outroPatrimonioList);
				formulario.put("dividaList", dividaList);

				Coleta coleta = new Coleta(null, impUtil.getFormularioVersao("patrimonioDivida"), ematerUsuario, agora, Confirmacao.S, pessoa, null, null, new ObjectMapper().writeValueAsString(formulario));
				coleta.setChaveSisater(impUtil.chaveColetaFormulario(base, rs.getString("IDUND"), rs.getString("IDBEN"), true, "patrimonioDivida"));
				Coleta coletaSalvo = coletaDao.findOneByChaveSisater(coleta.getChaveSisater());
				if (coletaSalvo != null) {
					coleta.setId(coletaSalvo.getId());
				}
				coletaDao.save(coleta);
			}
		}

		void set(ResultSet rs, Pessoa pessoa) {
			this.rs = rs;
			this.pessoa = pessoa;
		}
	}

	private class CheckEnderecoList {

		private ResultSet rs;

		private Pessoa salvo;

		public void avalia(List<PessoaEndereco> result, String... nomeCampo) throws SQLException {
			String logradouro = rs.getString(nomeCampo[0]);
			String cep = rs.getString(nomeCampo[1]);
			if (logradouro == null || logradouro.trim().length() == 0) {
				return;
			}

			PessoaEndereco sisater = new PessoaEndereco(null, new Endereco(null, logradouro, cep), "P");
			sisater.getEndereco().setPropriedadeRuralConfirmacao(Confirmacao.N);
			sisater.getEndereco().setEnderecoAtualizado(Confirmacao.N);
			sisater.setChaveSisater(impUtil.chavePessoaEndereco(base, rs.getString("IDUND"), rs.getString("IDBEN"), nomeCampo));

			boolean encontrou = false;
			if (salvo != null && salvo.getEnderecoList() != null) {
				for (PessoaEndereco pessoaEnderecoSalvo : salvo.getEnderecoList()) {
					PessoaEndereco pt = new PessoaEndereco(pessoaEnderecoSalvo.getId(), pessoaEnderecoSalvo.getEndereco(), pessoaEnderecoSalvo.getFinalidade());
					if (sisater.getChaveSisater().equals(pessoaEnderecoSalvo.getChaveSisater()) || String.format("%s%s", logradouro, cep == null ? null : String.format("\n%s", cep)).equals(pessoaEnderecoSalvo.getEndereco().getEnderecoSisater())) {
						pt.setEndereco(new Endereco(null, logradouro, cep));
						pt.setChaveSisater(sisater.getChaveSisater());
						pt.getEndereco().setEnderecoAtualizado(Confirmacao.N);
						encontrou = true;
					}
					result.add(pt);
				}
			}
			if (!encontrou) {
				result.add(sisater);
			}
		}

		void set(ResultSet rs, Pessoa salvo) {
			this.rs = rs;
			this.salvo = salvo;
		}
	}

	private class CheckGrupoSocialList {
		private ResultSet rs;
		private Pessoa salvo;

		public void avalia(List<PessoaGrupoSocial> result, String nomeCampo, GrupoSocial grupoSocial) throws SQLException {
			String informado = rs.getString(nomeCampo);

			PessoaGrupoSocial sisater = new PessoaGrupoSocial(null, grupoSocial);
			sisater.setChaveSisater(impUtil.chavePessoaGrupoSocial(base, rs.getString("IDUND"), rs.getString("IDBEN"), nomeCampo));
			sisater.setCadastroAcao("Sim".equalsIgnoreCase(informado) ? CadastroAcao.I : CadastroAcao.E);

			boolean encontrou = false;
			if (salvo != null && salvo.getGrupoSocialList() != null) {
				principal: for (PessoaGrupoSocial pessoaGrupoSocialSalvo : salvo.getGrupoSocialList()) {
					PessoaGrupoSocial pt = new PessoaGrupoSocial(pessoaGrupoSocialSalvo.getId(), pessoaGrupoSocialSalvo.getGrupoSocial());
					pt.setChaveSisater(pessoaGrupoSocialSalvo.getChaveSisater());
					if (sisater.getChaveSisater().equals(pt.getChaveSisater()) || grupoSocial.getId().equals(pt.getGrupoSocial().getId())) {
						pt.setGrupoSocial(grupoSocial);
						pt.setChaveSisater(sisater.getChaveSisater());
						pt.setCadastroAcao(sisater.getCadastroAcao());
						encontrou = true;
					}
					for (PessoaGrupoSocial p : result) {
						if (p.getGrupoSocial().getId().equals(pt.getGrupoSocial().getId())) {
							if (p.getCadastroAcao() == null && pt.getCadastroAcao() != null) {
								p.setCadastroAcao(pt.getCadastroAcao());
							}
							continue principal;
						}
					}
					result.add(pt);
				}
			}
			if (!encontrou && CadastroAcao.I.equals(sisater.getCadastroAcao())) {
				result.add(sisater);
			}
		}

		void set(ResultSet rs, Pessoa salvo) {
			this.rs = rs;
			this.salvo = salvo;
		}
	}

	private class CheckRelacionamentoList {

		private ResultSet rs;

		private Pessoa salvo;

		public void avaliar(List<PessoaRelacionamento> result, PessoaGenero genero, RelacionamentoFuncao relacionamentoFuncao, String... nomeCampo) throws SQLException, BoException {
			if (rs.getString(nomeCampo[0]) == null || rs.getString(nomeCampo[0]).trim().length() == 0) {
				return;
			}
			PessoaFisica pessoa = new PessoaFisica();
			pessoa.setGenero(PessoaGenero.M.equals(genero) ? PessoaGenero.F : PessoaGenero.M);
			if (nomeCampo.length == 1) {
				impUtil.pessoaConfiguraNome(pessoa, rs.getString(nomeCampo[0]), null);
			} else {
				impUtil.pessoaConfiguraNome(pessoa, rs.getString(nomeCampo[0]), nomeCampo[1] == null ? null : rs.getString(nomeCampo[1]));
				pessoa.setCpf(nomeCampo[2] == null ? null : UtilitarioString.formataCpf(rs.getString(nomeCampo[2])));
				pessoa.setRgNumero(nomeCampo[3] == null ? null : rs.getString(nomeCampo[3]));
				pessoa.setRgDataEmissao(nomeCampo[4] == null ? null : impUtil.captaData(rs.getDate(nomeCampo[4])));
				String localRg = nomeCampo[5] == null ? null : rs.getString(nomeCampo[5]);
				if (localRg != null && localRg.trim().length() > 0) {
					String[] localRgItems = localRgCapta(localRg);
					pessoa.setRgOrgaoEmissor(localRgItems[0]);
					pessoa.setRgUf(localRgItems[1]);
				}
				pessoa.setProfissao(nomeCampo[6] == null ? null : impUtil.deParaProfissao(rs.getString(nomeCampo[6])));
				String naturalidade = nomeCampo[7] == null ? null : rs.getString(nomeCampo[7]);
				if (naturalidade != null && naturalidade.trim().length() > 0) {
					EntidadeBase[] nascimento = localNascimentoCapta(naturalidade);
					pessoa.setNascimentoMunicipio((Municipio) nascimento[0]);
					pessoa.setNascimentoPais((Pais) nascimento[1]);
					if (pessoa.getNascimentoPais() != null) {
						pessoa.setNacionalidade(Confirmacao.S.equals(pessoa.getNascimentoPais().getPadrao()) ? PessoaNacionalidade.BN : PessoaNacionalidade.ES);
					}
				}
				pessoa.setNascimento(nomeCampo[8] == null ? null : impUtil.captaData(rs.getDate(nomeCampo[8])));
				pessoa.setEscolaridade(nomeCampo[9] == null ? null : impUtil.deParaEscolaridade(rs.getString(nomeCampo[9])));
				pessoa.setCertidaoCasamentoRegime(nomeCampo[10] == null ? null : impUtil.deParaRegimeCasamento(rs.getString(nomeCampo[10])));
				pessoa.setNomeMaeConjuge(nomeCampo[11] == null ? null : rs.getString(nomeCampo[11]));
			}
			pessoaRelacionamento(result, relacionamentoFuncao, pessoa, nomeCampo[0]);
		}

		public void pessoaRelacionamento(List<PessoaRelacionamento> result, RelacionamentoFuncao relacionamentoFuncao, PessoaFisica pessoa, String nomeCampo) throws SQLException, BoException {
			PessoaRelacionamento sisater = new PessoaRelacionamento(null, new Relacionamento(null, null, null, getRelacionamentoTipoFamiliar()), pessoa, relacionamentoFuncao);
			sisater.setChaveSisater(impUtil.chavePessoaRelacionamento(base, rs.getString("IDUND"), rs.getString("IDBEN"), nomeCampo));
			sisater.transformarInformacao();

			if (salvo != null) {
				List<Relacionamento> relacionamentoList = relacionamentoDao.findByPessoaRelacionamentoListPessoa(salvo);
				if (relacionamentoList != null) {
					salvo: for (Relacionamento relacionamentoSalvo : relacionamentoList) {
						for (PessoaRelacionamento pessoaRelacionamentoSalvo : relacionamentoSalvo.getPessoaRelacionamentoList()) {
							if (sisater.getChaveSisater().equals(pessoaRelacionamentoSalvo.getChaveSisater())) {
								sisater.setId(pessoaRelacionamentoSalvo.getId());
								sisater.getRelacionamento().setId(pessoaRelacionamentoSalvo.getRelacionamento().getId());
								break salvo;
							}
						}
					}
				}
			}

			result.add(sisater);
		}

		void set(ResultSet rs, Pessoa salvo) {
			this.rs = rs;
			this.salvo = salvo;
		}
	}

	private class CheckTelefoneList {

		private ResultSet rs;

		private Pessoa salvo;

		public void avalia(List<PessoaTelefone> result, String nomeCampo, TelefoneTipo tipo) throws SQLException {
			String informado = rs.getString(nomeCampo);
			if (informado != null && informado.trim().length() > 0) {
				informado = UtilitarioString.formataTelefone(informado);

				PessoaTelefone sisater = new PessoaTelefone(null, new Telefone(informado), "P", tipo);
				sisater.setChaveSisater(impUtil.chavePessoaTelefone(base, rs.getString("IDUND"), rs.getString("IDBEN"), nomeCampo));

				boolean encontrou = false;
				if (salvo != null && salvo.getTelefoneList() != null) {
					for (PessoaTelefone pessoaTelefoneSalvo : salvo.getTelefoneList()) {
						PessoaTelefone pt = new PessoaTelefone(pessoaTelefoneSalvo.getId(), pessoaTelefoneSalvo.getTelefone(), pessoaTelefoneSalvo.getFinalidade(), pessoaTelefoneSalvo.getTipo());
						if (sisater.getChaveSisater().equals(pessoaTelefoneSalvo.getChaveSisater()) || informado.equals(pessoaTelefoneSalvo.getTelefone().getNumero())) {
							pt.setTelefone(new Telefone(informado));
							pt.setChaveSisater(sisater.getChaveSisater());
							encontrou = true;
						}
						result.add(pt);
					}
				}
				if (!encontrou) {
					result.add(sisater);
				}
			}
		}

		void set(ResultSet rs, Pessoa salvo) {
			this.rs = rs;
			this.salvo = salvo;
		}
	}

	private static final String SISATER_CAMPO = "BFNOME";

	private static final String SISATER_TABELA = "BENEF00";

	private static final String SQL = String.format("SELECT T.* FROM %s T ORDER BY T.%s", SISATER_TABELA, SISATER_CAMPO);
	private static final String SQL_DIVIDA = "SELECT T.* FROM DIVIDA T WHERE T.IDBEN = ?";

	private static final String SQL_FOTO = "SELECT T.BFFOTO FROM BENEF02 T WHERE T.IDBEN = ?";

	private static final String SQL_PATRIMANI = "SELECT T.* FROM PATRIMANI T WHERE T.IDBEN = ?";

	private static final String SQL_PATRIMEQP = "SELECT T.* FROM PATRIMEQP T WHERE T.IDBEN = ?";

	private static final String SQL_PATRIMOUT = "SELECT T.* FROM PATRIMOUT T WHERE T.IDBEN = ?";

	private Calendar agora;

	@Autowired
	private ArquivoDao arquivoDao;

	private DbSater base;

	private Pais brasil;

	private CheckDiagnosticoList checkDiagnosticoList = new CheckDiagnosticoList();

	private CheckEnderecoList checkEnderecoList = new CheckEnderecoList();

	private CheckGrupoSocialList checkGrupoSocialList = new CheckGrupoSocialList();

	private CheckRelacionamentoList checkRelacionamentoList = new CheckRelacionamentoList();

	private CheckTelefoneList checkTelefoneList = new CheckTelefoneList();

	@Autowired
	private ColetaDao coletaDao;

	private Connection con;

	private Estado distritoFederal;

	private Usuario ematerUsuario;

	@Autowired
	private EstadoDao estadoDao;

	private FacadeBo facadeBo;

	@Autowired
	private GrupoSocialDao grupoSocialDao;

	private List<GrupoSocial> grupoSocialList;

	private ImpUtil impUtil;

	@Autowired
	private MunicipioDao municipioDao;

	@Autowired
	private PaisDao paisDao;

	@Autowired
	private PessoaDao pessoaDao;

	private PreparedStatement psDivida;

	private PreparedStatement psFoto;

	private PreparedStatement psPatrimAni;

	private PreparedStatement psPatrimEqp;

	private PreparedStatement psPatrimOut;

	@Autowired
	private RelacionamentoDao relacionamentoDao;

	@Autowired
	private RelacionamentoFuncaoDao relacionamentoFuncaoDao;

	private List<RelacionamentoFuncao> relacionamentoFuncaoList;

	@Autowired
	private RelacionamentoTipoDao relacionamentoTipoDao;

	private RelacionamentoTipo relacionamentoTipoFamiliar;

	private void captarDiagnosticoList(ResultSet rs, Pessoa pessoa) throws Exception {
		checkDiagnosticoList.set(rs, pessoa);
		checkDiagnosticoList.avaliaPatrimonioDivida();
		checkDiagnosticoList.avaliaBeneficioSocial();
	}

	private List<PessoaEmail> captarEmailList(ResultSet rs, Pessoa salvo) throws SQLException {
		List<PessoaEmail> result = null;
		String emailStr = rs.getString("BFEMAIL");
		if (emailStr != null) {
			result = new ArrayList<PessoaEmail>();

			PessoaEmail sisater = new PessoaEmail(null, new Email(emailStr.toLowerCase()), "P");
			sisater.setChaveSisater(impUtil.chavePessoaEmail(base, rs.getString("IDUND"), rs.getString("IDBEN")));

			boolean encontrou = false;
			if (salvo != null && salvo.getEmailList() != null) {
				for (PessoaEmail pessoaEmailSalvo : salvo.getEmailList()) {
					PessoaEmail pe = new PessoaEmail(pessoaEmailSalvo.getId(), pessoaEmailSalvo.getEmail(), pessoaEmailSalvo.getFinalidade());
					if (sisater.getChaveSisater().equals(pessoaEmailSalvo.getChaveSisater()) || emailStr.equals(pessoaEmailSalvo.getEmail().getEndereco())) {
						pe.getEmail().setEndereco(emailStr);
						pe.setChaveSisater(sisater.getChaveSisater());
						encontrou = true;
					}
					result.add(pe);
				}
			}
			if (!encontrou) {
				result.add(sisater);
			}
		}
		return result;
	}

	private List<PessoaEndereco> captarEnderecoList(ResultSet rs, Pessoa salvo) throws SQLException {
		List<PessoaEndereco> result = new ArrayList<PessoaEndereco>();
		checkEnderecoList.set(rs, salvo);
		checkEnderecoList.avalia(result, "BFENDERECO1", "BFCEP1");
		checkEnderecoList.avalia(result, "BFENDERECO2", "BFCEP2");

		return result;
	}

	private List<PessoaGrupoSocial> captarGrupoSocialList(ResultSet rs, Pessoa salvo) throws SQLException {
		List<PessoaGrupoSocial> result = new ArrayList<PessoaGrupoSocial>();

		checkGrupoSocialList.set(rs, salvo);
		checkGrupoSocialList.avalia(result, "PEATEPA", getGrupoSocial("ATEPA"));
		checkGrupoSocialList.avalia(result, "PEINCRA", getGrupoSocial("INCRA"));
		checkGrupoSocialList.avalia(result, "PEBSM", getGrupoSocial("Brasil Sem Miséria"));
		checkGrupoSocialList.avalia(result, "PESUSTENTABILIDADE", getGrupoSocial("Sustentabilidade"));

		return result;
	}

	private Arquivo captarPerfilArquivo(String idben) throws SQLException, IOException {
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

	private List<PessoaRelacionamento> captarRelacionamentoList(ResultSet rs, Pessoa salvo, Pessoa eu) throws SQLException, BoException {
		if (!PessoaTipo.PF.equals(eu.getPessoaTipo())) {
			return null;
		}
		List<PessoaRelacionamento> result = new ArrayList<PessoaRelacionamento>();

		checkRelacionamentoList.set(rs, salvo);
		checkRelacionamentoList.avaliar(result, ((PessoaFisica) eu).getGenero(), getRelacionamentoFuncao("Esposo"), "BFCONJUGENOM", "BFCONJAPELIDO", "BFCONJUGECPF", "BFCONJUGEINR", "BFCONJUGEIEX", "BFCONJUGEIOG", "BFCONJUGEPRF", "BFCONJUGENAT", "BFCONJDTNASC", "BFCONJINS",
				"BFREGCASAMENTO", "BFCONJUGEMAE");
		checkRelacionamentoList.avaliar(result, PessoaGenero.F, getRelacionamentoFuncao("Pai"), "BFPAI");
		checkRelacionamentoList.avaliar(result, PessoaGenero.M, getRelacionamentoFuncao("Pai"), "BFMAE");

		return result;
	}

	private List<PessoaTelefone> captarTelefoneList(ResultSet rs, Pessoa salvo) throws SQLException {
		List<PessoaTelefone> result = new ArrayList<PessoaTelefone>();

		checkTelefoneList.set(rs, salvo);
		checkTelefoneList.avalia(result, "BFCELULAR", TelefoneTipo.CE);
		checkTelefoneList.avalia(result, "BFTELEF", TelefoneTipo.FI);

		return result;
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		facadeBo = (FacadeBo) contexto.get("facadeBo");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");
		brasil = (Pais) contexto.get("brasil");
		distritoFederal = (Estado) contexto.get("distritoFederal");
		ematerUsuario = (Usuario) contexto.get("ematerUsuario");

		agora = (Calendar) contexto.get("agora");

		psFoto = con.prepareStatement(SQL_FOTO);

		psPatrimEqp = con.prepareStatement(SQL_PATRIMEQP);

		psPatrimAni = con.prepareStatement(SQL_PATRIMANI);

		psPatrimOut = con.prepareStatement(SQL_PATRIMOUT);

		psDivida = con.prepareStatement(SQL_DIVIDA);

		impUtil.criarMarcaTabela(con, SISATER_TABELA);

		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(SQL);) {
			int cont = 0;
			while (rs.next()) {
				cont++;
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
				pessoa.setPublicoAlvoConfirmacao(Confirmacao.S);
				pessoa.setPublicoAlvo(novoPublicoAlvo(pessoa, rs));
				impUtil.pessoaConfiguraNome(pessoa, rs.getString("BFNOME"), rs.getString("BFAPELIDO"));

				// recuperar os IDs
				Pessoa salvo = pessoaDao.findOneByChaveSisater(pessoa.getChaveSisater());
				if (salvo != null) {
					pessoa.setId(salvo.getId());
				}

				pessoa.setEmailList(captarEmailList(rs, salvo));
				pessoa.setTelefoneList(captarTelefoneList(rs, salvo));
				pessoa.setEnderecoList(captarEnderecoList(rs, salvo));
				pessoa.setGrupoSocialList(captarGrupoSocialList(rs, salvo));
				pessoa.setRelacionamentoList(captarRelacionamentoList(rs, salvo, pessoa));

				if (rs.getString("BFINSCEST") != null) {
					pessoa.setInscricaoEstadualUf(distritoFederal.getSigla());
					pessoa.setInscricaoEstadual(rs.getString("BFINSCEST"));
				}
				pessoa.setObservacoes(rs.getString("OBSERVACAO"));

				pessoa.setPerfilArquivo(captarPerfilArquivo(rs.getString("IDBEN")));
				pessoa.setSituacao(PessoaSituacao.A);
				pessoa.setSituacaoData(agora);

				// salvar no MySQL e no Firebird
				pessoa.setId((Integer) facadeBo.pessoaSalvar(contexto.getUsuario(), pessoa).getResposta());

				impUtil.chaveAterWebAtualizar(con, pessoa.getId(), SISATER_TABELA, "IDUND = ? AND IDBEN =  ?", rs.getString("IDUND"), rs.getString("IDBEN"));

				// identificar último usuario que atualizou o registro
				Usuario atualizador = impUtil.deParaUsuario(rs.getString("IDEMP"));
				Calendar atualizacaoData = impUtil.captaData(rs.getDate("BFDTATUAL"));
				if (atualizador != null) {
					if (atualizacaoData != null) {
						pessoa.setInclusaoData(atualizacaoData);
						pessoa.setAlteracaoData(atualizacaoData);
					}
					pessoa.setUsuarioAlteracao(atualizador);
					pessoaDao.save(pessoa);
				}

				captarDiagnosticoList(rs, pessoa);
			}
			if (logger.isDebugEnabled()) {
				logger.debug(String.format("[%s] importado %d pessoas", base.name(), cont));
			}
		}
		return false;
	}

	private GrupoSocial getGrupoSocial(String nome) {
		if (grupoSocialList == null) {
			grupoSocialList = new ArrayList<GrupoSocial>();
		}
		for (GrupoSocial grupoSocial : grupoSocialList) {
			if (nome.equals(grupoSocial.getNome())) {
				return grupoSocial;
			}
		}
		GrupoSocial grupoSocial = grupoSocialDao.findByNome(nome);
		grupoSocialList.add(grupoSocial);
		return grupoSocial;
	}

	private RelacionamentoFuncao getRelacionamentoFuncao(String nomeSeMasculino) {
		if (relacionamentoFuncaoList == null) {
			relacionamentoFuncaoList = new ArrayList<RelacionamentoFuncao>();
		}
		for (RelacionamentoFuncao relacionamentoFuncao : relacionamentoFuncaoList) {
			if (nomeSeMasculino.equals(relacionamentoFuncao.getNomeSeMasculino())) {
				return relacionamentoFuncao;
			}
		}
		RelacionamentoFuncao relacionamentoFuncao = relacionamentoFuncaoDao.findOneByNomeSeMasculino(nomeSeMasculino);
		relacionamentoFuncaoList.add(relacionamentoFuncao);
		return relacionamentoFuncao;
	}

	private RelacionamentoTipo getRelacionamentoTipoFamiliar() {
		if (relacionamentoTipoFamiliar == null) {
			relacionamentoTipoFamiliar = relacionamentoTipoDao.findByCodigo(RelacionamentoTipo.Codigo.FAMILIAR.name());
		}
		return relacionamentoTipoFamiliar;
	}

	private EntidadeBase[] localNascimentoCapta(String naturalidade) {
		EntidadeBase[] result = new EntidadeBase[2];
		if (naturalidade == null || naturalidade.trim().length() == 0) {
			return result;
		}
		naturalidade = UtilitarioString.substituirTudo(naturalidade, "  ", " ");
		naturalidade = UtilitarioString.substituirTudo(naturalidade, "- ", "-");
		naturalidade = UtilitarioString.substituirTudo(naturalidade, " -", "-");
		naturalidade = UtilitarioString.substituirTudo(naturalidade, " /", "/");
		naturalidade = UtilitarioString.substituirTudo(naturalidade, "/ ", "/");
		naturalidade = naturalidade.trim();

		String uf = null;
		if (naturalidade.length() >= 2) {
			uf = naturalidade.substring(naturalidade.length() - 2).toUpperCase();
		}
		if (uf != null && EnumUtils.isValidEnum(UF.class, uf)) {
			String local = naturalidade.substring(0, naturalidade.length() - 2);
			if (local.endsWith("-") || local.endsWith("/")) {
				local = local.substring(0, local.length() - 1);
			}
			naturalidade = String.format("%s-%s", local, uf);
		}
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
		List<Municipio> municipioList = null;

		Estado estado = estadoDao.findOneByPaisAndSigla(pais, siglaEstado.trim());
		boolean encontrou = false;
		if (estado != null) {
			result[1] = estado.getPais();
			municipioList = municipioDao.findByEstadoAndNomeLike(estado, nomeMunicipio.trim());
			if (municipioList != null && municipioList.size() == 1) {
				encontrou = true;
				result[0] = municipioList.get(0);
			}
		}
		if (!encontrou) {
			municipioList = municipioDao.findByNomeLike(nomeMunicipio.trim());
			if (municipioList != null && municipioList.size() == 1) {
				result[0] = municipioList.get(0);
				result[1] = ((Municipio) result[0]).getEstado().getPais();
			}
		}
	}

	private String[] localRgCapta(String localRg) {
		String[] result = null;
		if (localRg == null || localRg.trim().length() == 0) {
			return result;
		}
		localRg = UtilitarioString.substituirTudo(localRg, "  ", " ");
		localRg = UtilitarioString.substituirTudo(localRg, "- ", "-");
		localRg = UtilitarioString.substituirTudo(localRg, " -", "-");
		localRg = UtilitarioString.substituirTudo(localRg, " /", "/");
		localRg = UtilitarioString.substituirTudo(localRg, "/ ", "/");
		localRg = localRg.trim();
		String uf = null;
		if (localRg.length() >= 2) {
			uf = localRg.substring(localRg.length() - 2).toUpperCase();
		}
		if (uf != null && EnumUtils.isValidEnum(UF.class, uf)) {
			String orgao = localRg.substring(0, localRg.length() - 2);
			if (orgao.endsWith("-") || orgao.endsWith("/")) {
				orgao = orgao.substring(0, orgao.length() - 1);
			}
			return new String[] { orgao, uf };
		}
		return new String[] { localRg, null };
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
		result.setCpf(UtilitarioString.formataCpf(rs.getString("BFCPF")));
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
		String localRg = rs.getString("BFIDEORG");
		if (localRg != null && localRg.trim().length() > 0) {
			String[] localRgItems = localRgCapta(localRg);
			result.setRgOrgaoEmissor(localRgItems[0]);
			result.setRgUf(localRgItems[1]);
		}
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

	private PublicoAlvo novoPublicoAlvo(Pessoa pessoa, ResultSet rs) throws Exception {
		PublicoAlvo result = new PublicoAlvo();
		result.setCategoria(impUtil.deParaPublicoAlvoCategoria(rs.getString("BFCATEGORIA")));
		result.setSegmento(impUtil.deParaPublicoAlvoSegmento(rs.getString("BFSEGMENTO")));
		result.setCarteiraProdutorEmissao(impUtil.captaData(rs.getDate("BFCARTEIRADT")));
		result.setCarteiraProdutorExpiracao(impUtil.captaData(rs.getDate("BFCARTRENDT")));
		// result.setCarteiraProdutorNumero(rs.getString(""));
		result.setDapNumero(rs.getString("BFDAPNR"));
		result.setDapObservacao(rs.getString("BFDAPOBS"));
		result.setDapSituacao(impUtil.deParaDapSituacao(rs.getString("BFDAP")));
		// result.setDapValidade(impUtil.captaData(rs.getDate("")));
		result.setOrganizacaoTipo(impUtil.deParaOrganizacaoTipo(rs.getString("BFORGANIZA")));
		result.setPublicoAlvoSetorList(impUtil.deParaPublicoAlvoSetorList(rs.getString("SETOR"), rs.getString("SETOR2")));
		String tradicao = UtilitarioString.soNumero(rs.getString("BFATIVTRAD"));
		if (tradicao != null && tradicao.length() > 0) {
			result.setTradicao(Integer.parseInt(tradicao));
		}
		return result;
	}

}