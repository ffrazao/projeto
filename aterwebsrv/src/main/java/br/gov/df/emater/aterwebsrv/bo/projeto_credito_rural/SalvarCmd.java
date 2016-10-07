package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.pessoa.ArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.CronogramaPagamentoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.FluxoCaixaAnoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralArquivoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralCronogramaPagamentoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralFinanciamentoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralFluxoCaixaDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralGarantiaDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralHistoricoReceitaDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralParecerTecnicoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralPublicoAlvoPropriedadeRuralDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesaDao;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Atividade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FinanciamentoTipo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaTipo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralArquivo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralCronogramaPagamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFinanciamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFluxoCaixa;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralGarantia;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralHistoricoReceita;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralParecerTecnico;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralPublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralReceitaDespesa;

@Service("ProjetoCreditoRuralSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private CronogramaPagamentoDao cronogramaPagamentoDao;

	@Autowired
	private ProjetoCreditoRuralDao dao;

	@Autowired
	private FluxoCaixaAnoDao fluxoCaixaAnoDao;

	@Autowired
	private ProjetoCreditoRuralCronogramaPagamentoDao projetoCreditoRuralCronogramaPagamentoDao;

	@Autowired
	private ProjetoCreditoRuralFinanciamentoDao projetoCreditoRuralFinanciamentoDao;

	@Autowired
	private ProjetoCreditoRuralFluxoCaixaDao projetoCreditoRuralFluxoCaixaDao;

	@Autowired
	private ProjetoCreditoRuralGarantiaDao projetoCreditoRuralGarantiaDao;

	@Autowired
	private ProjetoCreditoRuralHistoricoReceitaDao projetoCreditoRuralHistoricoReceitaDao;

	@Autowired
	private ProjetoCreditoRuralParecerTecnicoDao projetoCreditoRuralParecerTecnicoDao;

	@Autowired
	private ProjetoCreditoRuralPublicoAlvoPropriedadeRuralDao projetoCreditoRuralPublicoAlvoPropriedadeRuralDao;

	@Autowired
	private ProjetoCreditoRuralReceitaDespesaDao projetoCreditoRuralReceitaDespesaDao;

	public SalvarCmd() {
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Atividade atividade = (Atividade) contexto.getRequisicao();

		ProjetoCreditoRural result = atividade.getProjetoCreditoRural();
		if (result == null) {
			return false;
		}
		
		if (result.getExcluidoMap() == null) {
			result.setExcluidoMap(atividade.getExcluidoMap());
		}
		Map<String, Set<Serializable>> excluidoMap = result.getExcluidoMap();

		// atualizar o campo de ligação
		result.setAtividade(atividade);

		// limpar os id das tabelas dependentes
		List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoCusteioList = null;
		List<ProjetoCreditoRuralCronogramaPagamento> cronogramaPagamentoInvestimentoList = null;
		List<ProjetoCreditoRuralFinanciamento> custeioList = null;
		List<ProjetoCreditoRuralFinanciamento> investimentoList = null;
		List<ProjetoCreditoRuralFluxoCaixa> fluxoCaixaList = null;
		List<ProjetoCreditoRuralGarantia> garantiaList = null;
		List<ProjetoCreditoRuralHistoricoReceita> trienioList = null;
		List<ProjetoCreditoRuralParecerTecnico> parecerTecnicoList = null;
		List<ProjetoCreditoRuralPublicoAlvoPropriedadeRural> publicoAlvoPropriedadeRuralList = null;
		List<ProjetoCreditoRuralReceitaDespesa> despesaList = null;
		List<ProjetoCreditoRuralReceitaDespesa> receitaList = null;

		List<ProjetoCreditoRuralArquivo> arquivoList = null;

		if (!CollectionUtils.isEmpty(result.getCronogramaPagamentoCusteioList())) {
			cronogramaPagamentoCusteioList = (List<ProjetoCreditoRuralCronogramaPagamento>) limparChavePrimaria(result.getCronogramaPagamentoCusteioList());
			result.getCronogramaPagamentoCusteioList().forEach((c) -> {
				limparChavePrimaria(c.getCronogramaPagamentoList());
			});
		}
		if (!CollectionUtils.isEmpty(result.getCronogramaPagamentoInvestimentoList())) {
			cronogramaPagamentoInvestimentoList = (List<ProjetoCreditoRuralCronogramaPagamento>) limparChavePrimaria(result.getCronogramaPagamentoInvestimentoList());
			result.getCronogramaPagamentoInvestimentoList().forEach((c) -> {
				limparChavePrimaria(c.getCronogramaPagamentoList());
			});
		}
		custeioList = (List<ProjetoCreditoRuralFinanciamento>) limparChavePrimaria(result.getCusteioList());
		investimentoList = (List<ProjetoCreditoRuralFinanciamento>) limparChavePrimaria(result.getInvestimentoList());
		if (!CollectionUtils.isEmpty(result.getFluxoCaixaList())) {
			fluxoCaixaList = (List<ProjetoCreditoRuralFluxoCaixa>) limparChavePrimaria(result.getFluxoCaixaList());
			fluxoCaixaList.forEach((c) -> {
				limparChavePrimaria(c.getFluxoCaixaAnoList());
			});
		}
		garantiaList = (List<ProjetoCreditoRuralGarantia>) limparChavePrimaria(result.getGarantiaList());
		trienioList = (List<ProjetoCreditoRuralHistoricoReceita>) limparChavePrimaria(result.getTrienioList());
		parecerTecnicoList = (List<ProjetoCreditoRuralParecerTecnico>) limparChavePrimaria(result.getParecerTecnicoList());
		publicoAlvoPropriedadeRuralList = (List<ProjetoCreditoRuralPublicoAlvoPropriedadeRural>) limparChavePrimaria(result.getPublicoAlvoPropriedadeRuralList());
		despesaList = (List<ProjetoCreditoRuralReceitaDespesa>) limparChavePrimaria(result.getDespesaList());
		receitaList = (List<ProjetoCreditoRuralReceitaDespesa>) limparChavePrimaria(result.getReceitaList());
		arquivoList = (List<ProjetoCreditoRuralArquivo>) limparChavePrimaria(result.getArquivoList());

		// salvar a tabela principal
		result = dao.save(result);
		
		result.setExcluidoMap(excluidoMap);

		if (!CollectionUtils.isEmpty(cronogramaPagamentoCusteioList)) {
			cronogramaPagamentoCusteioList.forEach((custeio) -> custeio.setTipo(FinanciamentoTipo.C));
		}
		salvarTabelaDependente(result, "cronogramaPagamentoCusteioList", projetoCreditoRuralCronogramaPagamentoDao, ProjetoCreditoRuralCronogramaPagamento.class, cronogramaPagamentoCusteioList);
		salvarPagamentos(cronogramaPagamentoCusteioList);

		if (!CollectionUtils.isEmpty(cronogramaPagamentoInvestimentoList)) {
			cronogramaPagamentoInvestimentoList.forEach((investimento) -> investimento.setTipo(FinanciamentoTipo.I));
		}
		salvarTabelaDependente(result, "cronogramaPagamentoInvestimentoList", projetoCreditoRuralCronogramaPagamentoDao, ProjetoCreditoRuralCronogramaPagamento.class, cronogramaPagamentoInvestimentoList);
		salvarPagamentos(cronogramaPagamentoInvestimentoList);

		if (!CollectionUtils.isEmpty(custeioList)) {
			custeioList.forEach((custeio) -> custeio.setTipo(FinanciamentoTipo.C));
		}
		salvarTabelaDependente(result, "custeioList", projetoCreditoRuralFinanciamentoDao, ProjetoCreditoRuralFinanciamento.class, custeioList);

		if (!CollectionUtils.isEmpty(investimentoList)) {
			investimentoList.forEach((investimento) -> investimento.setTipo(FinanciamentoTipo.I));
		}
		salvarTabelaDependente(result, "investimentoList", projetoCreditoRuralFinanciamentoDao, ProjetoCreditoRuralFinanciamento.class, investimentoList);

		salvarTabelaDependente(result, "fluxoCaixaList", projetoCreditoRuralFluxoCaixaDao, ProjetoCreditoRuralFluxoCaixa.class, fluxoCaixaList);
		if (!CollectionUtils.isEmpty(fluxoCaixaList)) {
			fluxoCaixaList.forEach((fluxoCaixa) -> fluxoCaixa.getFluxoCaixaAnoList().forEach((ano) -> {
				ano.setProjetoCreditoRuralFluxoCaixa(fluxoCaixa);
				fluxoCaixaAnoDao.save(ano);
			}));
		}

		salvarTabelaDependente(result, "garantiaList", projetoCreditoRuralGarantiaDao, ProjetoCreditoRuralGarantia.class, garantiaList);

		salvarTabelaDependente(result, "trienioList", projetoCreditoRuralHistoricoReceitaDao, ProjetoCreditoRuralHistoricoReceita.class, trienioList);

		if (!CollectionUtils.isEmpty(parecerTecnicoList)) {
			parecerTecnicoList.forEach((parecer) -> parecer.setUsuario(parecer.getUsuario() == null ? null : getUsuario(parecer.getUsuario().getUsername())));
			salvarTabelaDependente(result, "parecerTecnicoList", projetoCreditoRuralParecerTecnicoDao, ProjetoCreditoRuralParecerTecnico.class, parecerTecnicoList);
		}

		recuperarIdExcluirInexistentes(result, ProjetoCreditoRuralPublicoAlvoPropriedadeRural.class, projetoCreditoRuralPublicoAlvoPropriedadeRuralDao, publicoAlvoPropriedadeRuralList, "getPublicoAlvoPropriedadeRural");
		salvarTabelaDependente(result, "publicoAlvoPropriedadeRuralList", projetoCreditoRuralPublicoAlvoPropriedadeRuralDao, ProjetoCreditoRuralPublicoAlvoPropriedadeRural.class, publicoAlvoPropriedadeRuralList);

		if (!CollectionUtils.isEmpty(despesaList)) {
			despesaList.forEach((despesa) -> despesa.setTipo(FluxoCaixaTipo.D));
		}
		salvarTabelaDependente(result, "despesaList", projetoCreditoRuralReceitaDespesaDao, ProjetoCreditoRuralReceitaDespesa.class, despesaList);

		if (!CollectionUtils.isEmpty(receitaList)) {
			receitaList.forEach((receita) -> receita.setTipo(FluxoCaixaTipo.R));
		}
		salvarTabelaDependente(result, "receitaList", projetoCreditoRuralReceitaDespesaDao, ProjetoCreditoRuralReceitaDespesa.class, receitaList);

		// salvar arquivos vinculados
		List<ProjetoCreditoRuralArquivo> projetoCreditoRuralArquivoList = projetoCreditoRuralArquivoDao.findByProjetoCreditoRural(result);
		if (!CollectionUtils.isEmpty(projetoCreditoRuralArquivoList)) {
			if (CollectionUtils.isEmpty(arquivoList)) {
				projetoCreditoRuralArquivoDao.deleteByProjetoCreditoRural(result);
			} else {
				for (ProjetoCreditoRuralArquivo pcraSalvo : projetoCreditoRuralArquivoList) {
					boolean encontrou = false;
					for (ProjetoCreditoRuralArquivo pcra : arquivoList) {
						if (pcraSalvo.getId().equals(pcra.getId())) {
							encontrou = true;
							break;
						}
					}
					if (!encontrou) {
						projetoCreditoRuralArquivoDao.delete(pcraSalvo);
					}
				}
			}
		}
		if (!CollectionUtils.isEmpty(arquivoList)) {
			for (ProjetoCreditoRuralArquivo pcra : arquivoList) {
				pcra.setProjetoCreditoRural(result);
				pcra.setArquivo(arquivoDao.findByMd5(pcra.getArquivo().getMd5()));
				projetoCreditoRuralArquivoDao.save(pcra);
			}
		}

		dao.flush();

		// não modificar o conteúdo da resposta
		return false;
	}

	@Autowired
	private ArquivoDao arquivoDao;

	@Autowired
	private ProjetoCreditoRuralArquivoDao projetoCreditoRuralArquivoDao;

	@SuppressWarnings("unchecked")
	private <T> void recuperarIdExcluirInexistentes(ProjetoCreditoRural projetoCreditoRural, Class<T> classe, JpaRepository<T, Integer> dao, List<T> lista, String nomeCampoCompara) throws BoException {
		List<T> salvoList = null;
		try {
			salvoList = (List<T>) MethodUtils.invokeMethod(dao, "findAllByProjetoCreditoRural", projetoCreditoRural);
		} catch (Exception e) {
			throw new BoException(e);
		}
		// recuperar IDs já salvos
		if (!CollectionUtils.isEmpty(salvoList)) {
			for (T propSalvo : salvoList) {
				boolean encontrou = false;
				if (!CollectionUtils.isEmpty(lista)) {
					for (T prop : lista) {
						try {
							// comparar chaves primárias dos campos de
							// comparação
							if (((_ChavePrimaria<Serializable>) ((T) MethodUtils.invokeMethod(propSalvo, nomeCampoCompara))).getId().equals(((_ChavePrimaria<Serializable>) ((T) MethodUtils.invokeMethod(prop, nomeCampoCompara))).getId())) {
								// recuperar a chave primaria
								((_ChavePrimaria<Serializable>) prop).setId(((_ChavePrimaria<Serializable>) propSalvo).getId());
								encontrou = true;
								break;
							}
						} catch (Exception e) {
							throw new BoException(e);
						}
					}
				}
				// excluir caso ID não encontrado
				if (!encontrou) {
					dao.delete(propSalvo);
				}
			}
		}
	}

	private void salvarPagamentos(List<ProjetoCreditoRuralCronogramaPagamento> lista) {
		if (!CollectionUtils.isEmpty(lista)) {
			lista.forEach((cronograma) -> cronograma.getCronogramaPagamentoList().forEach((pagto) -> {
				pagto.setProjetoCreditoRuralCronogramaPagamento(cronograma);
				cronogramaPagamentoDao.save(pagto);
			}));
		}
	}

	private <T> void salvarTabelaDependente(ProjetoCreditoRural projetoCreditoRural, String nomeLista, JpaRepository<T, Integer> dao, Class<T> classe, List<T> lista) throws BoException {
		// excluir registros
		excluirRegistros(projetoCreditoRural, nomeLista, dao);
		if (!CollectionUtils.isEmpty(lista)) {
			// salvar registros filhos
			for (T registro : lista) {
				try {
					MethodUtils.invokeMethod(registro, "setProjetoCreditoRural", projetoCreditoRural);
				} catch (Exception e) {
					throw new BoException(e);
				}
				dao.save(registro);
			}
		}
	}
}