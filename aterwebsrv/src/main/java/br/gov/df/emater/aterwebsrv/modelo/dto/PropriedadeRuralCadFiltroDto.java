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
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Area;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;

public class PropriedadeRuralCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private List<Area> areaList;

	private AreaUtil areaUtil;

	private List<Comunidade> comunidadeList;

	private List<PessoaJuridica> empresaList;

	private String nomePropriedade;

	private Set<Confirmacao> outorga;

	private String pessoaVinculada;

	private List<PublicoAlvo> publicoAlvoList;

	private SistemaProducao sistemaProducao;

	private SituacaoFundiaria situacaoFundiaria;

	private List<UnidadeOrganizacional> unidadeOrganizacionalList;

	public AreaUtil getAreaUtil() {
		return areaUtil;
	}

	public List<Comunidade> getComunidadeList() {
		return comunidadeList;
	}

	public List<PessoaJuridica> getEmpresaList() {
		return empresaList;
	}

	public String getNomePropriedade() {
		return nomePropriedade;
	}

	public Set<Confirmacao> getOutorga() {
		return outorga;
	}

	public String getPessoaVinculada() {
		return pessoaVinculada;
	}

	public List<PublicoAlvo> getPublicoAlvoList() {
		return publicoAlvoList;
	}

	public SistemaProducao getSistemaProducao() {
		return sistemaProducao;
	}

	public SituacaoFundiaria getSituacaoFundiaria() {
		return situacaoFundiaria;
	}

	public List<UnidadeOrganizacional> getUnidadeOrganizacionalList() {
		return unidadeOrganizacionalList;
	}

	public List<Area> getAreaList() {
		return areaList;
	}

	public void setAreaList(List<Area> areaList) {
		this.areaList = areaList;
	}

	public void setAreaUtil(AreaUtil areaUtil) {
		this.areaUtil = areaUtil;
	}

	public void setComunidadeList(List<Comunidade> comunidadeList) {
		this.comunidadeList = comunidadeList;
	}

	public void setEmpresaList(List<PessoaJuridica> empresaList) {
		this.empresaList = empresaList;
	}

	public void setNomePropriedade(String nomePropriedade) {
		this.nomePropriedade = nomePropriedade;
	}

	public void setOutorga(Set<Confirmacao> outorga) {
		this.outorga = outorga;
	}

	public void setPessoaVinculada(String pessoaVinculada) {
		this.pessoaVinculada = pessoaVinculada;
	}

	public void setPublicoAlvoList(List<PublicoAlvo> publicoAlvoList) {
		this.publicoAlvoList = publicoAlvoList;
	}

	public void setSistemaProducao(SistemaProducao sistemaProducao) {
		this.sistemaProducao = sistemaProducao;
	}

	public void setSituacaoFundiaria(SituacaoFundiaria situacaoFundiaria) {
		this.situacaoFundiaria = situacaoFundiaria;
	}

	public void setUnidadeOrganizacionalList(List<UnidadeOrganizacional> unidadeOrganizacionalList) {
		this.unidadeOrganizacionalList = unidadeOrganizacionalList;
	}

}