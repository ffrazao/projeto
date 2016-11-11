package br.gov.df.emater.aterwebsrv.dto.funcional;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dao.funcional.EmpregoViDao;
import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

public class UnidadeOrganizacionalHierarquiaDto implements Dto {

	private static final long serialVersionUID = 1L;

	private List<EmpregoViDao> colaboradorList;

	private List<UnidadeOrganizacionalHierarquiaDto> descendenciaList;

	private Integer id;

	private UnidadeOrganizacional unidadeOrganizacional;

	public List<EmpregoViDao> getColaboradorList() {
		return colaboradorList;
	}

	public List<UnidadeOrganizacionalHierarquiaDto> getDescendenciaList() {
		return descendenciaList;
	}

	public Integer getId() {
		return id;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setColaboradorList(List<EmpregoViDao> colaboradorList) {
		this.colaboradorList = colaboradorList;
	}

	public void setDescendenciaList(List<UnidadeOrganizacionalHierarquiaDto> descendenciaList) {
		this.descendenciaList = descendenciaList;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

}