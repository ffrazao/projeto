package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.List;
import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

public class IndiceProducaoCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	private List<Comunidade> comunidadeList;

	private Set<Confirmacao> confirmado;

	private List<PessoaJuridica> empresaList;

	private Integer id;

	private List<UnidadeOrganizacional> unidadeOrganizacionalList;

	public IndiceProducaoCadFiltroDto() {

	}

	public Integer getAno() {
		return ano;
	}

	public List<Comunidade> getComunidadeList() {
		return comunidadeList;
	}

	public Set<Confirmacao> getConfirmado() {
		return confirmado;
	}

	public List<PessoaJuridica> getEmpresaList() {
		return empresaList;
	}

	public Integer getId() {
		return id;
	}

	public List<UnidadeOrganizacional> getUnidadeOrganizacionalList() {
		return unidadeOrganizacionalList;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setComunidadeList(List<Comunidade> comunidadeList) {
		this.comunidadeList = comunidadeList;
	}

	public void setConfirmado(Set<Confirmacao> confirmado) {
		this.confirmado = confirmado;
	}

	public void setEmpresaList(List<PessoaJuridica> empresaList) {
		this.empresaList = empresaList;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setUnidadeOrganizacionalList(List<UnidadeOrganizacional> unidadeOrganizacionalList) {
		this.unidadeOrganizacionalList = unidadeOrganizacionalList;
	}

}