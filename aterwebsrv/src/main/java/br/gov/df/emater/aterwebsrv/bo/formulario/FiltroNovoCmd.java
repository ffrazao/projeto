package br.gov.df.emater.aterwebsrv.bo.formulario;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.modelo.dto.FormularioCadFiltroDto;

@Service("FormularioFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		FormularioCadFiltroDto filtro = new FormularioCadFiltroDto();
		context.put("resultado", filtro);
		return false;
	}

}
