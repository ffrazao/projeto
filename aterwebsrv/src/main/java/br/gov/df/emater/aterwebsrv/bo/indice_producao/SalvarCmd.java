package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaComposicaoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoFormaComposicao;

@Service("IndiceProducaoSalvarCmd")
public class SalvarCmd extends _Comando {

	public SalvarCmd() {
	}

	@Autowired
	private ProducaoDao dao;

	@Autowired
	private ProducaoFormaDao producaoFormaDao;
	
	@Autowired
	private ProducaoFormaComposicaoDao producaoFormaComposicaoDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Producao result = (Producao) contexto.getRequisicao();
		if (result.getId() == null) {
			result.setInclusaoUsuario(getUsuario(contexto.getUsuario().getName()));
		}
		result.setAlteracaoUsuario(getUsuario(contexto.getUsuario().getName()));

		dao.save(result);

		for (ProducaoForma producaoForma : result.getProducaoFormaList()) {
			producaoForma.setProducao(result);
			if (producaoForma.getId() == null) {
				producaoForma.setInclusaoUsuario(getUsuario(contexto.getUsuario().getName()));
			}
			producaoForma.setAlteracaoUsuario(getUsuario(contexto.getUsuario().getName()));

			producaoFormaDao.save(producaoForma);
			
			for (ProducaoFormaComposicao producaoFormaComposicao: producaoForma.getProducaoFormaComposicaoList()) {
				producaoFormaComposicao.setProducaoForma(producaoForma);
				producaoFormaComposicaoDao.save(producaoFormaComposicao);
			}
		}

		dao.flush();

		contexto.setResposta(result.getId());
		return true;
	}

}