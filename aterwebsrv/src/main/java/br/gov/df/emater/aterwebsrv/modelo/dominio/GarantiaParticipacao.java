package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum GarantiaParticipacao {

	C("", 3), P("", 1), S("", 2);

	private String descricao;

	private Integer ordem;

	private GarantiaParticipacao(String descricao, Integer ordem) {
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
