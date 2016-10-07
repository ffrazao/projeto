package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;
import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioString;
import br.gov.df.emater.aterwebsrv.modelo.dominio.ItemNomeResultado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ItemNome;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoComposicao;

class ProducaoCalculo {

	public static String getComposicao(Producao producao) {
		List<Integer> idList = new ArrayList<Integer>();
		for (ProducaoComposicao composicao : producao.getProducaoComposicaoList()) {
			idList.add(composicao.getFormaProducaoValor().getId());
		}
		Collections.sort(idList);
		return UtilitarioString.collectionToString(idList);
	}

	private Map<String, Object> bemClassificacaoList;

	private Map<String, CalculoItem> matriz = new HashMap<String, CalculoItem>();

	private String nomeCalculo;

	ProducaoCalculo(Map<String, Object> bemClassificacaoList, String nomeCalculo) {
		this.bemClassificacaoList = bemClassificacaoList;
		this.nomeCalculo = nomeCalculo;
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

	private BigDecimal acumula(Integer origem, Integer destino) {
		return acumula(new BigDecimal(origem == null ? 0 : origem), new BigDecimal(destino == null ? 0 : destino));
	}

	@SuppressWarnings("unchecked")
	public void acumulaItem(String composicaoProducaoId, Producao producao, Object publicoAlvoId, boolean confirmado) {
		CalculoItem calculo = null;

		if (matriz.containsKey(composicaoProducaoId)) {
			calculo = matriz.get(composicaoProducaoId);
		} else {
			calculo = new CalculoItem();
		}

		if (!confirmado || (confirmado && producao.getDataConfirmacao() != null)) {
			calculo.getProducaoTotal().setItemAValor(acumula(producao.getItemAValor(), calculo.getProducaoTotal().getItemAValor()));
			calculo.getProducaoTotal().setItemBValor(acumula(producao.getItemBValor(), calculo.getProducaoTotal().getItemBValor()));
			calculo.getProducaoTotal().setItemCValor(acumula(producao.getItemCValor(), calculo.getProducaoTotal().getItemCValor()));
			calculo.getProducaoTotal().setVolume(acumula(producao.getVolume(), calculo.getProducaoTotal().getVolume()));
			calculo.getProducaoTotal().setValorUnitario(acumula(producao.getValorUnitario(), calculo.getProducaoTotal().getValorUnitario()));
			calculo.getProducaoTotal().setValorTotal(acumula(producao.getValorTotal(), calculo.getProducaoTotal().getValorTotal()));

			if (publicoAlvoId != null) {
				if (publicoAlvoId instanceof Integer) {
					calculo.getPublicoAlvoList().add((Integer) publicoAlvoId);
				} else {
					calculo.getPublicoAlvoList().addAll((Set<Integer>) publicoAlvoId);
				}
			} else {
				calculo.getProducaoTotal().setQuantidadeProdutores(acumula(producao.getQuantidadeProdutores(), calculo.getProducaoTotal().getQuantidadeProdutores()).intValue());
			}
		}

		matriz.put(composicaoProducaoId, calculo);
	}

	public void atualizarMedias(CalculoItem calculoItem) {
		ItemNome itemA = (ItemNome) this.bemClassificacaoList.get("itemA");
		ItemNome itemB = (ItemNome) this.bemClassificacaoList.get("itemB");
		ItemNome itemC = (ItemNome) this.bemClassificacaoList.get("itemC");

		if (calculoItem.getProducaoTotal().getItemAValor() != null && itemA != null && ItemNomeResultado.M.equals(itemA.getResultado())) {
			BigDecimal vlr = new BigDecimal("0");
			if (calculoItem.getProducaoTotal().getItemBValor() != null && ItemNomeResultado.S.equals(itemB.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoTotal().getItemBValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (calculoItem.getProducaoTotal().getItemCValor() != null && ItemNomeResultado.S.equals(itemC.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoTotal().getItemCValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (BigDecimal.ZERO.equals(vlr)) {
				calculoItem.getProducaoTotal().setItemAValor(BigDecimal.ZERO);
			} else {
				calculoItem.getProducaoTotal().setItemAValor(calculoItem.getProducaoTotal().getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}

		if (calculoItem.getProducaoTotal().getItemBValor() != null && itemB != null && ItemNomeResultado.M.equals(itemB.getResultado())) {
			BigDecimal vlr = new BigDecimal("0");
			if (calculoItem.getProducaoTotal().getItemAValor() != null && ItemNomeResultado.S.equals(itemA.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoTotal().getItemAValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (calculoItem.getProducaoTotal().getItemCValor() != null && ItemNomeResultado.S.equals(itemC.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoTotal().getItemCValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (BigDecimal.ZERO.equals(vlr)) {
				calculoItem.getProducaoTotal().setItemBValor(BigDecimal.ZERO);
			} else {
				calculoItem.getProducaoTotal().setItemBValor(calculoItem.getProducaoTotal().getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}

		if (calculoItem.getProducaoTotal().getItemCValor() != null && itemC != null && ItemNomeResultado.M.equals(itemC.getResultado())) {
			BigDecimal vlr = new BigDecimal("0");
			if (calculoItem.getProducaoTotal().getItemAValor() != null && ItemNomeResultado.S.equals(itemA.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoTotal().getItemAValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (calculoItem.getProducaoTotal().getItemBValor() != null && ItemNomeResultado.S.equals(itemB.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoTotal().getItemBValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (BigDecimal.ZERO.equals(vlr)) {
				calculoItem.getProducaoTotal().setItemCValor(BigDecimal.ZERO);
			} else {
				calculoItem.getProducaoTotal().setItemCValor(calculoItem.getProducaoTotal().getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}

		if (calculoItem.getProducaoTotal().getVolume() != null && calculoItem.getProducaoTotal().getValorTotal() != null) {
			if (BigDecimal.ZERO.equals(calculoItem.getProducaoTotal().getVolume())) {
				calculoItem.getProducaoTotal().setValorUnitario(BigDecimal.ZERO);
			} else {
				calculoItem.getProducaoTotal().setValorUnitario(calculoItem.getProducaoTotal().getValorTotal().divide(calculoItem.getProducaoTotal().getVolume(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}
	}

	public Map<String, Object> getBemClassificacaoList() {
		return bemClassificacaoList;
	}

	public Map<String, CalculoItem> getMatriz() {
		return matriz;
	}

	public String getNomeCalculo() {
		return nomeCalculo;
	}

	public void totalizar() {
		ProducaoCalculo temp = new ProducaoCalculo(bemClassificacaoList, null);
		for (Map.Entry<String, CalculoItem> item : this.matriz.entrySet()) {
			temp.acumulaItem("", item.getValue().getProducaoTotal(), item.getValue().getPublicoAlvoList(), false);
		}
		if (temp.matriz.get("") != null) {
			this.acumulaItem("", temp.matriz.get("").getProducaoTotal(), temp.matriz.get("").getPublicoAlvoList(), false);
		} else {
			this.acumulaItem("", new Producao(), null, false);
		}
	}
}