package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.HashSet;
import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

class CalculoItem {

	private Producao producaoTotal = new Producao();

	private Set<Integer> publicoAlvoList = new HashSet<Integer>();

	public Producao getProducaoTotal() {
		return producaoTotal;
	}

	public Set<Integer> getPublicoAlvoList() {
		return publicoAlvoList;
	}

	public void setProducaoTotal(Producao producaoTotal) {
		this.producaoTotal = producaoTotal;
	}

	public void setPublicoAlvoList(Set<Integer> publicoAlvoList) {
		this.publicoAlvoList = publicoAlvoList;
	}

}