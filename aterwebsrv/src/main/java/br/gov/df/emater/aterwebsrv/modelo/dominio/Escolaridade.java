package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum Escolaridade {
	AC("Sabe ler / escrever (Alfabetizado)", 2), AI("N�o sabe ler / escrever (Analfabeto)", 1), DO("Doutorado", 11), ES("Especializa��o/ Resid�ncia", 9), FC("Fundamental Completo", 4), FI("Fundamental Incompleto", 3), MC("M�dio Completo", 6), ME("Mestrado", 10), MI(
			"M�dio Incompleto", 5), PD("P�s-Doutorado", 12), SC("Superior Completo", 8), SI("Superior Incompleto", 7);

	private String descricao;

	private Integer ordem;

	private Escolaridade(String descricao, Integer ordem) {
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
