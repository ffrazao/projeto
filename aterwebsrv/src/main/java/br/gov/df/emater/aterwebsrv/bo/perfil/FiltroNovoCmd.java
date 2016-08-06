package br.gov.df.emater.aterwebsrv.bo.perfil;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dto.sistema.PerfilCadFiltroDto;

@Service("PerfilFiltroNovoCmd")
public class FiltroNovoCmd implements Command {

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		PerfilCadFiltroDto filtro = new PerfilCadFiltroDto();

		context.put("resultado", filtro);
		return false;
	}

}
