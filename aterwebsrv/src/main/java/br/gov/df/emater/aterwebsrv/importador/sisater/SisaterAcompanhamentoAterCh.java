package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service
public class SisaterAcompanhamentoAterCh extends _Cadeia {

	@Autowired
	public SisaterAcompanhamentoAterCh(SisaterAcompanhamentoAterExcluirCmd c1, SisaterAcompanhamentoAterIncluirAntes2014Cmd c2, SisaterAcompanhamentoAterIncluirDepois2014Cmd c3) {
		super.addCommand(c1);
		//super.addCommand(c2);
		super.addCommand(c3);
	}

}