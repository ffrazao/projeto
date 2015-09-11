package br.gov.df.emater.aterwebsrv.bo.seguranca;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("SegurancaLogCmd")
public class LogCmd extends _Comando {

	@Override
	public boolean executar(_Contexto context) throws Exception {
		System.out.println(context.getAcao());
		System.out.println(context.getErro());
		System.out.println(context.getRequisicao());
		System.out.println(context.getResposta());
		return false;
	}
	

}
