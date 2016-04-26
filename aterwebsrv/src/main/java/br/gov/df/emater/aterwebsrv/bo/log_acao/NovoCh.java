package br.gov.df.emater.aterwebsrv.bo.log_acao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("LogAcaoNovoCh")
public class NovoCh extends _Cadeia {

	@Autowired
	public NovoCh(AutenticarUsuarioCmd c1, NovoCmd c2, LogCmd c3) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
	}

}