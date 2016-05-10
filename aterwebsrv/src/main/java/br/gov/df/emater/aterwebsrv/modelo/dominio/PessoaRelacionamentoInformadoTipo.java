package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaRelacionamentoInformadoTipo {

	C("Cônjuge", 1), P("Pai", 2), M("Mãe", 3);

	private String descricao;

	private Integer ordem;

	private PessoaRelacionamentoInformadoTipo(String descricao, Integer ordem) {
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