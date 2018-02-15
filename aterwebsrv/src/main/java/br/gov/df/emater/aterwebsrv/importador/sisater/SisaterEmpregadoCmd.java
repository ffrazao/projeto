package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo.util.ArquivoConstantes;
import br.gov.df.emater.aterwebsrv.dao.funcional.CargoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoViDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaRelacionamentoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;
import br.gov.df.emater.aterwebsrv.importador.apoio.ImpUtil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ArquivoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Cargo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.funcional.EmpregoVi;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Arquivo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service("SisaterEmpregadoCmd")
public class SisaterEmpregadoCmd extends _Comando {

	private static final String SQL = "SELECT * FROM %s WHERE EMNOME <> 'Toda a equipe' ORDER BY IDEMP";

	private static final String TABELA = "EMPREGADO";

	private Calendar agora;

	@Autowired
	private ArquivoDao arquivoDao;

	private DbSater base;

	@Autowired
	private CargoDao cargoDao;

	private Cargo cargoPadrao;

	private Connection con;

	private PessoaJuridica emater;

	private Usuario ematerUsuario;

	private RelacionamentoFuncao empregadoFuncao;

	private RelacionamentoFuncao empregadorFuncao;

	@Autowired
	private EmpregoDao empregoDao;

	@Autowired
	private EmpregoViDao empregoViDao;

	private ImpUtil impUtil;

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private PessoaRelacionamentoDao pessoaRelacionamentoDao;

	private RelacionamentoTipo relacionamentoTipo;

