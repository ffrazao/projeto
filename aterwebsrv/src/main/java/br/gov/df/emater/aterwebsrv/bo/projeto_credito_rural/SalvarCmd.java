package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.reflect.MethodUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.CronogramaPagamentoDao;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.FluxoCaixaAnoDao;
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

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Atividade atividade = (Atividade) contexto.getRequisicao();

		ProjetoCreditoRural result = atividade.getProjetoCreditoRural();
		if (result == null) {
			return false;
		}

		// atualizar o campo de ligação
		result.setAtividade(atividade);

		// limpar os id das tabelas dependentes
		if (!CollectionUtils.isEmpty(result.getCronogramaPagamentoCusteioList())) {
			limparChavePrimaria(result.getCronogramaPagamentoCusteioList());
			result.getCronogramaPagamentoCusteioList().forEach((c) -> {
				limparChavePrimaria(c.getCronogramaPagamentoList());
			});
		}
		if (!CollectionUtils.isEmpty(result.getCronogramaPagamentoInvestimentoList())) {
			limparChavePrimaria(result.getCronogramaPagamentoInvestimentoList());
			result.getCronogramaPagamentoInvestimentoList().forEach((c) -> {
				limparChavePrimaria(c.getCronogramaPagamentoList());
			});
		}
		limparChavePrimaria(result.getCusteioList());
		limparChavePrimaria(result.getInvestimentoList());
		if (!CollectionUtils.isEmpty(result.getFluxoCaixaList())) {
			limparChavePrimaria(result.getFluxoCaixaList());
			result.getFluxoCaixaList().forEach((c) -> {
				limparChavePrimaria(c.getFluxoCaixaAnoList());
			});
		}
		limparChavePrimaria(result.getGarantiaList());
		limparChavePrimaria(result.getTrienioList());
		limparChavePrimaria(result.getParecerTecnicoList());
		limparChavePrimaria(result.getPublicoAlvoPropriedadeRuralList());
		limparChavePrimaria(result.getDespesaList());
		limparChavePrimaria(result.getReceitaList());

		// salvar a tabela principal
		result = dao.save(result);

		if (!CollectionUtils.isEmpty(result.getCronogramaPagamentoCusteioList())) {
			result.getCronogramaPagamentoCusteioList().forEach((custeio) -> custeio.setTipo(FinanciamentoTipo.C));
		}
		salvarTabelaDependente(result, "cronogramaPagamentoCusteioList", projetoCreditoRuralCronogramaPagamentoDao, ProjetoCreditoRuralCronogramaPagamento.class, result.getCronogramaPagamentoCusteioList());
		salvarPagamentos(result.getCronogramaPagamentoCusteioList());

		if (!CollectionUtils.isEmpty(result.getCronogramaPagamentoInvestimentoList())) {
			result.getCronogramaPagamentoInvestimentoList().forEach((investimento) -> investimento.setTipo(FinanciamentoTipo.I));
		}
		salvarTabelaDependente(result, "cronogramaPagamentoInvestimentoList", projetoCreditoRuralCronogramaPagamentoDao, ProjetoCreditoRuralCronogramaPagamento.class, result.getCronogramaPagamentoInvestimentoList());
		salvarPagamentos(result.getCronogramaPagamentoInvestimentoList());

		if (!CollectionUtils.isEmpty(result.getCusteioList())) {
			result.getCusteioList().forEach((custeio) -> custeio.setTipo(FinanciamentoTipo.C));
		}
		salvarTabelaDependente(result, "custeioList", projetoCreditoRuralFinanciamentoDao, ProjetoCreditoRuralFinanciamento.class, result.getCusteioList());

		if (!CollectionUtils.isEmpty(result.getInvestimentoList())) {
			result.getInvestimentoList().forEach((investimento) -> investimento.setTipo(FinanciamentoTipo.I));
		}
		salvarTabelaDependente(result, "investimentoList", projetoCreditoRuralFinanciamentoDao, ProjetoCreditoRuralFinanciamento.class, result.getInvestimentoList());

		salvarTabelaDependente(result, "fluxoCaixaList", projetoCreditoRuralFluxoCaixaDao, ProjetoCreditoRuralFluxoCaixa.class, result.getFluxoCaixaList());
		result.getFluxoCaixaList().forEach((fluxoCaixa) -> fluxoCaixa.getFluxoCaixaAnoList().forEach((ano) -> {
			ano.setProjetoCreditoRuralFluxoCaixa(fluxoCaixa);
			fluxoCaixaAnoDao.save(ano);
		}));

		salvarTabelaDependente(result, "garantiaList", projetoCreditoRuralGarantiaDao, ProjetoCreditoRuralGarantia.class, result.getGarantiaList());

		salvarTabelaDependente(result, "trienioList", projetoCreditoRuralHistoricoReceitaDao, ProjetoCreditoRuralHistoricoReceita.class, result.getTrienioList());

		result.getParecerTecnicoList().forEach((parecer) -> parecer.setUsuario(parecer.getUsuario() == null ? null : getUsuario(parecer.getUsuario().getUsername())));
		salvarTabelaDependente(result, "parecerTecnicoList", projetoCreditoRuralParecerTecnicoDao, ProjetoCreditoRuralParecerTecnico.class, result.getParecerTecnicoList());

		recuperarIdExcluirInexistentes(result, ProjetoCreditoRuralPublicoAlvoPropriedadeRural.class, projetoCreditoRuralPublicoAlvoPropriedadeRuralDao, result.getPublicoAlvoPropriedadeRuralList(), "getPublicoAlvoPropriedadeRural");
		salvarTabelaDependente(result, "publicoAlvoPropriedadeRuralList", projetoCreditoRuralPublicoAlvoPropriedadeRuralDao, ProjetoCreditoRuralPublicoAlvoPropriedadeRural.class, result.getPublicoAlvoPropriedadeRuralList());

		if (!CollectionUtils.isEmpty(result.getDespesaList())) {
			result.getDespesaList().forEach((despesa) -> despesa.setTipo(FluxoCaixaTipo.D));
		}
		salvarTabelaDependente(result, "despesaList", projetoCreditoRuralReceitaDespesaDao, ProjetoCreditoRuralReceitaDespesa.class, result.getDespesaList());

		if (!CollectionUtils.isEmpty(result.getReceitaList())) {
			result.getReceitaList().forEach((receita) -> receita.setTipo(FluxoCaixaTipo.R));
		}
		salvarTabelaDependente(result, "receitaList", projetoCreditoRuralReceitaDespesaDao, ProjetoCreditoRuralReceitaDespesa.class, result.getReceitaList());

		dao.flush();

		// não modificar o conteúdo da resposta
		return false;
	}

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
		lista.forEach((cronograma) -> cronograma.getCronogramaPagamentoList().forEach((pagto) -> {
			pagto.setProjetoCreditoRuralCronogramaPagamento(cronograma);
			cronogramaPagamentoDao.save(pagto);
		}));
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