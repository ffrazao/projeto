package br.gov.df.emater.aterwebsrv.bo.usuario;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.dto.UsuarioCadFiltroDto;

@Service("UsuarioFiltroNovoCmd")
public class FiltroNovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		UsuarioCadFiltroDto filtro = new UsuarioCadFiltroDto();

		contexto.setResposta(filtro);
		return false;
	}

}
