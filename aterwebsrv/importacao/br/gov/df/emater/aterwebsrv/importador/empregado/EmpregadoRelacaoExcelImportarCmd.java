package br.gov.df.emater.aterwebsrv.importador.empregado;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;

import org.apache.commons.collections.CollectionUtils;
import org.apache.poi.ss.usermodel.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import org.springframework.util.StringUtils;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.funcional.CargoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.LotacaoDao;
import br.gov.df.emater.aterwebsrv.dao.funcional.UnidadeOrganizacionalDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.GrupoSocialTipoDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaFisicaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaJuridicaDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Escolaridade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaGenero;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Cargo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Emprego;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Lotacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.GrupoSocialTipo;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaFisica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaRelacionamento;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoFuncao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.RelacionamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service
public class EmpregadoRelacaoExcelImportarCmd extends _Comando {

	@Autowired
	private GrupoSocialTipoDao grupoSocialTipoDao;

	@Autowired
	private CargoDao cargoDao;

	@Autowired
	private EmpregoDao empregoDao;

	@Autowired
	private LotacaoDao lotacaoDao;

	@Autowired
	private PessoaDao pessoaDao;

	@Autowired
	private PessoaJuridicaDao pessoaJuridicaDao;

	@Autowired
	private PessoaFisicaDao pessoaFisicaDao;

	@Autowired
	private UnidadeOrganizacionalDao unidadeOrganizacionalDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<Map<String, Object>> mapa = (List<Map<String, Object>>) contexto.get("RelacaoEmpregadosExcel");

		EntityManager em = (EntityManager) contexto.get("em");
		FacadeBo facadeBo = (FacadeBo) contexto.get("facadeBo");
		RelacionamentoFuncao empregadorFuncao = (RelacionamentoFuncao) contexto.get("empregadorFuncao");
		RelacionamentoTipo relacionamentoTipo = (RelacionamentoTipo) contexto.get("relacionamentoTipo");
		Calendar agora = (Calendar) contexto.get("agora");
		PessoaJuridica emater = (PessoaJuridica) contexto.get("emater");

		GrupoSocialTipo uoGrupoSocialTipo = grupoSocialTipoDao.findOneByCodigo(GrupoSocialTipo.Codigo.UNIDADE_ORGANIZACIONAL);

		PlatformTransactionManager transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		DefaultTransactionDefinition transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

		PessoaJuridica sab = null;

