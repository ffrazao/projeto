package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.FluxoCaixaTipo;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralCronogramaPagamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFinanciamento;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRuralFluxoCaixa;

@Service("ProjetoCreditoRuralCalcularFluxoCaixaCmd")
public class CalcularFluxoCaixaCmd extends _SalvarCmd {

	//@SuppressWarnings("unused")
	private class Item {

		private boolean cronograma;

		private String nomeLote;

		public Item(String nomeLote) {
			this.nomeLote = nomeLote;
		}

		public String getNomeLote() {
			return nomeLote;
		}

		public boolean isCronograma() {
			return cronograma;
		}

		public void setCronograma(boolean cronograma) {
			this.cronograma = cronograma;
		}

		public void setNomeLote(String nomeLote) {
			this.nomeLote = nomeLote;
		}
	}

	private Comparator<Item> comparatorItem = new Comparator<Item>() {
		public int compare(Item i1, Item i2) {
			return i1.getNomeLote().compareTo(i2.getNomeLote());
		}
	};

	public CalcularFluxoCaixaCmd() {
	};

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRural result = (ProjetoCreditoRural) contexto.getRequisicao();

		// verificar se há cronogramas e se estão corretamente selecionados
		List<Item> investimentoNomeLote, custeioNomeLote;
		investimentoNomeLote = pegarNomeLote(result.getInvestimentoList());
		custeioNomeLote = pegarNomeLote(result.getCusteioList());

		if (investimentoNomeLote.size() == 0 && custeioNomeLote.size() == 0) {
			throw new BoException("Nenhum lote informado", "Erro ao calcular fluxo de caixa");
		}
		Collections.sort(investimentoNomeLote, comparatorItem);
		Collections.sort(custeioNomeLote, comparatorItem);

		nomeLoteTemCronograma(investimentoNomeLote, result.getCronogramaPagamentoInvestimentoList());
		nomeLoteTemCronograma(custeioNomeLote, result.getCronogramaPagamentoCusteioList());

		List<String> investimentoNomeLoteSemCronogrma = new ArrayList<>();
		investimentoNomeLote.forEach((v) -> {
			if (!v.cronograma) {
				investimentoNomeLoteSemCronogrma.add(v.getNomeLote());
			}
		});
		List<String> custeioNomeLoteSemCronogrma = new ArrayList<>();
		custeioNomeLote.forEach((v) -> {
			if (!v.cronograma) {
				custeioNomeLoteSemCronogrma.add(v.getNomeLote());
			}
		});
		if (investimentoNomeLoteSemCronogrma.size() > 0 || custeioNomeLoteSemCronogrma.size() > 0) {
			throw new BoException("Há lotes sem cronograma de pagamento | De investimento (" + investimentoNomeLoteSemCronogrma + ") | De custeio(" + custeioNomeLoteSemCronogrma + ")", "Erro ao calcular fluxo de caixa");
		}
		List<ProjetoCreditoRuralFluxoCaixa> fluxoCaixaList = new ArrayList<>();
		
		fluxoCaixaList.add(new ProjetoCreditoRuralFluxoCaixa(-1, FluxoCaixaTipo.R));
		fluxoCaixaList.add(new ProjetoCreditoRuralFluxoCaixa(-2, FluxoCaixaTipo.R));
		fluxoCaixaList.add(new ProjetoCreditoRuralFluxoCaixa(-3, FluxoCaixaTipo.D));
		fluxoCaixaList.add(new ProjetoCreditoRuralFluxoCaixa(-4, FluxoCaixaTipo.D));
		fluxoCaixaList.add(new ProjetoCreditoRuralFluxoCaixa(-5, FluxoCaixaTipo.D));
		
		result.setFluxoCaixaList(fluxoCaixaList);
		
		contexto.setResposta(result);

		return false;
	}

	private void nomeLoteTemCronograma(List<Item> nomeLoteList, List<ProjetoCreditoRuralCronogramaPagamento> cronogramaList) {
		if (nomeLoteList != null) {
			nomeLoteList.forEach((v) -> {
				v.setCronograma(false);
				if (cronogramaList != null) {
					for (int i = 0; i < cronogramaList.size(); i++) {
						if (v.getNomeLote().equals(cronogramaList.get(i).getNomeLote()) && Confirmacao.S.equals(cronogramaList.get(i).getSelecionado())) {
							v.setCronograma(true);
							break;
						}
					}
				}
			});
		}
	};

	private List<Item> pegarNomeLote(List<ProjetoCreditoRuralFinanciamento> lista) throws BoException {
		List<Item> result = new ArrayList<>();
		boolean encontrou, loteNaoIdentificado = false;
		if (lista != null) {
			for (ProjetoCreditoRuralFinanciamento v : lista) {
				if (StringUtils.isBlank(v.getNomeLote())) {
					loteNaoIdentificado = true;
				}
				encontrou = false;
				for (int i = 0; i < result.size(); i++) {
					if (v.getNomeLote().equals(result.get(i).getNomeLote())) {
						encontrou = true;
						break;
					}
				}
				if (!encontrou) {
					result.add(new Item(v.getNomeLote()));
				}
			}
			;
		}
		if (loteNaoIdentificado) {
			throw new BoException("Há lotes não identificados");
		}
		return result;
	}

}