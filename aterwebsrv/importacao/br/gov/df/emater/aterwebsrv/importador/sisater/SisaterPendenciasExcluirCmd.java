package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.ater.PropriedadeRuralPendenciaDao;
import br.gov.df.emater.aterwebsrv.dao.pessoa.PessoaPendenciaDao;

@Service
public class SisaterPendenciasExcluirCmd extends _Comando {

	@Autowired
	private PessoaPendenciaDao pessoaPendenciaDao;

	@Autowired
	private PropriedadeRuralPendenciaDao propriedadeRuralPendenciaDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		PlatformTransactionManager transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		DefaultTransactionDefinition transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

		TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
		try {
			pessoaPendenciaDao.deleteAll();
			pessoaPendenciaDao.flush();
			
			propriedadeRuralPendenciaDao.deleteAll();
			propriedadeRuralPendenciaDao.flush();

			transactionManager.commit(transactionStatus);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			transactionManager.rollback(transactionStatus);
		}
		return false;
	}

}