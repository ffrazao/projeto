package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

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
	
	private PlatformTransactionManager transactionManager;
	
	private TransactionStatus transactionStatus;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		
		transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		
		transactionStatus = (TransactionStatus) contexto.get("transactionStatus");

		try {
			pessoaPendenciaDao.deleteAll();
			propriedadeRuralPendenciaDao.deleteAll();
			
			transactionManager.commit(transactionStatus);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}
		return false;
	}

}