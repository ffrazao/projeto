package br.gov.df.emater.aterwebsrv.dto;

import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvo;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

public class PublicoAlvoPropriedadeRuralCadFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private List<PropriedadeRural> propriedadeRuralList;

	private List<PublicoAlvo> publicoAlvoList;

	private List<UnidadeOrganizacional> unidadeOrganizacionalList;

	public List<PropriedadeRural> getPropriedadeRuralList() {
		return propriedadeRuralList;
	}

	public List<PublicoAlvo> getPublicoAlvoList() {
		return publicoAlvoList;
	}

	public List<UnidadeOrganizacional> getUnidadeOrganizacionalList() {
		return unidadeOrganizacionalList;
	}

	public void setPropriedadeRuralList(List<PropriedadeRural> propriedadeRuralList) {
		this.propriedadeRuralList = propriedadeRuralList;
	}

	public void setPublicoAlvoList(List<PublicoAlvo> publicoAlvoList) {
		this.publicoAlvoList = publicoAlvoList;
	}

	public void setUnidadeOrganizacionalList(List<UnidadeOrganizacional> unidadeOrganizacionalList) {
		this.unidadeOrganizacionalList = unidadeOrganizacionalList;
	}

}