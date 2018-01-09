package br.gov.df.emater.aterwebsrv.dto.pessoa;


import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;

public class DeclaracaoCeasaProdutoRelDto {
	
	private Integer id;
	
	private String unidade;
	
	private BemClassificado cultura;
	
	private String area;
	
	private String colheita;
	
	private String producao;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUnidade() {
		return unidade;
	}

	public void setUnidade(String unidade) {
		this.unidade = unidade;
	}

	public BemClassificado getCultura() {
		return cultura;
	}

	public void setCultura(BemClassificado cultura) {
		this.cultura = cultura;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getColheita() {
		return colheita;
	}

	public void setColheita(String colheita) {
		this.colheita = colheita;
	}

	public String getProducao() {
		return producao;
	}

	public void setProducao(String producao) {
		this.producao = producao;
	}
	
	
	
	

}
