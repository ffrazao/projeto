package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;

public interface PessoaDaoCustom {

	List<Object[]> filtrar(PessoaCadFiltroDto filtro);

}