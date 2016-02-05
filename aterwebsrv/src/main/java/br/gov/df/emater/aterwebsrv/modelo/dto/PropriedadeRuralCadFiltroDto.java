package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.List;
import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.ater.SistemaProducao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AreaUtil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.SituacaoFundiaria;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

public class PropriedadeRuralCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private AreaUtil areaUtil;

	private List<PessoaJuridica> empresaList;
	private List<UnidadeOrganizacional> unidadeOrganizacionalList;
	private List<Comunidade> comunidadeList;
	private List<PublicoAlvo> publicoAlvoList;
	
	private String pessoaVinculada;

	public String getPessoaVinculada() {
		return pessoaVinculada;
	}

	public void setPessoaVinculada(String pessoaVinculada) {
		this.pessoaVinculada = pessoaVinculada;
	}

	public List<PessoaJuridica> getEmpresaList() {
		return empresaList;
	}

	public void setEmpresaList(List<PessoaJuridica> empresaList) {
		this.empresaList = empresaList;
	}

	public List<UnidadeOrganizacional> getUnidadeOrganizacionalList() {
		return unidadeOrganizacionalList;
	}

	public void setUnidadeOrganizacionalList(List<UnidadeOrganizacional> unidadeOrganizacionalList) {
		this.unidadeOrganizacionalList = unidadeOrganizacionalList;
	}

	public List<Comunidade> getComunidadeList() {
		return comunidadeList;
	}

	public void setComunidadeList(List<Comunidade> comunidadeList) {
		this.comunidadeList = comunidadeList;
	}

	public List<PublicoAlvo> getPublicoAlvoList() {
		return publicoAlvoList;
	}

	public void setPublicoAlvoList(List<PublicoAlvo> publicoAlvoList) {
		this.publicoAlvoList = publicoAlvoList;
	}

	private String nomePropriedade;

	private Set<Confirmacao> outorga;

	private SistemaProducao sistemaProducao;

	private SituacaoFundiaria situacaoFundiaria;



	public AreaUtil getAreaUtil() {
		return areaUtil;
	}

	public String getNomePropriedade() {
		return nomePropriedade;
	}

	public Set<Confirmacao> getOutorga() {
		return outorga;
	}

	public SistemaProducao getSistemaProducao() {
		return sistemaProducao;
	}

	public SituacaoFundiaria getSituacaoFundiaria() {
		return situacaoFundiaria;
	}

	public void setAreaUtil(AreaUtil areaUtil) {
		this.areaUtil = areaUtil;
	}

	public void setNomePropriedade(String nomePropriedade) {
		this.nomePropriedade = nomePropriedade;
	}

	public void setOutorga(Set<Confirmacao> outorga) {
		this.outorga = outorga;
	}

	public void setSistemaProducao(SistemaProducao sistemaProducao) {
		this.sistemaProducao = sistemaProducao;
	}

	public void setSituacaoFundiaria(SituacaoFundiaria situacaoFundiaria) {
		this.situacaoFundiaria = situacaoFundiaria;
	}

}