package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;

@Service("PessoaFiltroExecutarCh")
public class FiltroExecutarCh extends _Cadeia {

	@Autowired
	public FiltroExecutarCh(AutenticarUsuarioCmd c1, SalvarCmd c2) {
		super.addCommand(c1);
		super.addCommand(c2);
	}

}
