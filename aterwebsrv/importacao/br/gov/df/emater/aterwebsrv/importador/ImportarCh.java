package br.gov.df.emater.aterwebsrv.importador;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service("ImportarCh")
public class ImportarCh extends _Cadeia {

	@Autowired
	public ImportarCh(
			InicioImportacaoCmd c0, 
			EmaterCmd c1, 
			EmaterUsuarioCmd c2, 
			TempDirCmd c3, 
			RelacaoEmpregadosExcelCarregarCmd c4, 
			RelacaoEmpregadosExcelImportarCmd c5, 
			ContaUsuarioExcelCarregarCmd c6, 
			ContaUsuarioExcelImportarCmd c7
			, SisaterCmd c8
			) {
		super.addCommand(c0);
		 super.addCommand(c1);
		super.addCommand(c2);
//		 super.addCommand(c3);
//		 super.addCommand(c4);
//		 super.addCommand(c5);
//		 super.addCommand(c6);
//		 super.addCommand(c7);
		 super.addCommand(c8);
	}

}
