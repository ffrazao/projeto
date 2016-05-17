package br.gov.df.emater.aterwebsrv.modelo.pendencia;

import java.util.List;

public interface Pendenciavel<T extends Pendencia<?>> {

	public List<T> getPendenciaList();

	public void setPendenciaList(List<T> pendenciaList);

}
