package br.gov.df.emater.aterwebsrv.bo.pessoa;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaDao;
import br.gov.df.emater.aterwebsrv.modelo.dto.PessoaCadFiltroDto;

@Service("PessoaFiltrarCmd")
public class FiltrarCmd extends _Comando {
	
	@Autowired
	private PessoaDao pessoaDao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		System.out.println("Filtrando pessoa...");
		PessoaCadFiltroDto filtro = (PessoaCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) pessoaDao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}
