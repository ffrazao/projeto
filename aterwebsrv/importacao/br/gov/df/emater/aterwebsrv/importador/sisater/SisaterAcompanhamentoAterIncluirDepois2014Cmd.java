package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.security.Principal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AssuntoDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.MetodoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Assunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeAssunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeChaveSisater;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadePessoa;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Metodo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFinalidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeFormato;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeNatureza;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePessoaParticipacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadePrioridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AtividadeSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service
public class SisaterAcompanhamentoAterIncluirDepois2014Cmd extends _Comando {

	private class CheckAtividadeList {

		private void acumulaAssunto(Assunto assunto) {
			if (atividade.getAssuntoList() == null) {
				atividade.setAssuntoList(new ArrayList<AtividadeAssunto>());
			}
			for (AtividadeAssunto item : atividade.getAssuntoList()) {
				if (assunto.getId().equals(item.getAssunto().getId())) {
					return;
				}
			}
			atividade.getAssuntoList().add(new AtividadeAssunto(assunto));
			return;
		}

		private void acumulaPessoaDemandante() throws Exception {
			if (atividade.getPessoaDemandanteList() == null) {
				atividade.setPessoaDemandanteList(new ArrayList<AtividadePessoa>());
			}
			demandantePs.setString(1, idUnd);
			demandantePs.setInt(2, idAtr);
			try (ResultSet rs = demandantePs.executeQuery()) {
				while (rs.next()) {
					Pessoa pessoa = pessoaDao.findOneByChaveSisater(impUtil.chaveBeneficiario(base, rs.getString("IDUND"), rs.getString("IDBEN")));
					if (pessoa == null) {
						continue;
					}
					for (AtividadePessoa item : atividade.getPessoaDemandanteList()) {
						if (pessoa.getId().equals(item.getPessoa().getId())) {
							return;
						}
					}
					atividade.getPessoaDemandanteList().add(new AtividadePessoa(null, pessoa, atividade.getInicio(), AtividadePessoaParticipacao.E, Confirmacao.S));
				}
			}
			return;
		}

		private void acumulaPessoaExecutor(Pessoa empregado) throws BoException {
			if (atividade.getPessoaExecutorList() == null) {
				atividade.setPessoaExecutorList(new ArrayList<AtividadePessoa>());
				atividade.getPessoaExecutorList().add(new AtividadePessoa(impUtil.deParaUnidadeOrganizacional(emater, base.getSigla()), null, atividade.getInicio(), AtividadePessoaParticipacao.E, Confirmacao.N));
			}
			for (AtividadePessoa item : atividade.getPessoaExecutorList()) {
				if (item.getPessoa() != null && empregado.getId().equals(item.getPessoa().getId())) {
					return;
				}
			}
			atividade.getPessoaExecutorList().add(new AtividadePessoa(null, empregado, atividade.getInicio(), AtividadePessoaParticipacao.E, Confirmacao.S));
			return;
		}

		private void acumulaChaveSisater(String chaveAtividade) {
			atividade.getChaveSisaterList().add(new AtividadeChaveSisater(chaveAtividade));
		}

		private String idUnd;

		private Integer idAtr;

		private Atividade atividade;

		private PreparedStatement demandantePs;

		void importar(DbSater base, Principal usuario) throws Exception {

			impUtil.criarMarcaTabelaSisater(con, TABELA);
			demandantePs = con.prepareStatement(String.format("SELECT IDUND, IDBEN FROM %s WHERE IDUND = ? AND IDATR = ?", TABELA_SUB));
			idUnd = null;
			idAtr = null;
			atividade = null;

			try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(String.format(SQL, TABELA));) {

				int cont = 0;

				while (rs.next()) {
					TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
					try {
						if (!(rs.getString("IDUND").equals(idUnd) && rs.getInt("IDATR") == idAtr)) {
							idUnd = rs.getString("IDUND");
							idAtr = rs.getInt("IDATR");
							salvar(usuario);
							atividade = novoAtividade(rs);
							acumulaPessoaDemandante();
						}

						acumulaAssunto(assuntoPega(rs.getString("ACAO")));
						acumulaPessoaExecutor(empregadoPega(rs.getString("IDEMP")));
						acumulaChaveSisater(impUtil.chaveAtividadeDepois2014(base, rs.getString("IDUND"), rs.getInt("IDATR"), rs.getDate("ATERDT"), rs.getString("IDEMP"), rs.getString("IDMET"), rs.getString("IDTEMA"), rs.getString("IDACAO"), TABELA));

						cont++;
						transactionManager.commit(transactionStatus);
					} catch (Exception e) {
						logger.error(e);
						e.printStackTrace();
						transactionManager.rollback(transactionStatus);
					}
				}
				TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
				try {
					salvar(usuario);
					cont++;
					transactionManager.commit(transactionStatus);
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
					transactionManager.rollback(transactionStatus);
				}

				if (logger.isDebugEnabled()) {
					logger.debug(String.format("[%s] importado %d atividades", base.name(), cont));
				}
			}
		}

