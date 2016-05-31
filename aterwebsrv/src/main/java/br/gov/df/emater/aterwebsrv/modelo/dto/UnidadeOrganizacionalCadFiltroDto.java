package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.dominio.UnidadeOrganizacionalClassificacao;

public class UnidadeOrganizacionalCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Set<UnidadeOrganizacionalClassificacao> classificacao;

	private String nome;

	public Set<UnidadeOrganizacionalClassificacao> getClassificacao() {
		return classificacao;
	}

	public String getNome() {
		return nome;
	}

	public String getNomeLike() {
		if (this.nome != null) {
			return String.format("%%%s%%", this.nome);
		} else {
			return "%";
		}
	}

	public void setClassificacao(Set<UnidadeOrganizacionalClassificacao> classificacao) {
		this.classificacao = classificacao;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}