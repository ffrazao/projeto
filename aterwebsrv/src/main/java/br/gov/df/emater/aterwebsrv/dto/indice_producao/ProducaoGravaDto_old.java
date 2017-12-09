package br.gov.df.emater.aterwebsrv.dto.indice_producao;

import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoProprietario;

public class ProducaoGravaDto_old  {
	
	private static final long serialVersionUID = 1L;

	private ProducaoProprietario ProducaoProprietario;
	
	private List<Object> producaoAgricolaList;

	private List<Object> producaoAnimalList;

	private List<Object> producaoFloriculturaList;

	private List<Object> producaoList;

	private List<Object> producaoNaoAgricolaList;

	public List<Object> getProducaoAgricolaList() {
		return producaoAgricolaList;
	}

	public List<Object> getProducaoAnimalList() {
		return producaoAnimalList;
	}

	public List<Object> getProducaoFloriculturaList() {
		return producaoFloriculturaList;
	}

	public List<Object> getProducaoList() {
		return producaoList;
	}

	public List<Object> getProducaoNaoAgricolaList() {
		return producaoNaoAgricolaList;
	}

	public void setProducaoAgricolaList(List<Object> producaoAgricolaList) {
		this.producaoAgricolaList = producaoAgricolaList;
	}
	public void setProducaoAnimalList(List<Object> producaoAnimalList) {
		this.producaoAnimalList = producaoAnimalList;
	}
	public void setProducaoFloriculturaList(List<Object> producaoFloriculturaList) {
		this.producaoFloriculturaList = producaoFloriculturaList;
	}

	public void setProducaoList(List<Object> producaoList) {
		this.producaoList = producaoList;
	}

	public void setProducaoNaoAgricolaList(List<Object> producaoNaoAgricolaList) {
		this.producaoNaoAgricolaList = producaoNaoAgricolaList;
	}

	public ProducaoProprietario getProducaoProprietario() {
		return ProducaoProprietario;
	}

	public void setProducaoProprietario(ProducaoProprietario producaoProprietario) {
		ProducaoProprietario = producaoProprietario;
	}

}
