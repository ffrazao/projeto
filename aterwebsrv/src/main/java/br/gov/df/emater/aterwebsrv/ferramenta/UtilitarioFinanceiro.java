package br.gov.df.emater.aterwebsrv.ferramenta;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Singleton utilitario para cálculos financeiros
public class UtilitarioFinanceiro {

	private static volatile UtilitarioFinanceiro instance;

	public static final UtilitarioFinanceiro getInstance() {
		if (instance == null) {
			synchronized (UtilitarioFinanceiro.class) {
				instance = new UtilitarioFinanceiro();
			}
		}
		return instance;
	}

	public static void main(String[] args) {
		for (Map<String, Object> l : UtilitarioFinanceiro.getInstance().tabelaPriceCalculaParcelas("1000", "3", 4)) {
			for (Map.Entry<String, Object> c : l.entrySet()) {
				System.out.format("[%s = %s]", c.getKey(), c.getValue());
			}
			System.out.format("\n");
		}
	}

	private UtilitarioFinanceiro() {
	}

	public List<Map<String, Object>> tabelaPriceCalculaParcelas(String valorPresenteStr, String taxaJurosStr, Integer totalParcelas) {
		List<Map<String, Object>> result = null;

		BigDecimal valorParcela = tabelaPriceCalculaValorParcela(valorPresenteStr, taxaJurosStr, totalParcelas);
		if (valorParcela == null) {
			return null;
		}

		BigDecimal taxaJuros = new BigDecimal(taxaJurosStr, MathContext.UNLIMITED).divide(new BigDecimal("100", MathContext.UNLIMITED), 10, RoundingMode.HALF_UP);
		BigDecimal saldoDevedor = new BigDecimal(valorPresenteStr, MathContext.UNLIMITED);
		BigDecimal juros = new BigDecimal("0", MathContext.UNLIMITED);
		BigDecimal amortizacao = new BigDecimal("0", MathContext.UNLIMITED);

		for (int parcela = 0; parcela < totalParcelas; parcela++) {
			juros = saldoDevedor.multiply(taxaJuros);
			amortizacao = valorParcela.subtract(juros, MathContext.UNLIMITED);
			saldoDevedor = saldoDevedor.subtract(amortizacao);
			if (result == null) {
				result = new ArrayList<>();
			}
			Map<String, Object> linha = new HashMap<>();
			linha.put("parcela", parcela + 1);
			linha.put("saldoDevedor", saldoDevedor);
			linha.put("valorParcela", valorParcela);
			linha.put("juros", juros);
			linha.put("amortizacao", amortizacao);
			result.add(linha);
		}

		return result;
	}

	public BigDecimal tabelaPriceCalculaValorParcela(String valorPresenteStr, String taxaJurosStr, Integer totalParcelas) {
		if (valorPresenteStr == null || taxaJurosStr == null || totalParcelas == null) {
			return null;
		}

		BigDecimal taxaJuros = new BigDecimal(taxaJurosStr, MathContext.UNLIMITED);
		taxaJuros = taxaJuros.divide(new BigDecimal("100", MathContext.UNLIMITED), 10, RoundingMode.HALF_UP);

		// BigDecimal result = valorPresente * ((Math.pow(1 + taxaJuros,
		// totalParcelas) * taxaJuros) / (Math.pow(1 + taxaJuros, totalParcelas)
		// - 1));

		BigDecimal multiplicador = new BigDecimal("1", MathContext.UNLIMITED).add(taxaJuros).pow(totalParcelas);

		BigDecimal result = new BigDecimal(valorPresenteStr, MathContext.UNLIMITED).multiply((multiplicador.multiply(taxaJuros)).divide(multiplicador.subtract(new BigDecimal("1", MathContext.UNLIMITED)), 10, RoundingMode.HALF_UP));

		return result;
	}
}