package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ferramenta.UtilDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ItemNomeResultado;
import br.gov.df.emater.aterwebsrv.modelo.dto.IndiceProducaoCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ItemNome;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.UnidadeMedida;

@Service("IndiceProducaoFiltrarCmd")
public class FiltrarCmd extends _Comando {

	private class CalculoItem {

		private ProducaoForma producaoFormaTotal = new ProducaoForma();

		private Set<Integer> publicoAlvoList = new HashSet<Integer>();

	}

	private class ProducaoCalculo {

		ProducaoCalculo(Map<String, Object> bemClassificacaoList, String nomeCalculo) {
			this.bemClassificacaoList = bemClassificacaoList;
			this.nomeCalculo = nomeCalculo;
		}

		private String nomeCalculo;

		private Map<String, Object> bemClassificacaoList;

		private Map<String, CalculoItem> matriz = new HashMap<String, CalculoItem>();

		private BigDecimal acumula(Integer origem, Integer destino) {
			return acumula(new BigDecimal(origem == null ? 0 : origem), new BigDecimal(destino == null ? 0 : destino));
		}

		private BigDecimal acumula(BigDecimal origem, BigDecimal destino) {
			if (origem == null && destino == null) {
				return null;
			}
			if (origem == null && destino != null) {
				origem = new BigDecimal("0");
			}
			return (destino == null ? new BigDecimal("0") : destino).add(origem, UtilitarioNumero.BIG_DECIMAL_PRECISAO);
		}

		@SuppressWarnings("unchecked")
		public void acumulaItem(String composicaoProducaoFormaId, ProducaoForma producaoForma, Object publicoAlvoId, boolean confirmado) {
			CalculoItem calculo = null;

			if (matriz.containsKey(composicaoProducaoFormaId)) {
				calculo = matriz.get(composicaoProducaoFormaId);
			} else {
				calculo = new CalculoItem();
			}

			if (!confirmado || (confirmado && producaoForma.getDataConfirmacao() != null)) {
				calculo.producaoFormaTotal.setItemAValor(acumula(producaoForma.getItemAValor(), calculo.producaoFormaTotal.getItemAValor()));
				calculo.producaoFormaTotal.setItemBValor(acumula(producaoForma.getItemBValor(), calculo.producaoFormaTotal.getItemBValor()));
				calculo.producaoFormaTotal.setItemCValor(acumula(producaoForma.getItemCValor(), calculo.producaoFormaTotal.getItemCValor()));
				calculo.producaoFormaTotal.setVolume(acumula(producaoForma.getVolume(), calculo.producaoFormaTotal.getVolume()));
				calculo.producaoFormaTotal.setValorUnitario(acumula(producaoForma.getValorUnitario(), calculo.producaoFormaTotal.getValorUnitario()));
				calculo.producaoFormaTotal.setValorTotal(acumula(producaoForma.getValorTotal(), calculo.producaoFormaTotal.getValorTotal()));

				if (publicoAlvoId != null) {
					if (publicoAlvoId instanceof Integer) {
						calculo.publicoAlvoList.add((Integer) publicoAlvoId);
					} else {
						calculo.publicoAlvoList.addAll((Set<Integer>) publicoAlvoId);
					}
				} else {
					calculo.producaoFormaTotal.setQuantidadeProdutores(acumula(producaoForma.getQuantidadeProdutores(), calculo.producaoFormaTotal.getQuantidadeProdutores()).intValue());
				}
			}

			matriz.put(composicaoProducaoFormaId, calculo);
		}

		public String getComposicao(ProducaoForma producaoForma) {
			List<Integer> idList = new ArrayList<Integer>();
			for (ProducaoFormaComposicao composicao : producaoForma.getProducaoFormaComposicaoList()) {
				idList.add(composicao.getFormaProducaoValor().getId());
			}
			Collections.sort(idList);
			return UtilitarioString.collectionToString(idList);
		}

		public void totalizar() {
			ProducaoCalculo temp = new ProducaoCalculo(bemClassificacaoList, null);
			for (Map.Entry<String, CalculoItem> item : this.matriz.entrySet()) {
				temp.acumulaItem("", item.getValue().producaoFormaTotal, item.getValue().publicoAlvoList, false);
			}
			if (temp.matriz.get("") != null) {
				this.acumulaItem("", temp.matriz.get("").producaoFormaTotal, temp.matriz.get("").publicoAlvoList, false);
			} else {
				this.acumulaItem("", new ProducaoForma(), null, false);
			}
		}

