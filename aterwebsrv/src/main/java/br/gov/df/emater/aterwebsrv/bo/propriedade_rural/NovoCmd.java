package br.gov.df.emater.aterwebsrv.bo.propriedade_rural;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.ater.PropriedadeRural;

@Service("PropriedadeRuralNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PropriedadeRural result = new PropriedadeRural();

		contexto.setResposta(result);

		return true;
	}

}