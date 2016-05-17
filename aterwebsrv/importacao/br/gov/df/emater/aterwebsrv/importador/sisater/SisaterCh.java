package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service("SisaterCh")
public class SisaterCh extends _Cadeia {

	@Autowired
	public SisaterCh(SisaterComunidadeCmd c1, SisaterPublicoAlvoCmd c2, SisaterPropriedadeRuralExportaUTMCmd c3, SisaterPropriedadeRuralCmd c4, SisaterPublicoAlvoPropriedadeRuralCmd c5, SisaterIndiceProducaoCmd c6) {
		// super.addCommand(c1);
		super.addCommand(c2);
		// super.addCommand(c3);
		// super.addCommand(c4);
		// super.addCommand(c5);
		super.addCommand(c6);
	}

}