		private void salvar(Principal usuario) throws Exception {
			if (atividade != null) {
				em.detach(atividade);

				// salvar no MySQL e no Firebird
				atividade.setId((Integer) facadeBo.atividadeSalvar(usuario, atividade).getResposta());

				int ini;
				String c;
				for (AtividadeChaveSisater chave : atividade.getChaveSisaterList()) {

					c = chave.getChaveSisater();

					ini = c.indexOf("IDUND") + 1 + "IDUND".length();
					String idUnd = c.substring(ini, c.indexOf(",", ini));
					ini = c.indexOf("IDATR") + 1 + "IDATR".length();
					String idAtr = c.substring(ini, c.indexOf(",", ini));
					ini = c.indexOf("ATERDT") + 1 + "ATERDT".length();
					String aterDt = c.substring(ini, c.indexOf(",", ini));
					ini = c.indexOf("IDEMP") + 1 + "IDEMP".length();
					String idEmp = c.substring(ini, c.indexOf(",", ini));
					ini = c.indexOf("IDMET") + 1 + "IDMET".length();
					String idMet = c.substring(ini, c.indexOf(",", ini));
					ini = c.indexOf("IDTEMA") + 1 + "IDTEMA".length();
					String idTema = c.substring(ini, c.indexOf(",", ini));
					ini = c.indexOf("IDACAO") + 1 + "IDACAO".length();
					String idAcao = c.substring(ini, c.indexOf("]", ini));

					impUtil.chaveAterWebAtualizar(con, atividade.getId(), agora, TABELA, "IDUND = ? AND IDATR = ? AND ATERDT = ? AND IDEMP = ? AND IDMET = ? AND IDTEMA = ? AND IDACAO = ?", idUnd, Integer.parseInt(idAtr),
							new java.sql.Date(((Calendar) UtilitarioData.getInstance().stringParaData(aterDt)).getTime().getTime()),
							// aterDt,
							idEmp, idMet, idTema, idAcao);
				}

			}
		}

