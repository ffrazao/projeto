package br.gov.df.emater.aterwebsrv.dto.funcional;

import java.util.List;
import java.util.stream.Collectors;

import br.gov.df.emater.aterwebsrv.dto.Dto;
import br.gov.df.emater.aterwebsrv.modelo.funcional.LotacaoAtivaVi;
import br.gov.df.emater.aterwebsrv.modelo.funcional.UnidadeOrganizacionalAtivaVi;

public class OrganogramaDto implements Dto {

	private static final long serialVersionUID = 1L;

	private List<OrganogramaDto> descendenteList;

	private List<LotacaoAtivaVi> lotacaoList;

	private UnidadeOrganizacionalAtivaVi unidadeOrganizacional;

	public List<OrganogramaDto> getDescendenteList() {
		return descendenteList;
	}

	public List<LotacaoAtivaVi> getLotacaoList() {
		return lotacaoList;
	}

	public UnidadeOrganizacionalAtivaVi getUnidadeOrganizacional() {
		return unidadeOrganizacional;
	}

	public void setDescendenteList(List<OrganogramaDto> descendenteList) {
		this.descendenteList = descendenteList;
	}

	public void setLotacaoList(List<LotacaoAtivaVi> lotacaoList) {
		if (lotacaoList != null) {
			lotacaoList.forEach((l) -> l.getEmprego().setPessoaRelacionamentoList(l.getEmprego().getPessoaRelacionamentoList().stream().filter(pr -> "Contratado".equalsIgnoreCase(pr.getRelacionamentoFuncao().getNomeSeMasculino())).collect(Collectors.toList())));
		}
		this.lotacaoList = lotacaoList;
	}

	public void setUnidadeOrganizacional(UnidadeOrganizacionalAtivaVi unidadeOrganizacional) {
		this.unidadeOrganizacional = unidadeOrganizacional;
	}

}
