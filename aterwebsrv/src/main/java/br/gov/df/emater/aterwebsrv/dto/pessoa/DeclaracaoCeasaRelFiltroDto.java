package br.gov.df.emater.aterwebsrv.dto.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.BemClassificado;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

public class DeclaracaoCeasaRelFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	//private DeclaracaoProdutorRelFiltroDto declaracaoProdutorRelFiltroDto;
	
	private List<Integer> publicoAlvoPropriedadeRuralIdList;
	
	private List<DeclaracaoCeasaProdutoRelDto> producaoList;
	
	

//	public DeclaracaoProdutorRelFiltroDto getDeclaracaoProdutorRelFiltroDto() {
//		return declaracaoProdutorRelFiltroDto;
//	}
//
//	public void setDeclaracaoProdutorRelFiltroDto(DeclaracaoProdutorRelFiltroDto declaracaoProdutorRelFiltroDto) {
//		this.declaracaoProdutorRelFiltroDto = declaracaoProdutorRelFiltroDto;
//	}

	public List<DeclaracaoCeasaProdutoRelDto> getProducaoList() {
		return producaoList;
	}

	public void setProducaoList(List<DeclaracaoCeasaProdutoRelDto> producaoList) {
		this.producaoList = producaoList;
	}

	public List<Integer> getPublicoAlvoPropriedadeRuralIdList() {
		return publicoAlvoPropriedadeRuralIdList;
	}

	public void setPublicoAlvoPropriedadeRuralIdList(List<Integer> publicoAlvoPropriedadeRuralIdList) {
		this.publicoAlvoPropriedadeRuralIdList = publicoAlvoPropriedadeRuralIdList;
	}

	
}