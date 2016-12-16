package br.gov.df.emater.aterwebsrv.bo_planejamento.planejamento;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao_planejamento.planejamento.MetaTaticaDao;
import br.gov.df.emater.aterwebsrv.dto_planejamento.PlanejamentoCadFiltroDto;

@Service("PlanejamentoRetornaMetaTaticaCmd")
public class RetornaMetaTaticaCmd extends _Comando {

	@Autowired
	private MetaTaticaDao dao;

	@SuppressWarnings("unchecked")
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		PlanejamentoCadFiltroDto filtro = (PlanejamentoCadFiltroDto) contexto.getRequisicao();
		List<Object[]> result = (List<Object[]>) dao.filtrar(filtro);
		contexto.setResposta(result);
		return false;
	}

}