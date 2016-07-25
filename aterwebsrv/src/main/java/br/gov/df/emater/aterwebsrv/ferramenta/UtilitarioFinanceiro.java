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

	public List<Map<String, Object>> tabelaPriceCalculaParcelas(String valorPresenteStr, String taxaJurosStr, Integer totalParcelas) {
		List<Map<String, Object>> result = null;

		BigDecimal valorParcela = tabelaPriceCalculaValorParcela(valorPresenteStr, taxaJurosStr, totalParcelas);
		if (valorParcela == null) {
			return null;
		}

		BigDecimal taxaJuros = big(taxaJurosStr).divide(big("100"), 10, RoundingMode.HALF_UP);
		BigDecimal saldoDevedor = big(valorPresenteStr);
		BigDecimal saldoDevedorAnterior;
		BigDecimal juros = big();
		BigDecimal amortizacao = big();

		for (int parcela = 0; parcela < totalParcelas; parcela++) {
			juros = saldoDevedor.multiply(taxaJuros).setScale(2, BigDecimal.ROUND_HALF_UP);
			amortizacao = valorParcela.subtract(juros, MathContext.UNLIMITED);
			saldoDevedorAnterior = saldoDevedor;
			saldoDevedor = saldoDevedor.subtract(amortizacao);
			if (result == null) {
				result = new ArrayList<>();
			}
			Map<String, Object> linha = new HashMap<>();
			linha.put("parcela", parcela + 1);
			linha.put("saldoDevedorAnterior", saldoDevedorAnterior);
			linha.put("saldoDevedor", saldoDevedor);
			linha.put("valorParcela", valorParcela);
			linha.put("juros", juros);
			linha.put("amortizacao", amortizacao);
			result.add(linha);
		}

		// jogar o eventual resquício do saldo devedor para a última parcela
		if (!CollectionUtils.isEmpty(result) && !saldoDevedor.equals(big())) {
			result.get(result.size() - 1).put("amortizacao", ((BigDecimal) result.get(result.size() - 1).get("amortizacao")).add(saldoDevedor));
			result.get(result.size() - 1).put("saldoDevedor", big());
		}

		return result;
	}

	private BigDecimal big() {
		return big(null);
	}

	private BigDecimal big(String valor) {
		return new BigDecimal(valor == null ? "0" : valor, MathContext.UNLIMITED);
	}

	public BigDecimal tabelaPriceCalculaValorParcela(String valorPresenteStr, String taxaJurosStr, Integer totalParcelas) {
		if (valorPresenteStr == null || taxaJurosStr == null || totalParcelas == null || totalParcelas < 1) {
			return null;
		}

		BigDecimal taxaJuros = big(taxaJurosStr);
		taxaJuros = taxaJuros.divide(big("100"), 10, RoundingMode.HALF_UP);

		BigDecimal multiplicador = big("1").add(taxaJuros).pow(totalParcelas);

		BigDecimal result = big(valorPresenteStr).multiply((multiplicador.multiply(taxaJuros)).divide(multiplicador.subtract(big("1")), 10, RoundingMode.HALF_UP));

		return result.setScale(2, BigDecimal.ROUND_HALF_UP);
	}
}