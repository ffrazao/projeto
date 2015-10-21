package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

public interface PessoaDaoCustom {

	List<Pessoa> filtrar(PessoaCadFiltroDto filtro);

}