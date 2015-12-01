package br.gov.df.emater.aterwebsrv.bo.seguranca;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("SegurancaLogCmd")
public class LogCmd extends _Comando {

	@Override
	public boolean executar(_Contexto context) throws Exception {
		System.out.format("Log acao[%s], erro[%s], requisicao[%s], resposta[%s]\n", context.getAcao(), context.getErro(), context.getRequisicao(), context.getResposta());
		return false;
	}

}