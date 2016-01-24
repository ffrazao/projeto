package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;

public class IndiceProducaoCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	private Set<Confirmacao> confirmado;

	private Integer id;

	public IndiceProducaoCadFiltroDto() {

	}

	public Integer getAno() {
		return ano;
	}

	public Set<Confirmacao> getConfirmado() {
		return confirmado;
	}

	public Integer getId() {
		return id;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setConfirmado(Set<Confirmacao> confirmado) {
		this.confirmado = confirmado;
	}

	public void setId(Integer id) {
		this.id = id;
	}

}