package br.gov.df.emater.aterwebsrv.dto.funcional;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Empregador;

public class ColaboradorListaDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Empregador empregador;

	private List<OrganogramaDto> organogramaList;

	public Empregador getEmpregador() {
		return empregador;
	}

	public void setEmpregador(Empregador empregador) {
		this.empregador = empregador;
	}

	public List<OrganogramaDto> getOrganogramaList() {
		return organogramaList;
	}

	public void setOrganogramaList(List<OrganogramaDto> organogramaList) {
		this.organogramaList = organogramaList;
	}

}