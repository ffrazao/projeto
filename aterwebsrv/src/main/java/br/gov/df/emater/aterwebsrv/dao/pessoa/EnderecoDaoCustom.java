package br.gov.df.emater.aterwebsrv.dao.pessoa;

import java.util.List;

import br.gov.df.emater.aterwebsrv.modelo.pessoa.Endereco;

public interface EnderecoDaoCustom {

	List<Endereco> procurar(Endereco endereco);

}