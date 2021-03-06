package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("PessoaSalvarCh")
public class SalvarCh extends _Cadeia {

	@Autowired
	public SalvarCh(AutenticarUsuarioCmd c1, VerifPendCh c2, SalvarCmd c3, LogCmd c4) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
		super.addCommand(c4);
	}

}