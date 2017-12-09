package br.gov.df.emater.aterwebsrv.importador.empregado;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Cadeia;

@Service("EmpregadoCh")
public class EmpregadoCh extends _Cadeia {

	@Autowired
	public EmpregadoCh(EmpregadoRelacaoExcelCarregarCmd c1, EmpregadoRelacaoExcelImportarCmd c2, EmpregadoContaUsuarioExcelCarregarCmd c3, EmpregadoContaUsuarioExcelImportarCmd c4, EmpregadoDemitidosExcelCarregarCmd c5, EmpregadoDemitidosExcelImportarCmd c6) {
		super.addCommand(c1);
		super.addCommand(c2);
		super.addCommand(c3);
		super.addCommand(c4);
		super.addCommand(c5);
		super.addCommand(c6);
	}

}
