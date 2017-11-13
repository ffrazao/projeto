package br.gov.df.emater.aterwebsrv.dto.indice_producao;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

public class ProducaoCadDto extends CadFiltroDtoCustom {
	
	private static final long serialVersionUID = 1L;

	private List<Producao> producaoAgricolaList;

	private List<Producao> producaoAnimalList;

	private List<Producao> producaoFloriculturaList;

	private List<Producao> producaoList;

	private List<Producao> producaoNaoAgricolaList;

	public List<Producao> getProducaoAgricolaList() {
		return producaoAgricolaList;
	}

	public List<Producao> getProducaoAnimalList() {
		return producaoAnimalList;
	}

	public List<Producao> getProducaoFloriculturaList() {
		return producaoFloriculturaList;
	}

	public List<Producao> getProducaoList() {
		return producaoList;
	}

	public List<Producao> getProducaoNaoAgricolaList() {
		return producaoNaoAgricolaList;
	}

	public void setProducaoAgricolaList(List<Producao> producaoAgricolaList) {
		this.producaoAgricolaList = producaoAgricolaList;
	}
	public void setProducaoAnimalList(List<Producao> producaoAnimalList) {
		this.producaoAnimalList = producaoAnimalList;
	}
	public void setProducaoFloriculturaList(List<Producao> producaoFloriculturaList) {
		this.producaoFloriculturaList = producaoFloriculturaList;
	}

	public void setProducaoList(List<Producao> producaoList) {
		this.producaoList = producaoList;
	}

	public void setProducaoNaoAgricolaList(List<Producao> producaoNaoAgricolaList) {
		this.producaoNaoAgricolaList = producaoNaoAgricolaList;
	}

}
