package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

public class PessoaDaoImpl implements PessoaDaoCustom {

	@Override
	public List<Pessoa> filtrar(PessoaCadFiltroDto filtro) {
		System.out.println("filtrando pelo dao...");
		return null;
	}

}
