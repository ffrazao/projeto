package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaGenero {

	F("Feminino", 2), M("Masculino", 1);

	private String descricao;

	private Integer ordem;

	private PessoaGenero(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}