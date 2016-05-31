package br.gov.df.emater.aterwebsrv.bo.perfil;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;

@Service("PerfilNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Perfil result = (Perfil) contexto.getRequisicao();

		if (result == null) {
			result = new Perfil();
		}

		result.setAtivo(Confirmacao.S);

		contexto.setResposta(result);

		return true;
	}

}