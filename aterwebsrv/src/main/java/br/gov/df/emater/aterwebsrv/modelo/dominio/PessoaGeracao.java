package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PessoaGeracao {

	A("Adulto", 3, 18, 60), C("Criança", 1, 0, 11), I("Idoso", 4, 61, 130), J("Jovem", 2, 12, 17);

	private String descricao;

	private Integer idadeFim;

	private Integer idadeInicio;

	private Integer ordem;

	private PessoaGeracao(String descricao, int ordem, Integer idadeInicio, Integer idadeFim) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.idadeInicio = idadeInicio;
		this.idadeFim = idadeFim;
	}

	public Integer getIdadeFim() {
		return idadeFim;
	}

	public Integer getIdadeInicio() {
		return idadeInicio;
	}

	public Integer getOrdem() {
		return ordem;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}