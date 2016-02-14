package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PropriedadeRuralVinculoTipo {

	AR("Arrendamento", 5), HA("Habitante", 3), PA("Parceria", 2), PR("Proprietário", 1), TR("Trabalhador", 4);

	private String descricao;

	private Integer ordem;

	private PropriedadeRuralVinculoTipo(String descricao, Integer ordem) {
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
