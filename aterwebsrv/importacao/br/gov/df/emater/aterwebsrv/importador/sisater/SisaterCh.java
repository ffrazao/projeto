package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service("SisaterCh")
public class SisaterCh extends _Cadeia {

	@Autowired
	public SisaterCh(SisaterPendenciasExcluirCmd c1, SisaterComunidadeCmd c2, SisaterEmpregadoCmd c3,
			SisaterPublicoAlvoCmd c4, SisaterPropriedadeRuralExportaUTMCmd c5, SisaterPropriedadeRuralCmd c6,
			SisaterPublicoAlvoPropriedadeRuralCmd c7, SisaterIndiceProducaoCmd c8, SisaterAcompanhamentoAterCh c9) {
		
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);

		super.addCommand(c4);

		super.addCommand(c5);
		// super.addCommand(c6);
		super.addCommand(c7);
		super.addCommand(c8);
		super.addCommand(c9);
	}

}