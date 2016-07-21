package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import java.util.Calendar;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.modelo.dto.ProjetoCreditoRuralCadFiltroDto;

@Service("ProjetoCreditoRuralFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		ProjetoCreditoRuralCadFiltroDto filtro = new ProjetoCreditoRuralCadFiltroDto();

		Calendar hoje = Calendar.getInstance();

		Calendar inicio = Calendar.getInstance();
		inicio.set(hoje.get(Calendar.YEAR), hoje.get(Calendar.YEAR), hoje.get(Calendar.DATE));
		inicio.set(Calendar.MONTH, -1);

		Calendar termino = Calendar.getInstance();
		termino.set(hoje.get(Calendar.YEAR), hoje.get(Calendar.YEAR), hoje.get(Calendar.DATE));

		filtro.setInicio(inicio);
		filtro.setTermino(termino);

		context.put("resultado", filtro);
		return false;
	}

}
