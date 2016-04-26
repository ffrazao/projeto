package br.gov.df.emater.aterwebsrv.bo.log_acao;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.modelo.dto.LogAcaoCadFiltroDto;

@Service("LogAcaoFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		LogAcaoCadFiltroDto filtro = new LogAcaoCadFiltroDto();

		context.put("resultado", filtro);
		return false;
	}

}
