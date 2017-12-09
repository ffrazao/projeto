package br.gov.df.emater.aterwebsrv.importador.empregado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.CargoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioData;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Cargo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;

@Service
public class EmpregadoDemitidosExcelImportarCmd extends _Comando {

	@Autowired
	private CargoDao cargoDao;

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private PessoaFisicaDao pessoaFisicaDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<Map<String, Object>> mapa = (List<Map<String, Object>>) contexto.get("DemitidoEmpregadoExcel");

		FacadeBo facadeBo = (FacadeBo) contexto.get("facadeBo");
		RelacionamentoFuncao empregadorFuncao = (RelacionamentoFuncao) contexto.get("empregadorFuncao");
		RelacionamentoTipo relacionamentoTipo = (RelacionamentoTipo) contexto.get("relacionamentoTipo");
		PessoaJuridica emater = (PessoaJuridica) contexto.get("emater");

		PlatformTransactionManager transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		DefaultTransactionDefinition transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

		int cont = 0;
		TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
		try {
			for (Map<String, Object> reg : mapa) {
				// identificar o empregador
				PessoaJuridica empregador = null;
				if (Arrays.asList(new String[] { "2 - NORMAL", "8 - CEDIDO", "5 - DESLIGADO" }).contains(reg.get("STATUS"))) {
					empregador = emater;
				} else {
					Pessoa sab = pessoaDao.findOneByApelidoSigla("SAB");
					if (sab == null) {
						sab = new PessoaJuridica();
						sab.setNome("Sociedade de Abastecimento de Brasília");
						sab.setApelidoSigla("SAB");
						sab.setPublicoAlvoConfirmacao(Confirmacao.N);
						sab.setId((Integer) facadeBo.pessoaSalvar(contexto.getUsuario(), sab).getResposta());
					}
					empregador = (PessoaJuridica) sab;
				}

				// identificar o cargo
				Cargo cargo = cargoDao.findOneByPessoaJuridicaAndNome(empregador, "Desconhecido");
				if (cargo == null) {
					cargo = new Cargo();
					cargo.setPessoaJuridica(empregador);
					cargo.setNome("Desconhecido");
					cargo = cargoDao.save(cargo);
				}

				// qualificar os dados
				String matricula = UtilitarioString.zeroEsquerda(reg.get("MATRICULA").toString().trim().toUpperCase(), 8);
				String nome = UtilitarioString.formataNomeProprio((String) reg.get("NOME"));
				Calendar admissao = new GregorianCalendar();
				try {
					admissao.setTime(DateUtil.getJavaDate((double) reg.get("ADMISSÃO")));
				} catch (ClassCastException e) {
					admissao = (Calendar) UtilitarioData.getInstance().stringParaData((String) reg.get("ADMISSÃO"));
				}
				Calendar demissao = null;
				Object desligamentoObj = reg.get("DESLIGAMENTO");
				if (desligamentoObj != null) {
					try {
						demissao = new GregorianCalendar();
						demissao.setTime(DateUtil.getJavaDate((double) desligamentoObj));
					} catch (ClassCastException e) {
						String temp = (String) desligamentoObj;
						if (!"99/99/9999".equals(temp)) {
							demissao = (Calendar) UtilitarioData.getInstance().stringParaData(temp);
						}
					}
				}
				String cpf = UtilitarioString.formataCpf((String) reg.get("CPF"));
				PessoaGenero genero = ((String) reg.get("DESC SEXO")).equals("M") ? PessoaGenero.M : PessoaGenero.F;
				Calendar nascimento = new GregorianCalendar();
				try {
					nascimento.setTime(DateUtil.getJavaDate((double) reg.get("DATA NASC.")));
				} catch (ClassCastException e) {
					nascimento = (Calendar) UtilitarioData.getInstance().stringParaData((String) reg.get("DATA NASC."));
				}

				List<PessoaFisica> empregadoList = pessoaFisicaDao.findByNomeIgnoreCaseAndGenero(nome, genero);
				PessoaFisica empregado = null;
				if (empregadoList == null || empregadoList.isEmpty()) {
					empregado = new PessoaFisica();
					empregado.setNome(nome);
					empregado.setApelidoSigla(nome.split("\\s")[0]);
				} else {
					empregado = empregadoList.get(0);
				}
				empregado.setPublicoAlvoConfirmacao(Confirmacao.N);
				empregado.setGenero(genero);
				empregado.setNascimento(nascimento);
				empregado.setCpf(cpf);

				// identificar o emprego
				if (empregado.getRelacionamentoList() == null || empregado.getRelacionamentoList().isEmpty()) {
					// criar o relacionamento de emprego
					Emprego emprego = new Emprego();
					emprego.setRelacionamentoTipo(relacionamentoTipo);
					emprego.setInicio(admissao);
					emprego.setTermino(demissao);
					emprego.setMatricula(matricula);
					emprego.setCargo(cargo);

					// adicionar o empregador no relacionamento
					PessoaRelacionamento pessoaRelacionamento = new PessoaRelacionamento();
					pessoaRelacionamento.setRelacionamento(emprego);
					pessoaRelacionamento.setRelacionamentoFuncao(empregadorFuncao);
					pessoaRelacionamento.setPessoa(empregador);

					List<PessoaRelacionamento> relacionamentoList = new ArrayList<PessoaRelacionamento>();
					relacionamentoList.add(pessoaRelacionamento);

					// inserir relacionamento na ficha do empregado
					empregado.setRelacionamentoList(relacionamentoList);
				}

				// salvar dados do empregado
				empregado.setId((Integer) facadeBo.pessoaSalvar(contexto.getUsuario(), empregado).getResposta());

				pessoaDao.flush();

				cont++;
			}

			transactionManager.commit(transactionStatus);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			transactionManager.rollback(transactionStatus);
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("DemitidoEmpregadoExcel importado %d empregados", cont));
		}

		return false;
	}

}