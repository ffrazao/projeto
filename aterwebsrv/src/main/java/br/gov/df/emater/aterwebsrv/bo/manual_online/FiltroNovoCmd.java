package br.gov.df.emater.aterwebsrv.bo.manual_online;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.sistema.ManualOnlineCadFiltroDto;

@Service("ManualOnlineFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		ManualOnlineCadFiltroDto filtro = new ManualOnlineCadFiltroDto();

		context.put("resultado", filtro);
		return false;
	}

}
