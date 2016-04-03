package br.gov.df.emater.aterwebsrv.modelo.dto;

import java.util.List;

public class CarteiraProdutorRelFiltroDto extends FiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private List<Integer> pessoaIdList;

	private List<Integer> publicoAlvoPropriedadeRuralIdList;

	public List<Integer> getPessoaIdList() {
		return pessoaIdList;
	}

	public List<Integer> getPublicoAlvoPropriedadeRuralIdList() {
		return publicoAlvoPropriedadeRuralIdList;
	}

	public void setPessoaIdList(List<Integer> pessoaIdList) {
		this.pessoaIdList = pessoaIdList;
	}

	public void setPublicoAlvoPropriedadeRuralIdList(List<Integer> publicoAlvoPropriedadeRuralIdList) {
		this.publicoAlvoPropriedadeRuralIdList = publicoAlvoPropriedadeRuralIdList;
	}

}