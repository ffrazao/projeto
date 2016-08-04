package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum GarantiaParticipacao {

	PA("Primeiro Avalista", 1),
	CPA("Cônjute do Primeiro Avalista", 2),
	SA("Segundo Avalista", 3),
	CSA("Cônjuge do Segundo Avalista", 4);

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
