package br.gov.df.emater.aterwebsrv.dto.funcional;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;

public class ColaboradorListaCadFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String nome;

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

}