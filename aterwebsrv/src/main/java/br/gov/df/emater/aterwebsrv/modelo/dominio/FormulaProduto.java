package br.gov.df.emater.aterwebsrv.modelo.dominio;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ExecutionException;

import br.gov.df.emater.aterwebsrv.ferramenta.UtilitarioNumero;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public enum FormulaProduto {

	A("A", 1), B("A * B", 2), C("A * B", 3);

	private String descricao;

	private Integer ordem;

	private FormulaProduto(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

	public BigDecimal volume(Producao producaoForma) throws InterruptedException, ExecutionException {
		// ver detalhes em http://projects.congrace.de/exp4j/index.html
		List<String> variaveis = new ArrayList<String>();
		if (producaoForma.getItemAValor() != null) {
			variaveis.add("A");
		}
		if (producaoForma.getItemBValor() != null) {
			variaveis.add("B");
		}
		if (producaoForma.getItemCValor() != null) {
			variaveis.add("C");
		}
		if (variaveis.size() == 0) {
			return null;
		}
		Expression e = new ExpressionBuilder(this.descricao).variables(new HashSet<String>(variaveis)).build();
		if (producaoForma.getItemAValor() != null) {
			e.setVariable("A", producaoForma.getItemAValor().doubleValue());
		}
		if (producaoForma.getItemBValor() != null) {
			e.setVariable("B", producaoForma.getItemBValor().doubleValue());
		}
		if (producaoForma.getItemCValor() != null) {
			e.setVariable("C", producaoForma.getItemCValor().doubleValue());
		}
		
		Double result = null;
		result = e.evaluate();

		/*
		ExecutorService exec = Executors.newFixedThreadPool(1);
		Future<Double> future = e.evaluateAsync(exec);
		result = future.get();
		*/

		return new BigDecimal(result, UtilitarioNumero.BIG_DECIMAL_PRECISAO);
	}
}
