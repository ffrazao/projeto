package br.gov.df.emater.aterwebsrv.dto.indice_producao;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificacao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.FormaProducaoValor;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

public class IndiceProducaoCadFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private Integer ano;

	private List<BemClassificacao> bemClassificacaoList;

	// ATENÇÃO! A ativacao deste filtro deve ser exclusiva do serviço,
	// não permitir que a camada de visão preencha estes dados
	@JsonIgnore
	private BemClassificado bemClassificado;

	private List<BemClassificado> bemClassificadoList;

	private List<Comunidade> comunidadeList;

	private Set<Confirmacao> confirmado;

	private List<PessoaJuridica> empresaList;

	private List<FormaProducaoValor> formaProducaoValorList;

	private Integer id;

	private PropriedadeRural propriedadeRural;

	private PublicoAlvo publicoAlvo;

	// ATENÇÃO! A ativacao deste filtro deve ser exclusiva do serviço,
	// não permitir que a camada de visão preencha estes dados
	@JsonIgnore
	private UnidadeOrganizacional unidadeOrganizacional;

	private List<UnidadeOrganizacional> unidadeOrganizacionalList;

	public IndiceProducaoCadFiltroDto() {

	}

	public Integer getAno() {
		return ano;
	}

	public List<BemClassificacao> getBemClassificacaoList() {
		return bemClassificacaoList;
	}

	public BemClassificado getBemClassificado() {
		return bemClassificado;
	}

	public List<BemClassificado> getBemClassificadoList() {
		return bemClassificadoList;
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

	public List<FormaProducaoValor> getFormaProducaoValorList() {
		return formaProducaoValorList;
	}

	public Integer getId() {
		return id;
	}

	public PropriedadeRural getPropriedadeRural() {
		return propriedadeRural;
	}

	public PublicoAlvo getPublicoAlvo() {
		return publicoAlvo;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public List<UnidadeOrganizacional> getUnidadeOrganizacionalList() {
		return unidadeOrganizacionalList;
	}

	public void setAno(Integer ano) {
		this.ano = ano;
	}

	public void setBemClassificacaoList(List<BemClassificacao> bemClassificacaoList) {
		this.bemClassificacaoList = bemClassificacaoList;
	}

	public void setBemClassificado(BemClassificado bemClassificado) {
		this.bemClassificado = bemClassificado;
	}

	public void setBemClassificadoList(List<BemClassificado> bemClassificadoList) {
		this.bemClassificadoList = bemClassificadoList;
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

	public void setFormaProducaoValorList(List<FormaProducaoValor> formaProducaoValorList) {
		this.formaProducaoValorList = formaProducaoValorList;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public void setPropriedadeRural(PropriedadeRural propriedadeRural) {
		this.propriedadeRural = propriedadeRural;
	}

	public void setPublicoAlvo(PublicoAlvo publicoAlvo) {
		this.publicoAlvo = publicoAlvo;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

	public void setUnidadeOrganizacionalList(List<UnidadeOrganizacional> unidadeOrganizacionalList) {
		this.unidadeOrganizacionalList = unidadeOrganizacionalList;
	}

}