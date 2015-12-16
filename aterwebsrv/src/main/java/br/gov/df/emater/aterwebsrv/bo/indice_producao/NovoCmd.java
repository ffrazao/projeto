package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Service("IndiceProducaoNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Producao result = new Producao();
		
		result.setAno(Calendar.getInstance().get(Calendar.YEAR));

		contexto.setResposta(result);

		return true;
	}

}