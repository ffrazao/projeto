package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;

@Service("IndiceProducaoExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private ProducaoDao dao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Producao producao = (Producao) contexto.getRequisicao();
		producao = dao.findOne(producao.getId());

		List<Producao> produtorProducaoList = dao.findByAnoAndBemAndPropriedadeRuralComunidadeUnidadeOrganizacional(producao.getAno(), producao.getBem(), producao.getUnidadeOrganizacional());

		if (produtorProducaoList != null) {
			for (Producao p : produtorProducaoList) {
				dao.delete(p);
			}
		}
		dao.delete(producao);

		contexto.setResposta(producao);
		return false;
	}

}