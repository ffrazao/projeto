package br.gov.df.emater.aterwebsrv.importador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;
import br.gov.df.emater.aterwebsrv.importador.basico.BasicoEmaterCmd;
import br.gov.df.emater.aterwebsrv.importador.basico.BasicoEmaterUsuarioCmd;
import br.gov.df.emater.aterwebsrv.importador.basico.BasicoInicioImportacaoCmd;
import br.gov.df.emater.aterwebsrv.importador.basico.BasicoTempDirCmd;
import br.gov.df.emater.aterwebsrv.importador.empregado.EmpregadoCh;
import br.gov.df.emater.aterwebsrv.importador.sisater.SisaterCmd;

@Service("ImportarCh")
public class ImportarCh extends _Cadeia {

	@Autowired
	public ImportarCh(BasicoInicioImportacaoCmd c0, BasicoEmaterCmd c1, BasicoEmaterUsuarioCmd c2, BasicoTempDirCmd c3, EmpregadoCh c4, SisaterCmd c5) {
		super.addCommand(c0);
		super.addCommand(c1);
		super.addCommand(c2);
		//super.addCommand(c3);
		//super.addCommand(c4);
		super.addCommand(c5);
	}

}
