package br.gov.df.emater.aterwebsrv.importador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service("SisaterCh")
public class SisaterCh extends _Cadeia {

	@Autowired
	public SisaterCh(SisaterComunidadeCmd c1) {
		super.addCommand(c1);
	}

}
