package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.stereotype.Service;

@Service("PessoaSalvarCmd")
public class SalvarCmd implements Command {

	@Override
	public boolean execute(Context context) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("Salvando pessoa...");
		return false;
	}

}
