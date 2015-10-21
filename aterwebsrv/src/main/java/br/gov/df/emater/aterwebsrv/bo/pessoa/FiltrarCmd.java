package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("PessoaFiltrarCmd")
public class FiltrarCmd extends _Comando {
	
	@Autowired
	private PessoaDao pessoaDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		System.out.println("Filtrando pessoa...");
		List<Pessoa> result = null;
		result = pessoaDao.findAll();
		contexto.setResposta(result);
		return false;
	}

}
