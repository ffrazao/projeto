package br.gov.df.emater.aterwebsrv.bo.log_acao;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dto.LogAcaoCadFiltroDto;

@Service("LogAcaoFiltroNovoCmd")
public class FiltroNovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		LogAcaoCadFiltroDto filtro = new LogAcaoCadFiltroDto();

		Calendar inicio = Calendar.getInstance();
		inicio.roll(Calendar.MONTH, -1);
		filtro.setInicio(inicio);
		filtro.setTermino(Calendar.getInstance());

		contexto.setResposta(filtro);

		return false;
	}

}
