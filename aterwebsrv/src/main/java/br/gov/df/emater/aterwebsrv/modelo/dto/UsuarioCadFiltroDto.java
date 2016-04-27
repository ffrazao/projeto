package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Set;

public class UsuarioCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Set<TagDto> pessoaNome;

	private Set<TagDto> usuarioNome;

	public UsuarioCadFiltroDto() {

	}

	public Set<TagDto> getPessoaNome() {
		return pessoaNome;
	}

	public void setPessoaNome(Set<TagDto> pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public Set<TagDto> getUsuarioNome() {
		return usuarioNome;
	}

	public void setUsuarioNome(Set<TagDto> usuarioNome) {
		this.usuarioNome = usuarioNome;
	}

}