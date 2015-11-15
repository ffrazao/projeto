package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.Set;

import br.gov.df.emater.aterwebsrv.modelo.ater.Comunidade;
import br.gov.df.emater.aterwebsrv.modelo.ater.SistemaProducao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.AreaUtil;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.SituacaoFundiaria;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

public class PropriedadeRuralCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private AreaUtil areaUtil;

	private Comunidade comunidade;

	private String nomePropriedade;

	private Set<Confirmacao> outorga;

	private String pessoaVinculada;

	private SistemaProducao sistemaProducao;

	private SituacaoFundiaria situacaoFundiaria;

	private UnidadeOrganizacional unidadeOrganizacional;

	public AreaUtil getAreaUtil() {
		return areaUtil;
	}

	public Comunidade getComunidade() {
		return comunidade;
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

	public SistemaProducao getSistemaProducao() {
		return sistemaProducao;
	}

	public SituacaoFundiaria getSituacaoFundiaria() {
		return situacaoFundiaria;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setAreaUtil(AreaUtil areaUtil) {
		this.areaUtil = areaUtil;
	}

	public void setComunidade(Comunidade comunidade) {
		this.comunidade = comunidade;
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

	public void setSistemaProducao(SistemaProducao sistemaProducao) {
		this.sistemaProducao = sistemaProducao;
	}

	public void setSituacaoFundiaria(SituacaoFundiaria situacaoFundiaria) {
		this.situacaoFundiaria = situacaoFundiaria;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

}