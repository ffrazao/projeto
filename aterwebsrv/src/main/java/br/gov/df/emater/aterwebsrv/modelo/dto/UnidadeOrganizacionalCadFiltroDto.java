package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;

public class UnidadeOrganizacionalCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String nome;

	private Set<UnidadeOrganizacionalClassificacao> classificacao;

	public String getNomeLike() {
		return String.format( "%%%s%%", this.nome);
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<UnidadeOrganizacionalClassificacao> getClassificacao() {
		return classificacao;
	}

	public void setClassificacao(Set<UnidadeOrganizacionalClassificacao> classificacao) {
		this.classificacao = classificacao;
	}

}