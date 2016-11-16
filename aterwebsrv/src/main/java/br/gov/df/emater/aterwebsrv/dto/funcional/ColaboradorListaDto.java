package br.gov.df.emater.aterwebsrv.dto.funcional;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.funcional.Empregador;

public class ColaboradorListaDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Empregador empregador;

	private List<UnidadeOrganizacionalListaDto> unidadeOrganizacionalList;

	public Empregador getEmpregador() {
		return empregador;
	}

	public List<UnidadeOrganizacionalListaDto> getUnidadeOrganizacionalList() {
		return unidadeOrganizacionalList;
	}

	public void setEmpregador(Empregador empregador) {
		this.empregador = empregador;
	}

	public void setUnidadeOrganizacionalList(List<UnidadeOrganizacionalListaDto> unidadeOrganizacionalList) {
		this.unidadeOrganizacionalList = unidadeOrganizacionalList;
	}

}