package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum AtividadeSituacao {

	B("Bloqueada", 5, false, false), C("Conclu�da", 3, true, true), E("Em Execu��o", 2, true, true), N("N�o Iniciada", 1, true, false), S("Suspensa", 6, false, true), X("Cancelada", 4, false, true);

	private String descricao;

	private Boolean inicial;

	private Boolean ocorrencia;

	private Integer ordem;

	private AtividadeSituacao(String descricao, Integer ordem, Boolean inicial, Boolean ocorrencia) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.inicial = inicial;
		this.ocorrencia = ocorrencia;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Boolean getInicial() {
		return this.inicial;
	}

	public Boolean getOcorrencia() {
		return this.ocorrencia;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}