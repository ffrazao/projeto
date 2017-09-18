package br.gov.df.emater.aterwebsrv.dto.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;

public class MesclarPessoaDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private List<Integer> pessoaIdList;

	private Integer principalId;

	public List<Integer> getPessoaIdList() {
		return pessoaIdList;
	}

	public Integer getPrincipalId() {
		return principalId;
	}

	public void setPessoaIdList(List<Integer> pessoaIdList) {
		this.pessoaIdList = pessoaIdList;
	}

	public void setPrincipalId(Integer principalId) {
		this.principalId = principalId;
	}

}