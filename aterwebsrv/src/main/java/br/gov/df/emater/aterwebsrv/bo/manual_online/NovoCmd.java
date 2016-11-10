package br.gov.df.emater.aterwebsrv.bo.manual_online;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.ManualOnline;

@Service("ManualOnlineNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ManualOnline result = (ManualOnline) contexto.getRequisicao();

		if (result == null) {
			result = new ManualOnline();
		}

		contexto.setResposta(result);

		return true;
	}

}