		int cont = 0;
		TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
		try {
			for (Map<String, Object> reg : mapa) {
				// identificar o empregador
				PessoaJuridica empregador = null;
				if (Arrays.asList(new String[] { "2 - NORMAL", "8 - CEDIDO" }).contains(reg.get("STATUS"))) {
					empregador = emater;
				} else {
					if (sab == null) {
						sab = pessoaJuridicaDao.findOneByNome("Sociedade de Abastecimento de Brasília S/A");
						if (sab == null) {
							sab = new PessoaJuridica();
							sab.setNome("Sociedade de Abastecimento de Brasília S/A");
							sab.setApelidoSigla("SAB");
							sab.setPublicoAlvoConfirmacao(Confirmacao.N);
							sab.setCnpj("00.037.226/0001-67");
							sab.setId((Integer) facadeBo.pessoaSalvar(contexto.getUsuario(), sab).getResposta());
						}
					}
					empregador = sab;
				}

				// identificar o cargo
				String cargoExcel = UtilitarioString.formataNomeProprio((String) reg.get("CARGO"));
				String especialidade = UtilitarioString.formataNomeProprio((String) reg.get("ESPECIALIDADE"));
				StringBuilder nomeCargo = new StringBuilder(cargoExcel);
				if (!StringUtils.isEmpty(especialidade) && !cargoExcel.equalsIgnoreCase(especialidade)) {
					nomeCargo.append(", ").append(especialidade);
				}
				Cargo cargo = cargoDao.findOneByPessoaJuridicaAndNome(empregador, nomeCargo.toString());
				if (cargo == null) {
					cargo = new Cargo();
					cargo.setPessoaJuridica(empregador);
					cargo.setNome(nomeCargo.toString());
					cargo.setEfetivo(Arrays.asList(new String[] { "COMISSIONADO", "JOVEM APRENDIZ" }).contains(cargoExcel) ? Confirmacao.N : Confirmacao.S);
					if (cargoExcel.toUpperCase().endsWith("-NM") || cargoExcel.toUpperCase().endsWith("TECNICO DE INFORMATICA") || cargoExcel.toUpperCase().indexOf("ADMINISTRATIVO") >= 0) {
						cargo.setEscolaridade(Escolaridade.MC);
					} else if (cargoExcel.toUpperCase().endsWith("-NS") || cargoExcel.toUpperCase().endsWith("TECNICO ESPECIALIZADO") || cargoExcel.toUpperCase().endsWith("MOTORISTA")) {
						cargo.setEscolaridade(Escolaridade.SC);
					} else if (cargoExcel.toUpperCase().equals("MECANICO AUTOMOTIVO") || cargoExcel.toUpperCase().equals("ELETRICISTA") || cargoExcel.toUpperCase().equals("DESENHISTA") || cargoExcel.toUpperCase().equals("DIGITADOR")) {
						cargo.setEscolaridade(Escolaridade.FC);
					}
					cargo = cargoDao.save(cargo);
				}

				// identificar a unidade organizacional
				String lotacaoExcel = UtilitarioString.formataNomeProprio((String) reg.get("LOTAÇÃO"));
				String[] lotacaoPartes = lotacaoExcel.split("\\-");
				String lotacaoSigla = lotacaoPartes[0].toUpperCase().trim();
				String lotacaoNome = lotacaoPartes[1].trim();
				if (lotacaoSigla.equals("CEDIDOS")) {
					lotacaoNome = lotacaoNome + ", " + lotacaoSigla;
				}
				UnidadeOrganizacional unidadeOrganizacional = unidadeOrganizacionalDao.findOneByPessoaJuridicaAndApelidoSigla(emater, lotacaoSigla);
				if (unidadeOrganizacional == null) {
					unidadeOrganizacional = unidadeOrganizacionalDao.findOneByPessoaJuridicaAndNome(emater, lotacaoNome);
					if (unidadeOrganizacional == null) {
						unidadeOrganizacional = new UnidadeOrganizacional();
						unidadeOrganizacional.setPessoaJuridica(emater);
						unidadeOrganizacional.setNome(lotacaoNome);
						unidadeOrganizacional.setApelidoSigla(lotacaoSigla);
						unidadeOrganizacional.setClassificacao(UnidadeOrganizacionalClassificacao.AD);
						unidadeOrganizacional.setSituacao(PessoaSituacao.A);
						unidadeOrganizacional.setInicio(agora);
						unidadeOrganizacional.setSituacaoData(agora);
						unidadeOrganizacional.setGrupoSocialTipo(uoGrupoSocialTipo);
						unidadeOrganizacional.setInclusaoUsuario((Usuario) ((UserAuthentication) contexto.getUsuario()).getDetails());
						unidadeOrganizacional.setAlteracaoUsuario((Usuario) ((UserAuthentication) contexto.getUsuario()).getDetails());
						unidadeOrganizacional = unidadeOrganizacionalDao.save(unidadeOrganizacional);
					}
				}

				// identificar a pessoa
				String nomeExcel = UtilitarioString.formataNomeProprio((String) reg.get("NOME"));
				String[] nascimentoExcel = ((String) reg.get("DIA/MÊS NASC.")).split("/");
				Calendar nascimento = new GregorianCalendar(1950, Integer.parseInt(nascimentoExcel[1]) - 1, Integer.parseInt(nascimentoExcel[0]));
				PessoaGenero genero = ((String) reg.get("DESC. SEXO")).equals("M") ? PessoaGenero.M : PessoaGenero.F;
				Calendar admissao = new GregorianCalendar();
				admissao.setTime(DateUtil.getJavaDate((double) reg.get("DATA ADMISSÃO")));

				List<PessoaFisica> empregadoList = pessoaFisicaDao.findByNomeIgnoreCaseAndGenero(nomeExcel, genero);
				PessoaFisica empregado = null;
				if (CollectionUtils.isEmpty(empregadoList)) {
					// identificar o emprego
					Emprego emprego = new Emprego();
					emprego.setRelacionamentoTipo(relacionamentoTipo);
					emprego.setInicio(admissao);
					emprego.setMatricula(UtilitarioString.zeroEsquerda(reg.get("MATRICULA").toString().trim().toUpperCase(), 8));
					emprego.setCargo(cargo);
					
					PessoaRelacionamento pessoaRelacionamento = new PessoaRelacionamento();
					pessoaRelacionamento.setRelacionamento(emprego);
					pessoaRelacionamento.setPessoa(empregador);
					pessoaRelacionamento.setRelacionamentoFuncao(empregadorFuncao);
					
					List<PessoaRelacionamento> relacionamentoList = new ArrayList<PessoaRelacionamento>();
					relacionamentoList.add(pessoaRelacionamento);
					
					empregado = new PessoaFisica();
					empregado.setNome(nomeExcel);
					empregado.setApelidoSigla(nomeExcel.split("\\s")[0]);
					empregado.setGenero(genero);
					empregado.setPublicoAlvoConfirmacao(Confirmacao.N);
					empregado.setNascimento(nascimento);
					empregado.setRelacionamentoList(relacionamentoList);
					
					empregado.setId((Integer) facadeBo.pessoaSalvar(contexto.getUsuario(), empregado).getResposta());
					
					em.detach(empregado);
					empregado = pessoaFisicaDao.findOne(empregado.getId());
					
				} else if (empregadoList.size() > 1) {
					throw new BoException("Empregados Homônimos!!! [%s]", nomeExcel);
				} else {
					empregado = empregadoList.get(0);
				}

				Emprego emprego = null;
				List<Emprego> empregoList = empregoDao.findByPessoaRelacionamentoListPessoaIn(empregado);
				for (Emprego e : empregoList) {
					if (e.getInicio().before(agora) && e.getTermino() == null || e.getTermino().after(agora)) {
						if (emprego == null) {
							emprego = e;
						} else {
							throw new BoException("Empregado com mais de um emprego ativo [%s]", nomeExcel);
						}
					}
				}
				if (emprego == null) {
					throw new BoException("Não foi possível identificar o emprego da pessoa [%s]", nomeExcel);
				}

				// identificar a lotacao
				Lotacao lotacao = null;
				String funcao = (String) reg.get("FUNÇÃO");
				List<Lotacao> lotacaoList = lotacaoDao.findAllByEmprego(emprego);
				if (CollectionUtils.isEmpty(lotacaoList)) {
					lotacao = novaLotacao(emprego, unidadeOrganizacional, emprego.getInicio(), funcao);
				} else {
					for (Lotacao l : lotacaoList) {
						if (l.getInicio().before(agora) && (l.getTermino() == null || l.getTermino().after(agora))) {
							if (!l.getUnidadeOrganizacional().getId().equals(unidadeOrganizacional.getId())) {
								if (lotacao != null) {
									throw new BoException("Empregado com mais de uma lotacao ativa");
								}
								l.setTermino(agora);
								lotacaoDao.save(l);
								lotacao = novaLotacao(emprego, unidadeOrganizacional, agora, funcao);
							} else {
								if (lotacao != null) {
									throw new BoException("Empregado com mais de uma lotacao ativa");
								}
								lotacao = l;
							}
						}
					}
					if (lotacao == null) {
						lotacao = novaLotacao(emprego, unidadeOrganizacional, emprego.getInicio(), funcao);
					}
				}

				if (lotacao == null) {
					throw new BoException("Não foi possível identificar a lotação do empregado");
				}
				pessoaDao.flush();

				cont++;
			}
			transactionManager.commit(transactionStatus);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			transactionManager.rollback(transactionStatus);
			throw e;
		}

