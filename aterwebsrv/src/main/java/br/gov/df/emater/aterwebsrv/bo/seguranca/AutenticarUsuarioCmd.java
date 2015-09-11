package br.gov.df.emater.aterwebsrv.bo.seguranca;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("SegurancaAutenticarUsuarioCmd")
public class AutenticarUsuarioCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		System.out.println("Validando usuário");
		return false;
	}

}
