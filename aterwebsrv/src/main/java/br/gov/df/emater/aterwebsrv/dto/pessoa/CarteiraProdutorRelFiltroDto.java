package br.gov.df.emater.aterwebsrv.dto.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;

public class CarteiraProdutorRelFiltroDto extends CadFiltroDtoCustom {

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