		private Atividade novoAtividade(ResultSet rs) throws SQLException, BoException {
			Atividade result = new Atividade();

			// info basicas
			result.setInclusaoData(agora);
			result.setInclusaoUsuario(ematerUsuario);
			result.setAlteracaoData(agora);
			result.setAlteracaoUsuario(ematerUsuario);
			result.setFinalidade(AtividadeFinalidade.O);
			result.setFormato(AtividadeFormato.E);
			result.setNatureza(AtividadeNatureza.D);
			result.setPrioridade(AtividadePrioridade.N);
			result.setSituacao(AtividadeSituacao.C);
			result.setSituacaoData(agora);

			result.setMetodo(metodoPega(rs.getString("METODO"), rs.getString("IDMET")));
			result.setInicio(impUtil.captaData(rs.getDate("ATERDT")));
			result.setConclusao(impUtil.captaData(rs.getDate("ATERDT")));
			result.setPublicoEstimado(rs.getInt("PUBREAL"));
			result.setPublicoReal(rs.getInt("PUBREAL"));
			result.setDetalhamento(rs.getString("OBS"));

			return result;
		}
	}

	private static final String SQL;

	private static final String TABELA = "ATERNOVA00";

	private static final String TABELA_SUB = "ATERNOVA01";

	static {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT A.*").append("\n");
		sql.append("              , B.METODO").append("\n");
		sql.append("              , C.ACAO").append("\n");
		sql.append("FROM            ATERNOVA00 A").append("\n");
		sql.append("LEFT JOIN       METODO B").append("\n");
		sql.append("ON              B.IDMET = A.IDMET").append("\n");
		sql.append("LEFT JOIN       ACAO C").append("\n");
		sql.append("ON              C.IDACAO = A.IDACAO").append("\n");
		sql.append("ORDER BY        A.IDUND").append("\n");
		sql.append("              , A.IDATR").append("\n");
		sql.append("              , A.ATERDT").append("\n");
		sql.append("              , A.IDEMP").append("\n");
		sql.append("              , A.IDMET").append("\n");
		sql.append("              , A.IDTEMA").append("\n");
		sql.append("              , A.IDACAO").append("\n");
		SQL = sql.toString();
	}

	private Calendar agora;

	@Autowired
	private AssuntoDao assuntoDao;

	private List<Assunto> assuntoList = new ArrayList<Assunto>();

	private DbSater base;

	private CheckAtividadeList check = new CheckAtividadeList();

	private Connection con;

	@PersistenceContext
	private EntityManager em;

	private PessoaJuridica emater;

	private Usuario ematerUsuario;

	private FacadeBo facadeBo;

	private ImpUtil impUtil;

	@Autowired
	private MetodoDao metodoDao;

	@Autowired
	private EmpregoDao empregoDao;

	private List<Metodo> metodoList = new ArrayList<Metodo>();

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;

	Assunto assuntoPega(String nome) throws BoException {
		switch (nome) {
		case "Boas práticas agrícolas - BPA":
			nome = "Boas Práticas Agropecuárias (BPA)";
			break;
		case "Boas práticas de fabricação - BPF":
			nome = "Boas Práticas de Fabricação (BPF)";
			break;
		case "Classificação / Padronização / Embalagem":
			nome = "Classificação / padronização / embalagens";
			break;
		case "Clínica":
			nome = "Clínica animal";
			break;
		case "Georeferenciamento":
			nome = "Geoprocessamento, Georreferenciamento, mapas";
			break;
		}
		for (Assunto assunto : assuntoList) {
			if (assunto.getNome().equals("nome")) {
				return assunto;
			}
		}
		Assunto assunto = assuntoDao.findOneByNome(nome);
		if (assunto == null) {
			throw new BoException("Assunto não cadastrado [%d]", nome);
		}
		assuntoList.add(assunto);
		return assunto;
	}

	private PlatformTransactionManager transactionManager;
	private DefaultTransactionDefinition transactionDefinition;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		facadeBo = (FacadeBo) contexto.get("facadeBo");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");

		emater = (PessoaJuridica) contexto.get("emater");
		ematerUsuario = ((UserAuthentication) contexto.getUsuario()).getDetails();

		empregadoFuncao = (RelacionamentoFuncao) contexto.get("empregadoFuncao");

		agora = (Calendar) contexto.get("agora");

		transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

		check.importar(base, contexto.getUsuario());

		return false;
	}

	Metodo metodoPega(String nome, String idMet) throws BoException {
		for (Metodo metodo : metodoList) {
			if (metodo.getNome().equalsIgnoreCase(nome)) {
				return metodo;
			}
		}
		Metodo metodo = metodoDao.findOneByNome(nome);
		if (metodo == null) {
			metodo = metodoDao.findOneByNome(String.format("%s, Método não identificado", idMet));
			if (metodo == null) {
				throw new BoException("Método não cadastrado [%s]", nome);
			}
		}
		metodoList.add(metodo);
		return metodo;
	}

	private RelacionamentoFuncao empregadoFuncao;

	Pessoa empregadoPega(String matricula) throws BoException {
		matricula = matricula.trim().toUpperCase();
		matricula = UtilitarioString.substituirTudo(matricula, "-", "");
		matricula = UtilitarioString.substituirTudo(matricula, ".", "");
		matricula = UtilitarioString.zeroEsquerda(matricula, 8);

		List<Emprego> empregoList = empregoDao.findByMatricula(matricula);

		if (empregoList == null || empregoList.size() != 1) {
			throw new BoException("Empregado não identificado [%s]", matricula);
		}

		Pessoa pessoa = null;
		List<PessoaRelacionamento> pessoaRelacionamentoList = empregoList.get(0).getPessoaRelacionamentoList();
		if (pessoaRelacionamentoList == null) {
			pessoaRelacionamentoList = pessoaRelacionamentoDao.findByRelacionamento(empregoList.get(0));
		}
		for (PessoaRelacionamento pessoaRelacionamento : pessoaRelacionamentoList) {
			if (empregadoFuncao.getId().equals(pessoaRelacionamento.getRelacionamentoFuncao().getId())) {
				pessoa = pessoaRelacionamento.getPessoa();
				break;
			}
		}
		if (pessoa == null) {
			throw new BoException("Empregado não cadastrado [%s]", matricula);
		}
		return pessoa;
	}
}