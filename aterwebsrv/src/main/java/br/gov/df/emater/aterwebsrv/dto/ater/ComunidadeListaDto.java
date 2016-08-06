package br.gov.df.emater.aterwebsrv.dto.ater;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;

public class ComunidadeListaDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String nome;

	private Set<Integer> pessoaJuridicaList;

	private Set<Integer> unidadeOrganizacionalList;

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

	public Set<Integer> getPessoaJuridicaList() {
		return pessoaJuridicaList;
	}

	public Set<Integer> getUnidadeOrganizacionalList() {
		return unidadeOrganizacionalList;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public void setPessoaJuridicaList(Set<Integer> pessoaJuridicaList) {
		this.pessoaJuridicaList = pessoaJuridicaList;
	}

	public void setUnidadeOrganizacionalList(Set<Integer> unidadeOrganizacionalList) {
		this.unidadeOrganizacionalList = unidadeOrganizacionalList;
	}

}