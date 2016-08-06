package br.gov.df.emater.aterwebsrv.bo.bem_classificacao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.BemClassificacaoDao;
import br.gov.df.emater.aterwebsrv.dto.indice_producao.BemClassificacaoCadFiltroDto;

@Service("BemClassificacaoFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private BemClassificacaoDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		BemClassificacaoCadFiltroDto filtro = (BemClassificacaoCadFiltroDto) contexto.getRequisicao();
		List<Object> result = null;
		result = (List<Object>) dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}
