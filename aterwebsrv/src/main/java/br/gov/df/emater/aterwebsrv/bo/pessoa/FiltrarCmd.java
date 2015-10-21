package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("PessoaFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Override
	public boolean executar(_Contexto context) throws Exception {
		System.out.println("Filtrando pessoa...");

		return false;
	}

}
