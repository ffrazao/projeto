package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.bo.seguranca.AutenticarUsuarioCmd;
import br.gov.df.emater.aterwebsrv.bo.seguranca.LogCmd;

@Service("ProjetoCreditoRuralProjetoTecnicoRelCh")
public class ProjetoTecnicoRelCh extends _Cadeia {

	@Autowired
	public ProjetoTecnicoRelCh(AutenticarUsuarioCmd c1, ProjetoTecnicoRelCmd c2, LogCmd c3) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
	}

}