package br.gov.df.emater.aterwebsrv.bo.usuario;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.modelo.dto.UsuarioCadFiltroDto;

@Service("UsuarioFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		UsuarioCadFiltroDto filtro = new UsuarioCadFiltroDto();

		context.put("resultado", filtro);
		return false;
	}

}
