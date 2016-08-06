package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.FuncionalidadeDao;
import br.gov.df.emater.aterwebsrv.dto.FuncionalidadeCadFiltroDto;

@Service("FuncionalidadeFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private FuncionalidadeDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		FuncionalidadeCadFiltroDto filtro = (FuncionalidadeCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}