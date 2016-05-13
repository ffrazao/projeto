package br.gov.df.emater.aterwebsrv.bo.formulario;

import java.util.Calendar;

import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Situacao;
import br.gov.df.emater.aterwebsrv.modelo.formulario.Formulario;

@Service("FormularioNovoCmd")
public class NovoCmd extends _Comando {

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Formulario result = new Formulario();

		result.setSituacao(Situacao.A);
		result.setInicio(Calendar.getInstance());

		contexto.setResposta(result);

		return true;
	}

}