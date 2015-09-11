package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("PessoaSalvarCmd")
public class SalvarCmd extends _Comando {

	@Override
	public boolean executar(_Contexto context) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("Salvando pessoa...");
		if( true ){
			throw new RuntimeException("sheet");
		}
		return false;
	}

}
