package br.gov.df.emater.aterwebsrv.bo.agenda;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.atividade.AtividadeCadFiltroDto;

@Service("AgendaFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		AtividadeCadFiltroDto filtro = new AtividadeCadFiltroDto();
		Calendar hoje = Calendar.getInstance();
		Calendar inicio = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), 1);
		inicio.add(Calendar.MONTH, -6);
		Calendar termino = new GregorianCalendar(hoje.get(Calendar.YEAR), hoje.get(Calendar.MONTH), hoje.get(Calendar.DATE));
		filtro.setInicio(inicio);
		filtro.setTermino(termino);
		context.put("resposta", filtro);
		return false;
	}

}
