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

	@Override
	public String getCodigo() {
		return codigo;
	}

	@Override
	public String getDescricao() {
		return descricao;
	}

	@Override
	public Integer getId() {
		return id;
	}

	@Override
	public EntidadeBase getPendenciaDono() {
		return null;
	}

	@Override
	public PendenciaTipo getTipo() {
		return tipo;
	}

	@Override
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	@Override
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	@Override
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
	public void setTipo(PendenciaTipo tipo) {
		this.tipo = tipo;
	}

}