		if (logger.isDebugEnabled()) {
			logger.debug(String.format("RelacaoEmpregadosExcel importado %d empregados", cont));
		}

		return false;
	}

	private Lotacao novaLotacao(Emprego emprego, UnidadeOrganizacional unidadeOrganizacional, Calendar inicio, String funcao) {
		Lotacao result = new Lotacao();
		result.setEmprego(emprego);
		result.setUnidadeOrganizacional(unidadeOrganizacional);
		result.setInicio(inicio);
		if (Arrays.asList(new String[] { "CHEFE DA ASSESSORIA JURIDICA", "CHEFE DA COMUNICACAO SOCIAL", "CHEFE DE GABINETE", "COORDENADOR", "DIRETOR", "GERENTE", "GERENTE DE ALEX. GUSMAO", "GERENTE DE BRAZLANDIA", "GERENTE DE CEILANDIA", "GERENTE DE PLANALTINA",
				"GERENTE DE RIO PRETO", "GERENTE DE SAO SEBASTIAO", "GERENTE DE SOBRADINHO", "GERENTE DE TABATINGA", "GERENTE DE TAQUARA", "GERENTE DE VARGEM BONITA", "GERENTE DO GAMA", "GERENTE DO JARDIM", "GERENTE DO PAD/DF", "GERENTE DO PARANOA", "GERENTE DO PIPIRIPAU",
				"PRESIDENTE", "SUPERVISOR REGIONAL" }).contains(funcao)) {
			result.setGestor(Confirmacao.S);
		} else {
			result.setGestor(Confirmacao.N);
		}
		result.setTemporario(Confirmacao.N);
		return lotacaoDao.save(result);
	}

}