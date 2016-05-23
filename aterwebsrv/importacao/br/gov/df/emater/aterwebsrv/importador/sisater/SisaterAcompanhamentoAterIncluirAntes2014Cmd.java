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

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AssuntoDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.MetodoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
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

@Service
public class SisaterAcompanhamentoAterIncluirAntes2014Cmd extends _Comando {

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
					try {
						if (!(rs.getString("IDUND").equals(idUnd) && rs.getInt("IDATR") == idAtr)) {
							idUnd = rs.getString("IDUND");
							idAtr = rs.getInt("IDATR");
							salvar(usuario);
							atividade = novoAtividade(rs);
							acumulaPessoaDemandante();
						}

						acumulaAssunto(assuntoPega(rs.getString("ATIVIDADE")));
						acumulaPessoaExecutor(empregadoPega(rs.getString("IDEMP")));
						acumulaChaveSisater(impUtil.chaveAtividadeAntes2014(base, rs.getString("IDUND"), rs.getInt("IDATR"), TABELA));

						cont++;

					} catch (Exception e) {
						logger.error(e);
					}
				}
				salvar(usuario);

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
					String idAtr = c.substring(ini, c.indexOf("]", ini));

					impUtil.chaveAterWebAtualizar(con, atividade.getId(), TABELA, "IDUND = ? AND IDATR = ?", idUnd, Integer.parseInt(idAtr));
				}

			}
		}

		private Atividade novoAtividade(ResultSet rs) throws SQLException, BoException {
			Atividade result = new Atividade();

			// info basicas
			result.setInclusaoData(agora);
			result.setInclusaoUsuario(new Usuario(ematerUsuario.getId()));
			result.setAlteracaoData(agora);
			result.setAlteracaoUsuario(new Usuario(ematerUsuario.getId()));
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
			result.setDetalhamento(rs.getString("ATEROBS"));

			return result;
		}
	}

	private static final String SQL;

	private static final String TABELA = "ATER00";

	private static final String TABELA_SUB = "ATER01";

	static {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT DISTINCT A.*").append("\n");
		sql.append("              , B.METODO").append("\n");
		sql.append("              , C.ATIVIDADE").append("\n");
		sql.append("FROM            ATER00 A").append("\n");
		sql.append("LEFT JOIN       METODO B").append("\n");
		sql.append("ON              B.IDMET = A.IDMET").append("\n");
		sql.append("LEFT JOIN       ATIVIDADE C").append("\n");
		sql.append("ON              C.IDATV = A.IDATV").append("\n");
		sql.append("ORDER BY        A.IDUND").append("\n");
		sql.append("              , A.IDATR").append("\n");
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

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		facadeBo = (FacadeBo) contexto.get("facadeBo");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");

		emater = (PessoaJuridica) contexto.get("emater");
		ematerUsuario = (Usuario) contexto.get("ematerUsuario");

		empregadoFuncao = (RelacionamentoFuncao) contexto.get("empregadoFuncao");

		agora = (Calendar) contexto.get("agora");

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
		for (PessoaRelacionamento pessoaRelacionamento : empregoList.get(0).getPessoaRelacionamentoList()) {
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