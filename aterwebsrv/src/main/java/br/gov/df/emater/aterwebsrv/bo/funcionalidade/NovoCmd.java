package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Funcionalidade;

@Service("FuncionalidadeNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Funcionalidade result = (Funcionalidade) contexto.getRequisicao();

		if (result == null) {
			result = new Funcionalidade();
		}

		result.setAtivo(Confirmacao.S);

		contexto.setResposta(result);

		return true;
	}

}