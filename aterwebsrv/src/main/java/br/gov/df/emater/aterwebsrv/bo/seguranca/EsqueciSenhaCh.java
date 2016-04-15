package br.gov.df.emater.aterwebsrv.bo.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.util.EmailEnviarCmd;

@Service("SegurancaEsqueciSenhaCh")
public class EsqueciSenhaCh extends _Cadeia {

	@Autowired
	public EsqueciSenhaCh(EsqueciSenhaCmd c1, EmailEnviarCmd c2) {
		super.addCommand(c1);
		super.addCommand(c2);
	}

}