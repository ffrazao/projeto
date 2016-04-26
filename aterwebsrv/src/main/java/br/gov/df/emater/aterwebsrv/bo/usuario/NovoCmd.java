package br.gov.df.emater.aterwebsrv.bo.usuario;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("UsuarioNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario result = (Usuario) contexto.getRequisicao();

		if (result == null) {
			result = new Usuario();
		}
		
		contexto.setResposta(result);

		return true;
	}

}