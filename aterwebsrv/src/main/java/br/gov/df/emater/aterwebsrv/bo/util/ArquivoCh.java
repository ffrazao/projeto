package br.gov.df.emater.aterwebsrv.bo.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("UtilArquivoCh")
public class ArquivoCh extends _Cadeia {

	@Autowired
	public ArquivoCh(AutenticarUsuarioCmd c1, @Qualifier("UtilArquivoCmd") ArquivoCmd c2, LogCmd c3) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
	}

}