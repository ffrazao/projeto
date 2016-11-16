package br.gov.df.emater.aterwebsrv.modelo.funcional;

import java.util.Calendar;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

public abstract interface LotacaoBase {

	public Emprego getEmprego();

	public Confirmacao getGestor();

	public Calendar getInicio();

	public Confirmacao getTemporario();

	public Calendar getTermino();

	public UnidadeOrganizacional getUnidadeOrganizacional();

	public void setEmprego(Emprego emprego);

	public void setGestor(Confirmacao gestor);

	public void setInicio(Calendar inicio);

	public void setTemporario(Confirmacao temporario);

	public void setTermino(Calendar termino);

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional);

}