package br.gov.df.emater.aterwebsrv.dto.funcional;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoAtivaVi;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacional;

public class UnidadeOrganizacionalListaDto implements Dto {

	private static final long serialVersionUID = 1L;

	private List<UnidadeOrganizacionalListaDto> descendenteList;

	private List<LotacaoAtivaVi> lotacaoList;

	private UnidadeOrganizacional unidadeOrganizacional;

	public List<UnidadeOrganizacionalListaDto> getDescendenteList() {
		return descendenteList;
	}

	public List<LotacaoAtivaVi> getLotacaoList() {
		return lotacaoList;
	}

	public UnidadeOrganizacional getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setDescendenteList(List<UnidadeOrganizacionalListaDto> descendenteList) {
		this.descendenteList = descendenteList;
	}

	public void setLotacaoList(List<LotacaoAtivaVi> lotacaoList) {
		this.lotacaoList = lotacaoList;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacional unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

}
