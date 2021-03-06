package br.gov.df.emater.aterwebsrv.bo.manual_online;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("ManualOnlineSalvarModuloCh")
public class SalvarModuloCh extends _Cadeia {

	@Autowired
	public SalvarModuloCh(AutenticarUsuarioCmd c1, SalvarModuloCmd c2, LogCmd c3) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
	}

}