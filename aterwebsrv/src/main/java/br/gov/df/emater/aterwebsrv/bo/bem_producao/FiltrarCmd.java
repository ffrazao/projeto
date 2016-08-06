package br.gov.df.emater.aterwebsrv.bo.bem_producao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemDao;
import br.gov.df.emater.aterwebsrv.dto.BemProducaoCadFiltroDto;

@Service("BemProducaoFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private BemDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		BemProducaoCadFiltroDto filtro = (BemProducaoCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}
