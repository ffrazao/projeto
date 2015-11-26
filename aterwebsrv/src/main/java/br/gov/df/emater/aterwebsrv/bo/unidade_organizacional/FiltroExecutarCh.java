package br.gov.df.emater.aterwebsrv.bo.unidade_organizacional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;

@Service("UnidadeOrganizacionalFiltroExecutarCh")
public class FiltroExecutarCh extends _Cadeia {

	@Autowired
	public FiltroExecutarCh(AutenticarUsuarioCmd c1, FiltrarCmd c2) {
		super.addCommand(c1);
		super.addCommand(c2);
	}

}
