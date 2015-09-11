package br.gov.df.emater.aterwebsrv.bo;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;

public abstract class _Comando implements Command {
	
	@Override
	public boolean execute(Context context) throws Exception {
		return executar((_Contexto) context);
	}
	
	public abstract boolean executar(_Contexto context) throws Exception;
	
}
