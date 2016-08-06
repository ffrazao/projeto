package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dto.projeto_credito_rural.ProjetoCreditoRuralCadFiltroDto;

@Service("ProjetoCreditoRuralFiltroNovoCmd")
public class FiltroNovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRuralCadFiltroDto filtro = new ProjetoCreditoRuralCadFiltroDto();

		Calendar hoje = Calendar.getInstance();

		Calendar inicio = new GregorianCalendar(hoje.get(Calendar.YEAR), 0, 1);
		inicio.roll(Calendar.YEAR, -1);

		Calendar termino = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), hoje.get(Calendar.DATE));

		filtro.setInicio(inicio);
		filtro.setTermino(termino);

		contexto.setResposta(filtro);
		return false;
	}

}
