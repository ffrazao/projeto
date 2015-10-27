package br.gov.df.emater.aterwebsrv.bo.dominio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("DominioCh")
public class DominioCh extends _Cadeia {

	@Autowired
	public DominioCh(AutenticarUsuarioCmd c1, PreparaRespostaCmd c2, EnumeracaoCmd c3, EntidadeCmd c4, LogCmd c5) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
		super.addCommand(c4);
		super.addCommand(c5);
	}

}
