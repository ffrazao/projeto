package br.gov.df.emater.aterwebsrv.bo.perfil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("PerfilSalvarComandoCh")
public class SalvarComandoCh extends _Cadeia {

	@Autowired
	public SalvarComandoCh(AutenticarUsuarioCmd c1, SalvarComandoCmd c2, LogCmd c3) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
	}

}