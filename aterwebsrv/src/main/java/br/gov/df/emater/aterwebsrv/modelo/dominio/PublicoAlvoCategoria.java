package br.gov.df.emater.aterwebsrv.modelo.dominio;

public enum PublicoAlvoCategoria {

	E("Empreendedor", 1, new PublicoAlvoSegmento[] { PublicoAlvoSegmento.F, PublicoAlvoSegmento.P }, new PessoaTipo[] { PessoaTipo.PF, PessoaTipo.PJ }), H("Habitante", 2, new PublicoAlvoSegmento[] { PublicoAlvoSegmento.F, PublicoAlvoSegmento.P },
			new PessoaTipo[] { PessoaTipo.PF }), T("Trabalhador", 3, new PublicoAlvoSegmento[] { PublicoAlvoSegmento.F }, new PessoaTipo[] { PessoaTipo.PF });

	private String descricao;

	private Integer ordem;

	private PessoaTipo[] pessoaTipoList;

	private PublicoAlvoSegmento[] publicoAlvoSegmentoList;

	private PublicoAlvoCategoria(String descricao, Integer ordem, PublicoAlvoSegmento[] publicoAlvoSegmentoList, PessoaTipo[] pessoaTipoList) {
		this.descricao = descricao;
		this.ordem = ordem;
		this.publicoAlvoSegmentoList = publicoAlvoSegmentoList;
		this.pessoaTipoList = pessoaTipoList;
	}

	public String getDescricao() {
		return this.descricao;
	}

	public Integer getOrdem() {
		return this.ordem;
	}

	public PessoaTipo[] getPessoaTipoList() {
		return pessoaTipoList;
	}

	public PublicoAlvoSegmento[] getPublicoAlvoSegmentoList() {
		return this.publicoAlvoSegmentoList;
	}

	@Override
	public String toString() {
		return this.descricao;
	}

}