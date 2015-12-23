package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.io.Serializable;
import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Bem;

public class PropriedadeRuralPorComunidadePublicoAlvoDto implements Serializable {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	private Bem bem;

	private Comunidade comunidade;

	private List<PublicoAlvo> publicoAlvoList;

	public Integer getAno() {
		return ano;
	}

	public Bem getBem() {
		return bem;
	}

	public Comunidade getComunidade() {
		return comunidade;
	}

	public List<PublicoAlvo> getPublicoAlvoList() {
		return publicoAlvoList;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setBem(Bem bem) {
		this.bem = bem;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
	}

	public void setPublicoAlvoList(List<PublicoAlvo> publicoAlvoList) {
		this.publicoAlvoList = publicoAlvoList;
	}

}
