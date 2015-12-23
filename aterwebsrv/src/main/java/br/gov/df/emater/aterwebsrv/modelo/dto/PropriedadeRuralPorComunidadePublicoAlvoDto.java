package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.io.Serializable;
import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;

public class PropriedadeRuralPorComunidadePublicoAlvoDto implements Serializable  {

	private static final long serialVersionUID = 1L;

	private Comunidade comunidade;

	private List<PublicoAlvo> publicoAlvoList;

	public Comunidade getComunidade() {
		return comunidade;
	}

	public List<PublicoAlvo> getPublicoAlvoList() {
		return publicoAlvoList;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public void setPublicoAlvoList(List<PublicoAlvo> publicoAlvoList) {
		this.publicoAlvoList = publicoAlvoList;
	}

}