	private void captarPerfilArquivo(Pessoa pessoa, ResultSet rs) throws SQLException, IOException {
		Arquivo result = new Arquivo();

		if (pessoa.getPerfilArquivo() != null && pessoa.getPerfilArquivo().getConteudo() != null && pessoa.getPerfilArquivo().getConteudo().length > 0) {
			return;
		}
		result.setConteudo(ConexaoFirebird.lerCampoBlob(rs, "EMFOTO"));
		if (result.getConteudo() == null || result.getConteudo().length == 0) {
			return;
		}
		result.setMd5(Criptografia.MD5_FILE(result.getConteudo()));
		Arquivo salvo = arquivoDao.findByMd5(result.getMd5());
		if (salvo != null) {
			salvo.setConteudo(result.getConteudo());
			salvo = arquivoDao.save(salvo);
			pessoa.setPerfilArquivo(salvo);
			return;
		}
		result.setExtensao(".jpg");
		result.setDataUpload(agora);
		result.setMimeTipo("image/jpeg");
		result.setNomeOriginal(String.format("perfil-%s.jpg", rs.getString("IDEMP")));
		result.setTipo(ArquivoTipo.P);
		result.setTamanho(result.getConteudo().length);
		result.setLocalDiretorioWeb(ArquivoConstantes.DIRETORIO_PERFIL.concat(File.separator).concat(result.getMd5()));
		result = arquivoDao.save(result);
		pessoa.setPerfilArquivo(result);
		return;
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		con = (Connection) contexto.get("conexao");
		base = (DbSater) contexto.get("base");
		impUtil = (ImpUtil) contexto.get("impUtil");

		emater = (PessoaJuridica) contexto.get("emater");
		ematerUsuario = ((UserAuthentication) contexto.getUsuario()).getDetails();
		empregadorFuncao = (RelacionamentoFuncao) contexto.get("empregadorFuncao");
		empregadoFuncao = (RelacionamentoFuncao) contexto.get("empregadoFuncao");
		relacionamentoTipo = (RelacionamentoTipo) contexto.get("relacionamentoTipo");
		cargoPadrao = cargoDao.findOneByPessoaJuridicaAndNome(emater, "Assistente Administrativo");

		agora = (Calendar) contexto.get("agora");

		PlatformTransactionManager transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		DefaultTransactionDefinition transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");
/*
		impUtil.criarMarcaTabelaSisater(con, TABELA);
		int cont = 0;
		try (Statement st = con.createStatement(); ResultSet rs = st.executeQuery(String.format(SQL, TABELA));) {
			while (rs.next()) {
				TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
				try {

					String matricula = rs.getString("IDEMP").trim().toUpperCase();
					matricula = UtilitarioString.substituirTudo(matricula, "-", "");
					matricula = UtilitarioString.substituirTudo(matricula, ".", "");
					matricula = UtilitarioString.zeroEsquerda(matricula, 8);

					if (matricula == null || matricula.trim().length() == 0) {
						continue;
					}

					List<Emprego> empregoList = empregoDao.findByMatricula(matricula);
					
					if (CollectionUtils.isEmpty(empregoList)) {
						// tentar encontrar pelo nome
						List<EmpregoVi> empregoViList = empregoViDao.findAllByEmpregadoNome(rs.getString("EMNOME"));
						if (empregoViList != null && empregoViList.size() == 1) {
							empregoList = empregoDao.findByMatricula(empregoViList.get(0).getMatricula());
						}
					}
					
					if (empregoList != null && empregoList.size() == 1) {
						List<PessoaRelacionamento> pessoaRelacionamentoList = empregoList.get(0).getPessoaRelacionamentoList();
						if (pessoaRelacionamentoList == null) {
							pessoaRelacionamentoList = pessoaRelacionamentoDao.findByRelacionamento(empregoList.get(0));
						}
						for (PessoaRelacionamento pessoaRelacionamento : pessoaRelacionamentoList) {
							if (empregadoFuncao.getId().equals(pessoaRelacionamento.getRelacionamentoFuncao().getId())) {
								Pessoa empregado = pessoaRelacionamento.getPessoa();
								empregado.setChaveSisater(impUtil.chaveEmpregado(base, rs.getString("IDEMP")));
								if (empregado.getPerfilArquivo() == null && rs.getObject("EMFOTO") != null) {
									captarPerfilArquivo(empregado, rs);
									pessoaDao.save(empregado);
								}
								break;
							}
						}
					} else if (empregoList != null && empregoList.size() == 0) {
						// salvar o empregado
						PessoaFisica empregado = (PessoaFisica) pessoaDao.findOneByChaveSisater(impUtil.chaveEmpregado(base, rs.getString("IDEMP")));
						if (empregado == null) {
							empregado = new PessoaFisica();
						}
						empregado.setChaveSisater(impUtil.chaveEmpregado(base, rs.getString("IDEMP")));
						empregado.setNome(rs.getString("EMNOME"));
						empregado.setApelidoSigla(rs.getString("EMAPELIDO"));
						empregado.setGenero(PessoaGenero.M);
						empregado.setInclusaoUsuario(ematerUsuario);
						empregado.setInclusaoData(agora);
						empregado.setAlteracaoUsuario(ematerUsuario);
						empregado.setAlteracaoData(agora);
						empregado.setSituacao(PessoaSituacao.A);
						empregado.setSituacaoData(agora);
						empregado.setPublicoAlvoConfirmacao(Confirmacao.N);
						captarPerfilArquivo(empregado, rs);
						empregado = pessoaDao.save(empregado);

						// salvar a relação de emprego
						Emprego emprego = new Emprego();
						emprego.setInicio(impUtil.captaData(rs.getDate("EMDATAADM")));
						if (emprego.getInicio() == null) {
							emprego.setInicio(agora);
						}
						emprego.setRelacionamentoTipo(relacionamentoTipo);
						emprego.setCargo(cargoPadrao);
						emprego = empregoDao.saveAndFlush(emprego);

						PessoaRelacionamento prEmpregador = new PessoaRelacionamento();
						prEmpregador.setRelacionamento(emprego);
						prEmpregador.setRelacionamentoFuncao(empregadorFuncao);
						prEmpregador.setPessoa(emater);
						prEmpregador = pessoaRelacionamentoDao.save(prEmpregador);

						PessoaRelacionamento prEmpregado = new PessoaRelacionamento();
						prEmpregado.setRelacionamento(emprego);
						prEmpregado.setRelacionamentoFuncao(empregadorFuncao);
						prEmpregado.setPessoa(empregado);
						prEmpregado = pessoaRelacionamentoDao.save(prEmpregado);

						List<PessoaRelacionamento> pessoaRelacionamentoList = new ArrayList<PessoaRelacionamento>();
						pessoaRelacionamentoList.add(prEmpregador);
						pessoaRelacionamentoList.add(prEmpregador);
						emprego.setPessoaRelacionamentoList(pessoaRelacionamentoList);
						emprego = empregoDao.saveAndFlush(emprego);
					} else {
						throw new BoException("Matricula cadastrada mais de uma vez");
					}
					cont++;
					transactionManager.commit(transactionStatus);
				} catch (Exception e) {
					logger.error(e);
					e.printStackTrace();
					transactionManager.rollback(transactionStatus);
				}
			}
		}
		if (logger.isDebugEnabled()) {
			logger.debug(String.format("[%s] importado %d empregados", base.name(), cont));
		}
*/	

		return false;
	}

}