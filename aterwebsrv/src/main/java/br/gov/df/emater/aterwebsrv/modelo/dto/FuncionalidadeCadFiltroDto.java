package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Set;

public class FuncionalidadeCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Set<TagDto> comando;

	private Set<TagDto> funcionalidade;

	private Set<TagDto> modulo;

	public Set<TagDto> getComando() {
		return comando;
	}

	public Set<TagDto> getFuncionalidade() {
		return funcionalidade;
	}

	public Set<TagDto> getModulo() {
		return modulo;
	}

	public void setComando(Set<TagDto> comando) {
		this.comando = comando;
	}

	public void setFuncionalidade(Set<TagDto> funcionalidade) {
		this.funcionalidade = funcionalidade;
	}

	public void setModulo(Set<TagDto> modulo) {
		this.modulo = modulo;
	}

}