		public void atualizarMedias(CalculoItem calculoItem) {
			ItemNome itemA = (ItemNome) this.bemClassificacaoList.get("itemA");
			ItemNome itemB = (ItemNome) this.bemClassificacaoList.get("itemB");
			ItemNome itemC = (ItemNome) this.bemClassificacaoList.get("itemC");

			if (calculoItem.producaoFormaTotal.getItemAValor() != null && itemA != null && ItemNomeResultado.M.equals(itemA.getResultado())) {
				BigDecimal vlr = new BigDecimal("0");
				if (calculoItem.producaoFormaTotal.getItemBValor() != null && ItemNomeResultado.S.equals(itemB.getResultado())) {
					vlr = vlr.add(calculoItem.producaoFormaTotal.getItemBValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
				}
				if (calculoItem.producaoFormaTotal.getItemCValor() != null && ItemNomeResultado.S.equals(itemC.getResultado())) {
					vlr = vlr.add(calculoItem.producaoFormaTotal.getItemCValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
				}
				if (BigDecimal.ZERO.equals(vlr)) {
					calculoItem.producaoFormaTotal.setItemAValor(BigDecimal.ZERO);
				} else {
					calculoItem.producaoFormaTotal.setItemAValor(calculoItem.producaoFormaTotal.getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
				}
			}
			
			if (calculoItem.producaoFormaTotal.getItemBValor() != null && itemB != null && ItemNomeResultado.M.equals(itemB.getResultado())) {
				BigDecimal vlr = new BigDecimal("0");
				if (calculoItem.producaoFormaTotal.getItemAValor() != null && ItemNomeResultado.S.equals(itemA.getResultado())) {
					vlr = vlr.add(calculoItem.producaoFormaTotal.getItemAValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
				}
				if (calculoItem.producaoFormaTotal.getItemCValor() != null && ItemNomeResultado.S.equals(itemC.getResultado())) {
					vlr = vlr.add(calculoItem.producaoFormaTotal.getItemCValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
				}
				if (BigDecimal.ZERO.equals(vlr)) {
					calculoItem.producaoFormaTotal.setItemBValor(BigDecimal.ZERO);
				} else {
					calculoItem.producaoFormaTotal.setItemBValor(calculoItem.producaoFormaTotal.getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
				}
			}
			
			if (calculoItem.producaoFormaTotal.getItemCValor() != null && itemC != null && ItemNomeResultado.M.equals(itemC.getResultado())) {
				BigDecimal vlr = new BigDecimal("0");
				if (calculoItem.producaoFormaTotal.getItemAValor() != null && ItemNomeResultado.S.equals(itemA.getResultado())) {
					vlr = vlr.add(calculoItem.producaoFormaTotal.getItemAValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
				}
				if (calculoItem.producaoFormaTotal.getItemBValor() != null && ItemNomeResultado.S.equals(itemB.getResultado())) {
					vlr = vlr.add(calculoItem.producaoFormaTotal.getItemBValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
				}
				if (BigDecimal.ZERO.equals(vlr)) {
					calculoItem.producaoFormaTotal.setItemCValor(BigDecimal.ZERO);
				} else {
					calculoItem.producaoFormaTotal.setItemCValor(calculoItem.producaoFormaTotal.getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
				}
			}
			
			if (calculoItem.producaoFormaTotal.getVolume() != null && calculoItem.producaoFormaTotal.getValorTotal() != null) {				
				if (BigDecimal.ZERO.equals(calculoItem.producaoFormaTotal.getVolume())) {
					calculoItem.producaoFormaTotal.setValorUnitario(BigDecimal.ZERO);
				} else {
					calculoItem.producaoFormaTotal.setValorUnitario(calculoItem.producaoFormaTotal.getValorTotal().divide(calculoItem.producaoFormaTotal.getVolume(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
				}
			}
		}
	}

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private UtilDao utilDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		IndiceProducaoCadFiltroDto filtro = (IndiceProducaoCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		List<Producao> producaoList = null;
		producaoList = (List<Producao>) dao.filtrar(filtro);

		if (producaoList != null) {
			// fetch no resultado
			if (filtro.getId() != null) {
				if (producaoList != null && producaoList.size() == 1) {
					producaoList = new ArrayList<Producao>();
					// result.add(fetchProdutor(result.get(0)));
				}
			} else {

				// contabilizar a producao
				for (Producao producao : producaoList) {
					List<Object> resultProdutor = null;

					Map<String, Object> bemClassificacaoList = utilDao.ipaBemClassificacaoDetalhes(producao.getBem().getBemClassificacao());

					ProducaoCalculo calculoProducaoTotal = new ProducaoCalculo(bemClassificacaoList, "Estimada");
					ProducaoCalculo calculoProducaoProdutoresEsperada = new ProducaoCalculo(bemClassificacaoList, "Calculada");
					ProducaoCalculo calculoProducaoProdutoresConfirmada = new ProducaoCalculo(bemClassificacaoList, "Confirmada");

					// contabilizar os produtores da producao
					List<Producao> produtorProducaoList = dao.findByAnoAndBemAndPropriedadeRuralComunidadeUnidadeOrganizacional(producao.getAno(), producao.getBem(), producao.getUnidadeOrganizacional());
					if (produtorProducaoList != null) {

						// looping pelos produtores cadastrados
						for (Producao produtorProducao : produtorProducaoList) {
							if (produtorProducao.getProducaoFormaList() != null) {
								ProducaoCalculo calculoProdutorEsperada = new ProducaoCalculo(bemClassificacaoList, "Estimada");
								ProducaoCalculo calculoProdutorConfirmada = new ProducaoCalculo(bemClassificacaoList, "Confirmada");
								for (ProducaoForma producaoForma : produtorProducao.getProducaoFormaList()) {
									String composicao = calculoProdutorEsperada.getComposicao(producaoForma);
									Integer publicoAlvoId = produtorProducao.getPublicoAlvo().getId();

									// acumular os totais do produtor
									calculoProducaoProdutoresEsperada.acumulaItem(composicao, producaoForma, publicoAlvoId, false);
									calculoProducaoProdutoresConfirmada.acumulaItem(composicao, producaoForma, publicoAlvoId, true);

									// acumular os totais da producao
									calculoProdutorEsperada.acumulaItem("", producaoForma, publicoAlvoId, false);
									calculoProdutorConfirmada.acumulaItem("", producaoForma, publicoAlvoId, true);
								}
								if (resultProdutor == null) {
									resultProdutor = new ArrayList<Object>();
								}
								resultProdutor.add(fetch(produtorProducao, calculoProdutorEsperada, calculoProdutorConfirmada));
							}
						}
					}

					// calcular a produção estimada informada
					for (ProducaoForma producaoForma : producao.getProducaoFormaList()) {
						calculoProducaoTotal.acumulaItem("", producaoForma, null, false);
					}

					calculoProducaoProdutoresEsperada.totalizar();

					calculoProducaoProdutoresConfirmada.totalizar();

					if (result == null) {
						result = new ArrayList<Object>();
					}
					result.add(fetch(producao, calculoProducaoProdutoresEsperada, calculoProducaoProdutoresConfirmada, calculoProducaoTotal, resultProdutor));
				}
			}
		}

		contexto.setResposta(result);
		return false;
	}

	private List<Object> fetch(Producao producao, ProducaoCalculo calculoProducaoEsperada, ProducaoCalculo calculoProducaoConfirmada) {
		return fetch(producao, calculoProducaoEsperada, calculoProducaoConfirmada, null, null);
	}

	private List<Object> fetch(Producao producao, ProducaoCalculo esperada, ProducaoCalculo confirmada, ProducaoCalculo total, List<Object> resultProdutor) {
		List<Object> result = new ArrayList<Object>();

		result.add(producao.getId()); // PRODUCAO_ID
		result.add(producao.getAno()); // PRODUCAO_ANO
		result.add(producao.getBem().getId()); // PRODUCAO_BEM_ID
		result.add(producao.getBem().getNome()); // PRODUCAO_BEM_NOME

		result.add(esperada.bemClassificacaoList == null ? null : esperada.bemClassificacaoList.get("bemClassificacao")); // PRODUCAO_BEM_CLASSIFICACAO
		result.add(esperada.bemClassificacaoList == null ? null : esperada.bemClassificacaoList.get("unidadeMedida") == null ? null : ((UnidadeMedida) esperada.bemClassificacaoList.get("unidadeMedida")).getNome()); // PRODUCAO_UNIDADE_MEDIDA
		result.add(esperada.bemClassificacaoList == null ? null : esperada.bemClassificacaoList.get("formulaProduto") == null ? null : esperada.bemClassificacaoList.get("formulaProduto").toString()); // PRODUCAO_FORMULA
		result.add(esperada.bemClassificacaoList == null ? null : esperada.bemClassificacaoList.get("itemA") == null ? null : ((ItemNome) esperada.bemClassificacaoList.get("itemA")).getNome()); // PRODUCAO_NOME_ITEM_A
		result.add(esperada.bemClassificacaoList == null ? null : esperada.bemClassificacaoList.get("itemB") == null ? null : ((ItemNome) esperada.bemClassificacaoList.get("itemB")).getNome()); // PRODUCAO_NOME_ITEM_B
		result.add(esperada.bemClassificacaoList == null ? null : esperada.bemClassificacaoList.get("itemC") == null ? null : ((ItemNome) esperada.bemClassificacaoList.get("itemC")).getNome()); // PRODUCAO_NOME_ITEM_C

		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getId()); // PRODUCAO_UNID_ORG_ID
		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getNome()); // PRODUCAO_UNID_ORG_NOME
		result.add(producao.getUnidadeOrganizacional() == null ? null : producao.getUnidadeOrganizacional().getSigla()); // PRODUCAO_UNID_ORG_SIGLA

		result.add(producao.getPublicoAlvo() == null ? null : producao.getPublicoAlvo().getId()); // PRODUCAO_PUBLICO_ALVO_ID
		result.add(producao.getPublicoAlvo() == null ? null : producao.getPublicoAlvo().getPessoa().getNome()); // PRODUCAO_PUBLICO_ALVO_NOME

		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getId()); // PRODUCAO_PROPRIEDADE_RURAL_ID
		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getNome()); // PRODUCAO_PROPRIEDADE_RURAL_NOME

		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getComunidade().getId()); // PRODUCAO_COMUNIDADE_ID
		result.add(producao.getPropriedadeRural() == null ? null : producao.getPropriedadeRural().getComunidade().getNome()); // PRODUCAO_COMUNIDADE_NOME

		// TODO fazer o totalizador final esperado e confirmado
		result.add(fetchProducaoFormaList(producao.getProducaoFormaList(), total, esperada, confirmada)); // PRODUCAO_FORMA_LIST

		result.add(producao.getInclusaoUsuario() == null ? null : producao.getInclusaoUsuario().getPessoa().getNome()); // PRODUCAO_INCLUSAO_NOME
		result.add(producao.getInclusaoUsuario() == null ? null : producao.getInclusaoData()); // PRODUCAO_INCLUSAO_DATA
		result.add(producao.getAlteracaoUsuario() == null ? null : producao.getAlteracaoUsuario().getPessoa().getNome()); // PRODUCAO_ALTERACAO_NOME
		result.add(producao.getAlteracaoUsuario() == null ? null : producao.getAlteracaoData()); // PRODUCAO_ALTERACAO_DATA

		result.add(resultProdutor); // PRODUCAO_PRODUTOR_LIST

		return result;
	}

	private List<Object> fetchProducaoFormaList(List<ProducaoForma> producaoFormaList, ProducaoCalculo total, ProducaoCalculo esperada, ProducaoCalculo confirmada) {
		List<Object> result = new ArrayList<Object>();
		
		// inserir todas as formas de producao
		if (producaoFormaList != null) {
			List<Object> registro = null;
			for (ProducaoForma producaoForma : producaoFormaList) {
				registro = fetchProducaoForma(producaoForma);
				String composicao = esperada.getComposicao(producaoForma);

				// inserir o totalizador das formas esperadas
				if (esperada != null && esperada.matriz.containsKey(composicao)) {
					CalculoItem calculoItem = esperada.matriz.get(composicao);
					calculoItem.producaoFormaTotal.setQuantidadeProdutores(calculoItem.publicoAlvoList.size());
					esperada.atualizarMedias(calculoItem);
					registro.addAll(fetchProducaoForma(calculoItem.producaoFormaTotal));
				}

				// inserir o totalizador das formas confirmadas
				if (confirmada != null && confirmada.matriz.containsKey(composicao)) {
					CalculoItem calculoItem = confirmada.matriz.get(composicao);
					calculoItem.producaoFormaTotal.setQuantidadeProdutores(calculoItem.publicoAlvoList.size());
					confirmada.atualizarMedias(calculoItem);
					registro.addAll(fetchProducaoForma(calculoItem.producaoFormaTotal));
				}
				result.add(registro);
			}
		}

		// inserir o totalizador total das formas esperadas
		if (total != null && total.matriz.containsKey("")) {
			CalculoItem calculoItem = total.matriz.get("");
			//calculoItem.producaoFormaTotal.setQuantidadeProdutores(calculoItem.publicoAlvoList.size());
			total.atualizarMedias(calculoItem);
			result.add(fetchProducaoForma(calculoItem.producaoFormaTotal, total.nomeCalculo));
		}

		// inserir o totalizador das formas esperadas
		if (esperada != null && esperada.matriz.containsKey("")) {
			CalculoItem calculoItem = esperada.matriz.get("");
			calculoItem.producaoFormaTotal.setQuantidadeProdutores(calculoItem.publicoAlvoList.size());
			esperada.atualizarMedias(calculoItem);
			result.add(fetchProducaoForma(calculoItem.producaoFormaTotal, esperada.nomeCalculo));
		}

		// inserir o totalizador das formas confirmadas
		if (confirmada != null && confirmada.matriz.containsKey("")) {
			CalculoItem calculoItem = confirmada.matriz.get("");
			calculoItem.producaoFormaTotal.setQuantidadeProdutores(calculoItem.publicoAlvoList.size());
			confirmada.atualizarMedias(calculoItem);
			result.add(fetchProducaoForma(calculoItem.producaoFormaTotal, confirmada.nomeCalculo));
		}

		return result;
	}

	private List<Object> fetchProducaoForma(ProducaoForma producaoForma) {
		return fetchProducaoForma(producaoForma, null);
	}

	private List<Object> fetchProducaoForma(ProducaoForma producaoForma, String nomeCalculo) {
		List<Object> result = new ArrayList<Object>();

		result.add(fetchProducaoFormaComposicaoList(producaoForma.getProducaoFormaComposicaoList())); // FORMA_COMPOSICAO_LIST
		result.add(producaoForma.getItemAValor()); // FORMA_VALOR_ITEM_A
		result.add(producaoForma.getItemBValor()); // FORMA_VALOR_ITEM_B
		result.add(producaoForma.getItemCValor()); // FORMA_VALOR_ITEM_C
		result.add(producaoForma.getVolume()); // FORMA_VOLUME
		result.add(producaoForma.getValorUnitario()); // FORMA_VLR_UNIT
		result.add(producaoForma.getValorTotal()); // FORMA_VLR_TOTAL
		result.add(producaoForma.getQuantidadeProdutores()); // FORMA_QTD_PRODUTORES
		result.add(producaoForma.getDataConfirmacao()); // FORMA_DATA_CONFIRMACAO
		result.add(producaoForma.getInclusaoUsuario() == null ? null : producaoForma.getInclusaoUsuario().getPessoa().getNome()); // FORMA_INCLUSAO_NOME
		result.add(producaoForma.getInclusaoData()); // FORMA_INCLUSAO_DATA
		result.add(producaoForma.getAlteracaoUsuario() == null ? null : producaoForma.getAlteracaoUsuario().getPessoa().getNome()); // FORMA_ALTERACAO_NOME
		result.add(producaoForma.getAlteracaoData()); // FORMA_ALTERACAO_DATA
		result.add(nomeCalculo); // FORMA_NOME_CALCULO

		return result;
	}

	private List<Object> fetchProducaoFormaComposicaoList(List<ProducaoFormaComposicao> producaoFormaComposicaoList) {
		List<Object> result = null;
		if (producaoFormaComposicaoList != null) {
			for (ProducaoFormaComposicao producaoFormaComposicao : producaoFormaComposicaoList) {
				List<Object> linha = new ArrayList<Object>();
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getId()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_ID
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getFormaProducaoItem().getNome()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_ITEM_NOME
				linha.add(producaoFormaComposicao.getFormaProducaoValor().getNome()); // COMPOSICAO_FORMA_PRODUCAO_VALOR_NOME
				linha.add(producaoFormaComposicao.getOrdem()); // COMPOSICAO_ORDEM
				if (result == null) {
					result = new ArrayList<Object>();
				}
				result.add(linha);
			}
		}
		return result;
	}

}
// @Autowired
// private ProducaoCalculo calculo;
//
// @Autowired
// private UtilDao utilDao;
//
//
// @SuppressWarnings("unused")
// private List<Object> fetch(List<Producao> producaoList) {
// List<Object> result = null;
// if (producaoList != null) {
// for (Producao producao : producaoList) {
// // calcular somatorios
// calculo.abreProducao(producao.getId());
// calculo.calcular(true);
//
// List<Object> linha = new ArrayList<Object>();
//
// linha.add(calculo.getProducao().getId()); // PRODUCAO_ID
// linha.add(calculo.getProducao().getAno()); // PRODUCAO_ANO
// linha.add(calculo.getProducao().getComunidade().getUnidadeOrganizacionalComunidadeList().get(0).getUnidadeOrganizacional().getNome());
// // PRODUCAO_UNIDADE_LOCAL
// Map<String, Object> bemClassificacaoList =
// utilDao.ipaBemClassificacaoDetalhes(calculo.getProducao().getBem().getBemClassificacao());
// linha.add(bemClassificacaoList.get("bemClassificacao")); // CLASSIFICACAO_BEM
// linha.add(calculo.getProducao().getBem().getNome()); // BEM_NOME
// linha.add(((UnidadeMedida)
// bemClassificacaoList.get("unidadeMedida")).getNome()); // UNIDADE_MEDIDA
// linha.add(fetchProducaoFormaList(calculo.getProducao().getProducaoFormaList()));
// // PRODUCAO_FORMA_LIST
// linha.add(fetchProdutores(calculo.getProducao())); // PRODUTORES_LIST
// linha.add(bemClassificacaoList.get("formulaProduto").toString()); // FORMULA
// linha.add(bemClassificacaoList.get("itemA") == null ? null : ((ItemNome)
// bemClassificacaoList.get("itemA")).getNome()); // ITEM_NOME_A
// linha.add(bemClassificacaoList.get("itemB") == null ? null : ((ItemNome)
// bemClassificacaoList.get("itemB")).getNome()); // ITEM_NOME_B
// linha.add(bemClassificacaoList.get("itemC") == null ? null : ((ItemNome)
// bemClassificacaoList.get("itemC")).getNome()); // ITEM_NOME_C
// linha.add(calculo.getProducao().getComunidade().getId()); // COMUNIDADE_ID
// linha.add(calculo.getProducao().getBem().getId()); // BEM_ID
// linha.add(calculo.getProducao().getComunidade().getNome()); //
// PRODUCAO_COMUNIDADE
//
// if (result == null) {
// result = new ArrayList<Object>();
// }
// result.add(linha.toArray());
// }
// }
// return result;
// }
//
// private List<Object[]> fetchProducaoFormaList(List<ProducaoForma>
// producaoFormaList) {
// List<Object[]> result = null;
// if (producaoFormaList != null) {
// for (ProducaoForma producaoForma : producaoFormaList) {
// List<Object> linha = new ArrayList<Object>();
// linha.add(fetchProducaoFormaComposicaoList(producaoForma.getProducaoFormaComposicaoList()));
// // PRODUCAO_FORMA_COMPOSICAO_LIST
// linha.add(producaoForma.getVolume()); // PROD_FORMA_VOLUME
// linha.add(0); // PROD_FORMA_VOLUME_ESPERADO
// linha.add(producaoForma.getQuantidadeProdutores()); //
// PROD_FORMA_QTD_PRODUTORES
// linha.add(0); // PROD_FORMA_VOLUME_CONFIRMADO
// linha.add(producaoForma.getItemAValor()); // PROD_FORMA_ITEM_A_VALOR
// linha.add(producaoForma.getItemBValor()); // PROD_FORMA_ITEM_B_VALOR
// linha.add(producaoForma.getItemCValor()); // PROD_FORMA_ITEM_C_VALOR
// linha.add(producaoForma.getValorUnitario()); // PROD_FORMA_VLR_UNITARIO
// linha.add(producaoForma.getValorTotal()); // PROD_FORMA_VLR_TOTAL
// linha.add(producaoForma.getDataConfirmacao()); // PROD_FORMA_DT_CONFIRM
//
// Object[] mapa =
// calculo.getMapaDeProducaoForma().get(calculo.getComposicao(producaoForma));
//
// if (mapa != null) {
// ProducaoForma confirmado = (ProducaoForma) mapa[2];
// linha.add(confirmado.getItemAValor()); // PROD_FORMA_ITEM_A_VALOR_CONFIRM
// linha.add(confirmado.getItemBValor()); // PROD_FORMA_ITEM_B_VALOR_CONFIRM
// linha.add(confirmado.getItemCValor()); // PROD_FORMA_ITEM_C_VALOR_CONFIRM
// linha.add(confirmado.getVolume()); // PROD_FORMA_VOLUME_CONFIRM
// linha.add(confirmado.getValorUnitario()); // PROD_FORMA_VLR_UNITARIO_CONFIRM
// linha.add(confirmado.getValorTotal()); // PROD_FORMA_VLR_TOTAL_CONFIRM
// linha.add(confirmado.getQuantidadeProdutores()); //
// PROD_FORMA_QTD_PRODUTORES_CONFIRM
// }
//
// if (result == null) {
// result = new ArrayList<Object[]>();
// }
// result.add(linha.toArray());
// }
// }
// return result;
// }
//
// private List<Object[]>
// fetchProducaoFormaComposicaoList(List<ProducaoFormaComposicao>
// producaoFormaComposicaoList) {
// List<Object[]> result = null;
// if (producaoFormaComposicaoList != null) {
// for (ProducaoFormaComposicao producaoFormaComposicao :
// producaoFormaComposicaoList) {
// List<Object> linha = new ArrayList<Object>();
// linha.add(producaoFormaComposicao.getFormaProducaoValor().getFormaProducaoItem().getNome());
// // COMPOSICAO_FORMA_PRODUCAO_VALOR_ITEM_NOME
// linha.add(producaoFormaComposicao.getFormaProducaoValor().getNome()); //
// COMPOSICAO_FORMA_PRODUCAO_VALOR_NOME
// linha.add(producaoFormaComposicao.getOrdem()); // COMPOSICAO_ORDEM
// linha.add(producaoFormaComposicao.getFormaProducaoValor().getId()); //
// COMPOSICAO_FORMA_PRODUCAO_VALOR_ID
// if (result == null) {
// result = new ArrayList<Object[]>();
// }
// result.add(linha.toArray());
// }
// }
// return result;
// }
//
// private List<Object> fetchProdutor(Producao producao) {
// List<Object> result = null;
// if (producao != null) {
// result = new ArrayList<Object>();
// result.add(producao.getPropriedadeRural() != null ?
// producao.getPropriedadeRural().getNome() : null);// PROPRIEDADE_RURAL_NOME
// result.add(producao.getPublicoAlvo() != null ?
// producao.getPublicoAlvo().getPessoa().getNome() : null);// PUBLICO_ALVO_NOME
// result.add(fetchProducaoFormaList(producao.getProducaoFormaList()));//
// PRODUTOR_PRODUCAO_FORMA_LIST
// result.add(producao.getId());// PRODUTOR_PRODUCAO_ID
// }
// return result;
// }
//
// private List<Object[]> fetchProdutores(Producao producao) {
// List<Object[]> result = null;
// if (producao != null) {
// List<Producao> producaoList =
// dao.findByAnoAndBemAndPropriedadeRuralComunidade(producao.getAno(),
// producao.getBem(), producao.getComunidade());
// if (producaoList != null) {
// for (Producao p : producaoList) {
// if (result == null) {
// result = new ArrayList<Object[]>();
// }
// result.add(fetchProdutor(p).toArray());
// }
// }
// }
// return result;
// }
