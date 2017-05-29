package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.Pessoa;

@Service("MesclarPessoasCmd")
public class MesclarPessoasCmd extends _Comando {

	@Autowired
	private PessoaDao dao;


	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		List<Integer> filtro = (List<Integer>) contexto.getRequisicao();
		
        for (Integer pessoaId :  filtro ) {
        	Pessoa pessoa = dao.findOne(pessoaId);
            contexto.setResposta(pessoa);
        }        	
		
		return false;
	}
}