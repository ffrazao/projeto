package br.gov.df.emater.aterwebsrv.dto;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaSituacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.UsuarioStatusConta;

public class UsuarioCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Set<TagDto> pessoaNome;

	private Set<PessoaSituacao> pessoaSituacao;

	private Set<TagDto> usuarioEmail;

	private Set<TagDto> usuarioNome;

	private Set<UsuarioStatusConta> usuarioSituacao;

	public UsuarioCadFiltroDto() {

	}

	public Set<TagDto> getPessoaNome() {
		return pessoaNome;
	}

	public Set<PessoaSituacao> getPessoaSituacao() {
		return pessoaSituacao;
	}

	public Set<TagDto> getUsuarioEmail() {
		return usuarioEmail;
	}

	public Set<TagDto> getUsuarioNome() {
		return usuarioNome;
	}

	public Set<UsuarioStatusConta> getUsuarioSituacao() {
		return usuarioSituacao;
	}

	public void setPessoaNome(Set<TagDto> pessoaNome) {
		this.pessoaNome = pessoaNome;
	}

	public void setPessoaSituacao(Set<PessoaSituacao> pessoaSituacao) {
		this.pessoaSituacao = pessoaSituacao;
	}

	public void setUsuarioEmail(Set<TagDto> usuarioEmail) {
		this.usuarioEmail = usuarioEmail;
	}

	public void setUsuarioNome(Set<TagDto> usuarioNome) {
		this.usuarioNome = usuarioNome;
	}

	public void setUsuarioSituacao(Set<UsuarioStatusConta> usuarioSituacao) {
		this.usuarioSituacao = usuarioSituacao;
	}

}