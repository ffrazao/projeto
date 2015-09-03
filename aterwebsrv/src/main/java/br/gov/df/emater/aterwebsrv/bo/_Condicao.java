package br.gov.df.emater.aterwebsrv.bo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public abstract class _Condicao extends _Comando {
	
	@Autowired
	private FacadeBo facadeBo;
	
	protected FacadeBo getFacadeBo() {
		return facadeBo;
	}

}
