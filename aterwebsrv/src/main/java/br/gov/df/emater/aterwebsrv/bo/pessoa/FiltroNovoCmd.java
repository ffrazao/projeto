package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.apache.commons.chain.Command;
import org.apache.commons.chain.Context;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.dominio.PessoaTipo;
import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;

@Service("PessoaFiltroNovoCmd")
public class FiltroNovoCmd implements Command {
	
	@Autowired
	private PessoaDao pessoaDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean execute(Context context) throws Exception {
		System.out.println(pessoaDao.count());
		
		PessoaCadFiltroDto filtro = new PessoaCadFiltroDto();
		filtro.setTipoPessoa(PessoaTipo.PF);
		context.put("resultado", filtro);
		System.out.println(filtro.toString());
		return false;
	}

}
