package br.gov.df.emater.aterwebsrv.bo.atividade;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("AtividadeExcluirCh")
public class ExcluirCh extends _Cadeia {

	@Autowired
	public ExcluirCh(AutenticarUsuarioCmd c1, ExcluirCmd c2, LogCmd c3) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
	}

}