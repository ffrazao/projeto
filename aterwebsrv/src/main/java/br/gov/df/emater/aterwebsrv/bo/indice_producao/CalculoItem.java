package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.HashSet;
import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;

class CalculoItem {

	private ProducaoForma producaoFormaTotal = new ProducaoForma();

	private Set<Integer> publicoAlvoList = new HashSet<Integer>();

	public ProducaoForma getProducaoFormaTotal() {
		return producaoFormaTotal;
	}

	public Set<Integer> getPublicoAlvoList() {
		return publicoAlvoList;
	}

	public void setProducaoFormaTotal(ProducaoForma producaoFormaTotal) {
		this.producaoFormaTotal = producaoFormaTotal;
	}

	public void setPublicoAlvoList(Set<Integer> publicoAlvoList) {
		this.publicoAlvoList = publicoAlvoList;
	}

}