package br.gov.df.emater.aterwebsrv.ferramenta;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;

// Singleton utilitario para cálculos financeiros
// fonte: https://pt.wikipedia.org/wiki/Tabela_Price
public class UtilitarioFinanceiro {

	private static volatile UtilitarioFinanceiro instance;

	public static enum Periodicidade {
		AO_DIA(1, new BigDecimal("1").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("7").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("30").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP),
				new BigDecimal("60").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("90").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("180").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP),
				new BigDecimal("360").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP)), A_SEMANA(2, new BigDecimal("1").divide(new BigDecimal("7"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP),
						new BigDecimal("30").divide(new BigDecimal("7"), 10, RoundingMode.HALF_UP), new BigDecimal("60").divide(new BigDecimal("7"), 10, RoundingMode.HALF_UP), new BigDecimal("90").divide(new BigDecimal("7"), 10, RoundingMode.HALF_UP),
						new BigDecimal("180").divide(new BigDecimal("7"), 10, RoundingMode.HALF_UP), new BigDecimal("360").divide(new BigDecimal("7"), 10, RoundingMode.HALF_UP)), AO_MES(3, new BigDecimal("1").divide(new BigDecimal("30"), 10, RoundingMode.HALF_UP),
								new BigDecimal("7").divide(new BigDecimal("30"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("2").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP),
								new BigDecimal("3").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("6").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP),
								new BigDecimal("12").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP)), AO_BIMESTRE(4, new BigDecimal("1").divide(new BigDecimal("60"), 10, RoundingMode.HALF_UP), new BigDecimal("7").divide(new BigDecimal("60"), 10, RoundingMode.HALF_UP),
										new BigDecimal("1").divide(new BigDecimal("2"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("3").divide(new BigDecimal("2"), 10, RoundingMode.HALF_UP),
										new BigDecimal("3").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("6").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP)), AO_TRIMESTRE(5,
												new BigDecimal("1").divide(new BigDecimal("90"), 10, RoundingMode.HALF_UP), new BigDecimal("7").divide(new BigDecimal("90"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP),
												new BigDecimal("2").divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP), new BigDecimal("2").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP),
												new BigDecimal("4").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP)), AO_SEMESTRE(6, new BigDecimal("1").divide(new BigDecimal("180"), 10, RoundingMode.HALF_UP),
														new BigDecimal("1").divide(new BigDecimal("26"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("6"), 10, RoundingMode.HALF_UP),
														new BigDecimal("1").divide(new BigDecimal("3"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("2"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP),
														new BigDecimal("2").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP)), AO_ANO(7, new BigDecimal("1").divide(new BigDecimal("360"), 10, RoundingMode.HALF_UP),
																new BigDecimal("1").divide(new BigDecimal("52"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("12"), 10, RoundingMode.HALF_UP),
																new BigDecimal("1").divide(new BigDecimal("6"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("4"), 10, RoundingMode.HALF_UP),
																new BigDecimal("1").divide(new BigDecimal("2"), 10, RoundingMode.HALF_UP), new BigDecimal("1").divide(new BigDecimal("1"), 10, RoundingMode.HALF_UP));

		private int ordem;

		private BigDecimal fatorEmDias;
		private BigDecimal fatorEmSemanas;
		private BigDecimal fatorEmMeses;
		private BigDecimal fatorEmBimestres;
		private BigDecimal fatorEmTrimestres;
		private BigDecimal fatorEmSemestres;
		private BigDecimal fatorEmAnos;

		private Periodicidade(int ordem, BigDecimal totalEmDias, BigDecimal totalEmSemanas, BigDecimal totalEmMeses, BigDecimal totalEmBimestres, BigDecimal totalEmTrimestres, BigDecimal totalEmSemestres, BigDecimal totalEmAnos) {
			this.ordem = ordem;
			this.fatorEmDias = totalEmDias;
			this.fatorEmSemanas = totalEmSemanas;
			this.fatorEmMeses = totalEmMeses;
			this.fatorEmBimestres = totalEmBimestres;
			this.fatorEmTrimestres = totalEmTrimestres;
			this.fatorEmSemestres = totalEmSemestres;
			this.fatorEmAnos = totalEmAnos;
		}

		public int getOrdem() {
			return ordem;
		}

		public BigDecimal getFatorEmDias() {
			return fatorEmDias;
		}

		public BigDecimal getFatorEmSemanas() {
			return fatorEmSemanas;
		}

		public BigDecimal getFatorEmMeses() {
			return fatorEmMeses;
		}

		public BigDecimal getFatorEmBimestres() {
			return fatorEmBimestres;
		}

		public BigDecimal getFatorEmTrimestres() {
			return fatorEmTrimestres;
		}

		public BigDecimal getFatorEmSemestres() {
			return fatorEmSemestres;
		}

		public BigDecimal getFatorEmAnos() {
			return fatorEmAnos;
		}

	};

	public static final UtilitarioFinanceiro getInstance() {
		if (instance == null) {
			synchronized (UtilitarioFinanceiro.class) {
				instance = new UtilitarioFinanceiro();
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		for (Map<String, Object> l : UtilitarioFinanceiro.getInstance().tabelaPriceCalculaParcelas("109047.62", "2", 5)) {
			for (Map.Entry<String, Object> c : l.entrySet()) {
				System.out.format("[%s = %s]", c.getKey(), c.getValue());
				// if (c.getKey().equals("amortizacao")) {
				// System.out.format("%f", c.getValue());
				// }
			}
			System.out.format("\n");
		}
	}

	private UtilitarioFinanceiro() {
	}

	private BigDecimal big() {
		return big(null);
	}

	private BigDecimal big(String valor) {
		return new BigDecimal(valor == null ? "0" : valor, MathContext.UNLIMITED);
	}

	public List<Map<String, Object>> tabelaPriceCalculaParcelas(BigDecimal valorPresente, BigDecimal taxaJuros, Integer totalParcelas) {
		List<Map<String, Object>> result = null;

		BigDecimal prestacao = tabelaPriceCalculaPrestacao(valorPresente, taxaJuros, totalParcelas);
		if (prestacao == null) {
			return null;
		}

		taxaJuros = taxaJuros.divide(big("100"), 10, RoundingMode.HALF_UP);
		BigDecimal saldoDevedorFinal = valorPresente;
		BigDecimal saldoDevedorInicial = big();
		BigDecimal juros = big();
		BigDecimal amortizacao = big();

		for (int parcela = 0; parcela < totalParcelas; parcela++) {
			juros = saldoDevedorFinal.multiply(taxaJuros).setScale(2, RoundingMode.HALF_UP);
			amortizacao = prestacao.subtract(juros);
			saldoDevedorInicial = saldoDevedorFinal;
			saldoDevedorFinal = saldoDevedorFinal.subtract(amortizacao);
			if (result == null) {
				result = new ArrayList<>();
			}
			Map<String, Object> linha = new HashMap<>();
			linha.put("parcela", parcela + 1);
			linha.put("saldoDevedorInicial", saldoDevedorInicial.setScale(2, RoundingMode.HALF_UP));
			linha.put("prestacao", prestacao.setScale(2, RoundingMode.HALF_UP));
			linha.put("taxaJuros", taxaJuros.setScale(10, RoundingMode.HALF_UP));
			linha.put("juros", juros.setScale(2, RoundingMode.HALF_UP));
			linha.put("amortizacao", amortizacao.setScale(2, RoundingMode.HALF_UP));
			linha.put("saldoDevedorFinal", saldoDevedorFinal.setScale(2, RoundingMode.HALF_UP));
			
			result.add(linha);
		}

		// jogar o eventual resquício do saldo devedor para a última parcela
		if (!CollectionUtils.isEmpty(result) && !saldoDevedorFinal.equals(big())) {
			// result.get(result.size() - 1).put("amortizacao",
			// amortizacao.add(saldoDevedorFinal).setScale(2, RoundingMode.HALF_UP));
			// result.get(result.size() - 1).put("saldoDevedorInicial",
			// saldoDevedorInicial.add(saldoDevedorFinal).setScale(2,
			// RoundingMode.HALF_UP));
			result.get(result.size() - 1).put("saldoDevedorFinal", big().setScale(2, RoundingMode.HALF_UP));
		}

		return result;
	}

	public List<Map<String, Object>> tabelaPriceCalculaParcelas(String valorPresenteStr, String taxaJurosStr, Integer totalParcelas) {
		return tabelaPriceCalculaParcelas(big(valorPresenteStr), big(taxaJurosStr), totalParcelas);
	}

	public BigDecimal tabelaPriceCalculaPrestacao(BigDecimal valorPresente, BigDecimal taxaJuros, Integer totalParcelas) {
		if (valorPresente == null || taxaJuros == null || totalParcelas == null || totalParcelas < 1) {
			return null;
		}

		taxaJuros = taxaJuros.divide(big("100"), 10, RoundingMode.HALF_UP);

		BigDecimal multiplicador = (big("1").add(taxaJuros)).pow(totalParcelas * -1, MathContext.DECIMAL64);

		BigDecimal result = valorPresente.multiply(taxaJuros.divide(big("1").subtract(multiplicador), 10, RoundingMode.HALF_UP));

		return result.setScale(2, BigDecimal.ROUND_HALF_UP);
	}

	public BigDecimal tabelaPriceCalculaPrestacao(String valorPresenteStr, String taxaJurosStr, Integer totalParcelas) {
		return tabelaPriceCalculaPrestacao(big(valorPresenteStr), big(taxaJurosStr), totalParcelas);
	}

	/**
	 * Converte o tempo da taxa fonte:
	 * http://fazaconta.com/taxa-mensal-vs-anual.htm
	 * 
	 * @param taxaJuros
	 * @param fatorConversao
	 * @return
	 */
	public BigDecimal converteTempoTaxa(BigDecimal taxaJuros, BigDecimal fatorConversao) {
		
		// POTÊNCIA(1 + 3%; 1/12) - 1
		taxaJuros = taxaJuros.divide(new BigDecimal("100"), 10, RoundingMode.HALF_UP);
		BigDecimal base = new BigDecimal("1").add(taxaJuros);
		BigDecimal result = new BigDecimal(Math.pow(base.doubleValue(), fatorConversao.doubleValue()), MathContext.DECIMAL64);
		result = result.subtract(big("1"));
		result = result.multiply(big("100"));
		return result.setScale(10, RoundingMode.HALF_UP);
	}
}