package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.FuncionalidadeCadFiltroDto;

@Service("FuncionalidadeFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		FuncionalidadeCadFiltroDto filtro = new FuncionalidadeCadFiltroDto();

		context.put("resultado", filtro);
		return false;
	}

}
