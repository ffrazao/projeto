package br.gov.df.emater.aterwebsrv.bo.formulario;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("FormularioColetarCh")
public class ColetarCh extends _Cadeia {

	@Autowired
	public ColetarCh(AutenticarUsuarioCmd c1, ColetarCmd c2, LogCmd c3) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
	}

}
