package br.gov.df.emater.aterwebsrv.bo.relatorio;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.relatorio._Relatorio;

@Service("RelatorioCompilarCmd")
public class CompilarCmd extends _Comando {

	@Autowired
	private _Relatorio relatorio;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		String relatorioNome = (String) contexto.getRequisicao();

		relatorio.compilar(relatorioNome);

		contexto.setResposta("Relatório(s) compilado(s)");

		return true;
	}

}