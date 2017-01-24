package br.gov.df.emater.aterwebsrv.dto.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

public class DeclaracaoProdutorRelFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private String observacao;

	private List<Integer> pessoaIdList;
	
	private List<Integer> publicoAlvoPropriedadeRuralIdList;

	public List<Integer> getPublicoAlvoPropriedadeRuralIdList() {
		return publicoAlvoPropriedadeRuralIdList;
	}

	public void setPublicoAlvoPropriedadeRuralIdList(List<Integer> publicoAlvoPropriedadeRuralIdList) {
		this.publicoAlvoPropriedadeRuralIdList = publicoAlvoPropriedadeRuralIdList;
	}

	public List<Integer> getPessoaIdList() {
		return pessoaIdList;
	}

	public void setPessoaIdList(List<Integer> pessoaIdList) {
		this.pessoaIdList = pessoaIdList;
	}

	public String getObservacao() {
		return observacao;
	}

	public void setObservacao(String observacao) {
		this.observacao = observacao;
	}

}