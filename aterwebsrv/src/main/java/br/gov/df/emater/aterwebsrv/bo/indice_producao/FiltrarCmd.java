package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.dto.IndiceProducaoCadFiltroDto;

@Service("IndiceProducaoFiltrarCmd")
public class FiltrarCmd extends _Comando {

	@Autowired
	private ProducaoDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		IndiceProducaoCadFiltroDto filtro = (IndiceProducaoCadFiltroDto) contexto.getRequisicao();
		List<Object[]> result = null;
		result = dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}
