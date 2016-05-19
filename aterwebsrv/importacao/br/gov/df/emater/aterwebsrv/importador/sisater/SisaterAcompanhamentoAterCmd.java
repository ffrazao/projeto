package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.security.Principal;
import java.sql.Connection;
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
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.MetodoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Assunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeAssunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadePessoa;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Metodo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service
public class SisaterAcompanhamentoAterCmd extends _Comando {

	@Autowired
	private AtividadeDao atividadeDao;
	
	private List<Metodo> metodoList = new ArrayList<Metodo>();
	
	@Autowired
	private MetodoDao metodoDao;
	
	Metodo metodoPega(String nome) throws BoException {
		for (Metodo metodo: metodoList) {
			if (metodo.getNome().equals(nome)) {
				return metodo;
			}
		}
		Metodo metodo = metodoDao.findOneByNome(nome);
		if (metodo == null) {
			throw new BoException("Método não cadastrado [%s]", nome);
		}
		metodoList.add(metodo);
		return metodo;
	}

	private List<Assunto> assuntoList = new ArrayList<Assunto>();
	
	@Autowired
	private AssuntoDao assuntoDao;
	
	Assunto assuntoPega(String nome) throws BoException {
		for (Assunto assunto: assuntoList) {
			if (assunto.getNome().equals(nome)) {
				return assunto;
			}
		}
		Assunto assunto = assuntoDao.findOneByNome(nome);
		if (assunto == null) {
			throw new BoException("Método não cadastrado [%s]", nome);
		}
		assuntoList.add(assunto);
		return assunto;
	}

	private List<Pessoa> pessoaList = new ArrayList<Pessoa>();
	
	@Autowired
	private PessoaDao pessoaDao;
	
	Pessoa pessoaPega(String nome) throws BoException {
		for (Pessoa pessoa: pessoaList) {
			if (pessoa.getNome().equals(nome)) {
				return pessoa;
			}
		}
		Pessoa pessoa = pessoaDao.findByNome(nome);
		if (pessoa == null) {
			throw new BoException("Método não cadastrado [%s]", nome);
		}
		pessoaList.add(pessoa);
		return pessoa;
	}


	private class CheckAtividadeList {

		void importar(DbSater base, Principal usuario) throws Exception {

			impUtil.criarMarcaTabela(con, TABELA);

			try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(String.format(SQL, TABELA));) {
				int cont = 0;
				while (rs.next()) {
					Atividade atividade = null;
					try {
						atividade = novoAtividade(rs);
					} catch (BoException e) {
						throw e;
					}
					cont++;

					// FIXME quebra-galho para teste. remover este código
					if (cont > 50) {
						break;
					}

					em.detach(atividade);

					// salvar no MySQL e no Firebird
					atividade.setId((Integer) facadeBo.atividadeSalvar(usuario, atividade).getResposta());

					impUtil.chaveAterWebAtualizar(con, atividade.getId(), TABELA, "IDUND = ? AND IDIPA = ? AND SAFRA = ?", rs.getString("IDUND"), rs.getInt("IDIPA"), rs.getString("SAFRA"));
				}
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("[%s] importado %d atividades", base.name(), cont));
				}
			}
		}

		void init() {
		}

		private Atividade novoAtividade(ResultSet rs) throws SQLException, BoException {
			Atividade result = new Atividade();

			// recuperar os IDs
			Atividade salvo = null;
			// salvo =
			// atividadeDao.findOneByAnoAndBemAndUnidadeOrganizacionalAndPublicoAlvoIsNullAndPropriedadeRuralIsNull(result.getAno(),
			// result.getBem(), result.getUnidadeOrganizacional());
			// if (salvo == null) {
			// salvo =
			// atividadeDao.findOneByChaveSisater(result.getChaveSisater());
			// }
			// if (salvo != null) {
			// result.setId(salvo.getId());
			// }

			if (salvo != null) {
				em.detach(salvo);
			}
			result.setId(rs.getInt(""));
			
			// info basicas
			result.setInclusaoData(agora);
			result.setInclusaoUsuario(new Usuario(ematerUsuario.getId()));
			result.setAlteracaoData(agora);
			result.setAlteracaoUsuario(new Usuario(ematerUsuario.getId()));
			
			result.setMetodo(metodoPega(rs.getString("IDMET")));
			result.setAssuntoList(acumulaAssunto(result, assuntoPega(rs.getString("IDTEMA IDACAO"))));
			result.setInicio(impUtil.captaData(rs.getDate("ATERDT")));
			result.setConclusao(impUtil.captaData(rs.getDate("ATERDT")));
			result.setPessoaExecutorList(acumulaPessoaExecutor(result, pessoaPega(rs.getString("IDEMP IDUND"))));
			result.setPessoaDemandanteList(acumulaPessoaDemandante(result));
			result.setPublicoEstimado(rs.getInt("PUBREAL"));
			result.setPublicoReal(rs.getInt("PUBREAL"));
			result.setDetalhamento(rs.getString("OBS"));

			adicionaChaveSisater(result, impUtil.chaveAtividade(base, rs.getString("IDUND"), rs.getInt("IDATR"), rs.getDate("ATERDT"), rs.getString("IDEMP"), rs.getString("IDMET"), rs.getString("IDTEMA"), rs.getString("IDACAO"), TABELA));

			return result;
		}

		private void adicionaChaveSisater(Atividade result, Object chaveAtividade) {
			// TODO Auto-generated method stub
			
		}

		private List<AtividadePessoa> acumulaPessoaDemandante(Atividade result) {
			// TODO Auto-generated method stub
			return null;
		}

		private List<AtividadePessoa> acumulaPessoaExecutor(Atividade result, Pessoa pessoaPega) {
			// TODO Auto-generated method stub
			return null;
		}

		private List<AtividadeAssunto> acumulaAssunto(Atividade result, Assunto assuntoPega) {
			// TODO Auto-generated method stub
			return null;
		}
	}

	private static final String SQL = "SELECT * FROM %s";

	private static final String TABELA = "ATERNOVA00";

	private static final String TABELA_SUB = "ATERNOVA01";

	private Calendar agora;

	private DbSater base;

	private CheckAtividadeList checkProducaoList = new CheckAtividadeList();

	private Connection con;

	@PersistenceContext
	private EntityManager em;

	private PessoaJuridica emater;

	private Usuario ematerUsuario;

	private FacadeBo facadeBo;

	private ImpUtil impUtil;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		facadeBo = (FacadeBo) contexto.get("facadeBo");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");

		emater = (PessoaJuridica) contexto.get("emater");
		ematerUsuario = (Usuario) contexto.get("ematerUsuario");

		agora = (Calendar) contexto.get("agora");

		checkProducaoList.init();

		checkProducaoList.importar(base, contexto.getUsuario());

		return false;
	}

}