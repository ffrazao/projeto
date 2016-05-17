package br.gov.df.emater.aterwebsrv.modelo.pendencia;

import org.apache.poi.ss.formula.functions.T;

import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PendenciaTipo;

public class PendenciaImpl implements Pendencia<T> {

	private static final long serialVersionUID = 1L;

	private String codigo;

	private String descricao;

	private Integer id;

	private PendenciaTipo tipo;

	public PendenciaImpl(Integer id, String codigo, PendenciaTipo tipo, String descricao) {
		this.setId(id);
		this.setCodigo(codigo);
		this.setTipo(tipo);
		this.setDescricao(descricao);
	}

	public String getCodigo() {
		return codigo;
	}

	public String getDescricao() {
		return descricao;
	}

	public Integer getId() {
		return id;
	}

	public PendenciaTipo getTipo() {
		return tipo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setTipo(PendenciaTipo tipo) {
		this.tipo = tipo;
	}

	@Override
	public EntidadeBase getPendenciaDono() {
		return null;
	}

}
