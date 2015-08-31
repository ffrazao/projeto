package br.gov.df.emater.aterwebsrv.bo.seguranca;

import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;

@Service("SegurancaAutenticarUsuarioCmd")
public class AutenticarUsuarioCmd extends _Comando {

	@Override
	public boolean execute(Context contexto) throws Exception {
		System.out.println("Validando usuário");
		return false;
	}

}
