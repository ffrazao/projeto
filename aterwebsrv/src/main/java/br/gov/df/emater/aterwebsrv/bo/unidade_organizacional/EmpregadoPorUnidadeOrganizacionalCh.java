package br.gov.df.emater.aterwebsrv.bo.unidade_organizacional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service("UnidadeOrganizacionalEmpregadoPorUnidadeOrganizacionalCh")
public class EmpregadoPorUnidadeOrganizacionalCh extends _Cadeia {

	@Autowired
	public EmpregadoPorUnidadeOrganizacionalCh(EmpregadoPorUnidadeOrganizacionalCmd c1) {
		super.addCommand(c1);
	}

}
