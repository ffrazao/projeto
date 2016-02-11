package br.gov.df.emater.aterwebsrv.bo.atividade;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.modelo.dto.AtividadeCadFiltroDto;

@Service("AtividadeFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		AtividadeCadFiltroDto filtro = new AtividadeCadFiltroDto();
		context.put("resultado", filtro);
		return false;
	}

}
