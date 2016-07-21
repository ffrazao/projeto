package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.credito_rural.ProjetoCreditoRural;

@Service("ProjetoCreditoRuralNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		ProjetoCreditoRural result = (ProjetoCreditoRural) contexto.getRequisicao();

		if (result == null) {
			result = new ProjetoCreditoRural();
		}
		contexto.setResposta(result);

		return true;
	}

}