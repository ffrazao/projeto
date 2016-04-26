package br.gov.df.emater.aterwebsrv.bo.log_acao;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.LogAcao;

@Service("LogAcaoNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		LogAcao result = (LogAcao) contexto.getRequisicao();

		if (result == null) {
			result = new LogAcao();
		}

		contexto.setResposta(result);

		return true;
	}

}