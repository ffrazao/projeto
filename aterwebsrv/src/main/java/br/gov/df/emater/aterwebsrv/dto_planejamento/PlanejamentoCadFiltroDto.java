package br.gov.df.emater.aterwebsrv.dto_planejamento;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.atividade.AtividadeAssunto;
import br.gov.df.emater.aterwebsrv.modelo.atividade.Metodo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

public class PlanejamentoCadFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	private UnidadeOrganizacional unidadeOrganizacional;

	private Metodo metodo;

	private List<AtividadeAssunto> atividadeList;

	public Integer getAno() {
		return ano;
	}

	public List<AtividadeAssunto> getAtividadeList() {
		return atividadeList;
	}

	public Metodo getMetodo() {
		return metodo;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setAtividadeList(List<AtividadeAssunto> atividadeList) {
		this.atividadeList = atividadeList;
	}

	public void setMetodo(Metodo metodo) {
		this.metodo = metodo;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

}