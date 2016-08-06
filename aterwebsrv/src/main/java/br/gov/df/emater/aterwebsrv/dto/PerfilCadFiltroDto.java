package br.gov.df.emater.aterwebsrv.dto;

import java.util.Set;

public class PerfilCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Set<TagDto> comandoNome;

	private Set<TagDto> funcionalidadeNome;

	private Set<TagDto> perfilCodigo;

	private Set<TagDto> perfilNome;

	public Set<TagDto> getComandoNome() {
		return comandoNome;
	}

	public Set<TagDto> getFuncionalidadeNome() {
		return funcionalidadeNome;
	}

	public Set<TagDto> getPerfilCodigo() {
		return perfilCodigo;
	}

	public Set<TagDto> getPerfilNome() {
		return perfilNome;
	}

	public void setComandoNome(Set<TagDto> comandoNome) {
		this.comandoNome = comandoNome;
	}

	public void setFuncionalidadeNome(Set<TagDto> funcionalidadeNome) {
		this.funcionalidadeNome = funcionalidadeNome;
	}

	public void setPerfilCodigo(Set<TagDto> perfilCodigo) {
		this.perfilCodigo = perfilCodigo;
	}

	public void setPerfilNome(Set<TagDto> perfilNome) {
		this.perfilNome = perfilNome;
	}

}