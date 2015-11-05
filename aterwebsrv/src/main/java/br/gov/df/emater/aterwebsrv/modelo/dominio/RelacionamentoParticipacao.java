package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum RelacionamentoParticipacao {

	A("Ativo", 1), N("Neutro", 2), P("Passivo", 3);

	private String descricao;

	private Integer ordem;

	private RelacionamentoParticipacao(String descricao, Integer ordem) {
		this.descricao = descricao;
		this.ordem = ordem;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}