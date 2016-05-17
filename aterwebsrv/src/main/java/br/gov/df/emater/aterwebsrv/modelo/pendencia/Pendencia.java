package br.gov.df.emater.aterwebsrv.modelo.pendencia;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo._ChavePrimaria;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaTipo;

public interface Pendencia<T> extends _ChavePrimaria<Integer> {

	String getCodigo();

	String getDescricao();

	EntidadeBase getPendenciaDono();

	PendenciaTipo getTipo();

	void setCodigo(String codigo);

	void setDescricao(String descricao);

	void setTipo(PendenciaTipo tipo);

}