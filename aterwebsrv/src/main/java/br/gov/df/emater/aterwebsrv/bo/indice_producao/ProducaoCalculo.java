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
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

class ProducaoCalculo {

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
	public void acumulaItem(String composicaoProducaoFormaId, ProducaoForma producaoForma, Object publicoAlvoId, boolean confirmado) {
		CalculoItem calculo = null;

		if (matriz.containsKey(composicaoProducaoFormaId)) {
			calculo = matriz.get(composicaoProducaoFormaId);
		} else {
			calculo = new CalculoItem();
		}

		if (!confirmado || (confirmado && producaoForma.getDataConfirmacao() != null)) {
			calculo.getProducaoFormaTotal().setItemAValor(acumula(producaoForma.getItemAValor(), calculo.getProducaoFormaTotal().getItemAValor()));
			calculo.getProducaoFormaTotal().setItemBValor(acumula(producaoForma.getItemBValor(), calculo.getProducaoFormaTotal().getItemBValor()));
			calculo.getProducaoFormaTotal().setItemCValor(acumula(producaoForma.getItemCValor(), calculo.getProducaoFormaTotal().getItemCValor()));
			calculo.getProducaoFormaTotal().setVolume(acumula(producaoForma.getVolume(), calculo.getProducaoFormaTotal().getVolume()));
			calculo.getProducaoFormaTotal().setValorUnitario(acumula(producaoForma.getValorUnitario(), calculo.getProducaoFormaTotal().getValorUnitario()));
			calculo.getProducaoFormaTotal().setValorTotal(acumula(producaoForma.getValorTotal(), calculo.getProducaoFormaTotal().getValorTotal()));

			if (publicoAlvoId != null) {
				if (publicoAlvoId instanceof Integer) {
					calculo.getPublicoAlvoList().add((Integer) publicoAlvoId);
				} else {
					calculo.getPublicoAlvoList().addAll((Set<Integer>) publicoAlvoId);
				}
			} else {
				calculo.getProducaoFormaTotal().setQuantidadeProdutores(acumula(producaoForma.getQuantidadeProdutores(), calculo.getProducaoFormaTotal().getQuantidadeProdutores()).intValue());
			}
		}

		matriz.put(composicaoProducaoFormaId, calculo);
	}

	public void atualizarMedias(CalculoItem calculoItem) {
		ItemNome itemA = (ItemNome) this.bemClassificacaoList.get("itemA");
		ItemNome itemB = (ItemNome) this.bemClassificacaoList.get("itemB");
		ItemNome itemC = (ItemNome) this.bemClassificacaoList.get("itemC");

		if (calculoItem.getProducaoFormaTotal().getItemAValor() != null && itemA != null && ItemNomeResultado.M.equals(itemA.getResultado())) {
			BigDecimal vlr = new BigDecimal("0");
			if (calculoItem.getProducaoFormaTotal().getItemBValor() != null && ItemNomeResultado.S.equals(itemB.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoFormaTotal().getItemBValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (calculoItem.getProducaoFormaTotal().getItemCValor() != null && ItemNomeResultado.S.equals(itemC.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoFormaTotal().getItemCValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (BigDecimal.ZERO.equals(vlr)) {
				calculoItem.getProducaoFormaTotal().setItemAValor(BigDecimal.ZERO);
			} else {
				calculoItem.getProducaoFormaTotal().setItemAValor(calculoItem.getProducaoFormaTotal().getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}

		if (calculoItem.getProducaoFormaTotal().getItemBValor() != null && itemB != null && ItemNomeResultado.M.equals(itemB.getResultado())) {
			BigDecimal vlr = new BigDecimal("0");
			if (calculoItem.getProducaoFormaTotal().getItemAValor() != null && ItemNomeResultado.S.equals(itemA.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoFormaTotal().getItemAValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (calculoItem.getProducaoFormaTotal().getItemCValor() != null && ItemNomeResultado.S.equals(itemC.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoFormaTotal().getItemCValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (BigDecimal.ZERO.equals(vlr)) {
				calculoItem.getProducaoFormaTotal().setItemBValor(BigDecimal.ZERO);
			} else {
				calculoItem.getProducaoFormaTotal().setItemBValor(calculoItem.getProducaoFormaTotal().getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}

		if (calculoItem.getProducaoFormaTotal().getItemCValor() != null && itemC != null && ItemNomeResultado.M.equals(itemC.getResultado())) {
			BigDecimal vlr = new BigDecimal("0");
			if (calculoItem.getProducaoFormaTotal().getItemAValor() != null && ItemNomeResultado.S.equals(itemA.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoFormaTotal().getItemAValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (calculoItem.getProducaoFormaTotal().getItemBValor() != null && ItemNomeResultado.S.equals(itemB.getResultado())) {
				vlr = vlr.add(calculoItem.getProducaoFormaTotal().getItemBValor(), UtilitarioNumero.BIG_DECIMAL_PRECISAO);
			}
			if (BigDecimal.ZERO.equals(vlr)) {
				calculoItem.getProducaoFormaTotal().setItemCValor(BigDecimal.ZERO);
			} else {
				calculoItem.getProducaoFormaTotal().setItemCValor(calculoItem.getProducaoFormaTotal().getVolume().divide(vlr, UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}

		if (calculoItem.getProducaoFormaTotal().getVolume() != null && calculoItem.getProducaoFormaTotal().getValorTotal() != null) {
			if (BigDecimal.ZERO.equals(calculoItem.getProducaoFormaTotal().getVolume())) {
				calculoItem.getProducaoFormaTotal().setValorUnitario(BigDecimal.ZERO);
			} else {
				calculoItem.getProducaoFormaTotal().setValorUnitario(calculoItem.getProducaoFormaTotal().getValorTotal().divide(calculoItem.getProducaoFormaTotal().getVolume(), UtilitarioNumero.BIG_DECIMAL_PRECISAO));
			}
		}
	}

	public Map<String, Object> getBemClassificacaoList() {
		return bemClassificacaoList;
	}

	public static String getComposicao(ProducaoForma producaoForma) {
		List<Integer> idList = new ArrayList<Integer>();
		for (ProducaoFormaComposicao composicao : producaoForma.getProducaoFormaComposicaoList()) {
			idList.add(composicao.getFormaProducaoValor().getId());
		}
		Collections.sort(idList);
		return UtilitarioString.collectionToString(idList);
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
			temp.acumulaItem("", item.getValue().getProducaoFormaTotal(), item.getValue().getPublicoAlvoList(), false);
		}
		if (temp.matriz.get("") != null) {
			this.acumulaItem("", temp.matriz.get("").getProducaoFormaTotal(), temp.matriz.get("").getPublicoAlvoList(), false);
		} else {
			this.acumulaItem("", new ProducaoForma(), null, false);
		}
	}
}