package br.gov.df.emater.aterwebsrv.bo.indice_producao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoBemClassificadoDao;
import br.gov.df.emater.aterwebsrv.dao.indice_producao.IpaProducaoDao;

@Service("IndiceProducaoExcluirCmd")
public class ExcluirCmd extends _Comando {

	@Autowired
	private IpaProducaoDao ipaProducaoDao;
	@Autowired
	private IpaProducaoBemClassificadoDao ipbcDao;
	
	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer result = (Integer) contexto.getRequisicao();
				
			ipbcDao.deleteBem(result);
			ipaProducaoDao.deleteIpa(result);

		contexto.setResposta(result);
		return false;
	}

}