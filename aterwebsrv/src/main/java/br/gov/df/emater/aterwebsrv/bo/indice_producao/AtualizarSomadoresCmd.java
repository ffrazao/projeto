package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.ProducaoFormaDao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.Producao;
import br.gov.df.emater.aterwebsrv.modelo.indice_producao.ProducaoForma;

@Service("AtualizarSomadoresCmd")
public class AtualizarSomadoresCmd extends _Comando {

	@Autowired
	private ProducaoFormaDao producaoFormaDao;

	@Autowired
	private ProducaoCalculo calculo;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer producaoId = (Integer) contexto.getResposta();

		calculo.abreProducao(producaoId);

		calculo.calcular(false);

		if (calculo.getProducaoProdutorList() != null) {
			// alualizar as produções
			for (Producao producao : calculo.getProducaoProdutorList()) {
				for (ProducaoForma producaoForma : producao.getProducaoFormaList()) {
					producaoFormaDao.save(producaoForma);
				}
			}
		}

		// atualizar as previsôes de produção
		for (ProducaoForma producaoForma : calculo.getProducao().getProducaoFormaList()) {
			producaoFormaDao.save(producaoForma);
		}

		return false;
	}

}