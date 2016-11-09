package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service("IndiceProducaoSalvarDescendenteCh")
public class SalvarDescendenteCh extends _Cadeia {

	@Autowired
	public SalvarDescendenteCh(SalvarCmd c1, SalvarPrincipalCmd c2) {
		super.addCommand(c1);
		super.addCommand(c2);
	}

}