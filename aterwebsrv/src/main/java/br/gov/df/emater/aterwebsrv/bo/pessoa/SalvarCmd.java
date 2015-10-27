package br.gov.df.emater.aterwebsrv.bo.pessoa;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("PessoaSalvarCmd")
public class SalvarCmd extends _Comando {
	
	@Autowired
	private PessoaDao dao;

	@Override
	public boolean executar(_Contexto context) throws Exception {
		dao.save((Pessoa) context.getRequisicao());
		return true;
	}

}