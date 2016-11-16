package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.util.Calendar;

public interface UnidadeOrganizacionalHierarquiaBase {

	public UnidadeOrganizacional getAscendente();

	public UnidadeOrganizacional getDescendente();

	public Integer getId();

	public Calendar getInicio();

	public Calendar getTermino();

	public void setAscendente(UnidadeOrganizacional ascendente);

	public void setDescendente(UnidadeOrganizacional descendente);

	public void setId(Integer id);

	public void setInicio(Calendar inicio);

	public void setTermino(Calendar termino);

}