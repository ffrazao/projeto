package br.gov.df.emater.aterwebsrv.dto.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.dto.CadFiltroDtoCustom;
import br.gov.df.emater.aterwebsrv.modelo.ater.PublicoAlvoPropriedadeRural;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

public class DeclaracaoCeasaRelFiltroDto extends CadFiltroDtoCustom {

	private static final long serialVersionUID = 1L;

	private DeclaracaoProdutorRelFiltroDto declaracaoProdutorRelFiltroDto;
	
	private List<Object> producaoList;

	public DeclaracaoProdutorRelFiltroDto getDeclaracaoProdutorRelFiltroDto() {
		return declaracaoProdutorRelFiltroDto;
	}

	public void setDeclaracaoProdutorRelFiltroDto(DeclaracaoProdutorRelFiltroDto declaracaoProdutorRelFiltroDto) {
		this.declaracaoProdutorRelFiltroDto = declaracaoProdutorRelFiltroDto;
	}

	public List<Object> getProducaoList() {
		return producaoList;
	}

	public void setProducaoList(List<Object> producaoList) {
		this.producaoList = producaoList;
	}

	
}