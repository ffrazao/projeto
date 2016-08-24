package br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.Dto;

public class ProjetoTecnicoGarantiaRelDto implements Dto {

	private static final long serialVersionUID = 1L;

	private String garantia;

	private List<ProjetoTecnicoGarantiaAvalistaRelDto> garantiaAvalistaList;

	public String getGarantia() {
		return garantia;
	}

	public List<ProjetoTecnicoGarantiaAvalistaRelDto> getGarantiaAvalistaList() {
		return garantiaAvalistaList;
	}

	public void setGarantia(String garantia) {
		this.garantia = garantia;
	}

	public void setGarantiaAvalistaList(List<ProjetoTecnicoGarantiaAvalistaRelDto> garantiaAvalistaList) {
		this.garantiaAvalistaList = garantiaAvalistaList;
	}

}
