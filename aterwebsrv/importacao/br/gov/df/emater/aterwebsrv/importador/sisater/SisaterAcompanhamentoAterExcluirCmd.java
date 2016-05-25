package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;

@Service
public class SisaterAcompanhamentoAterExcluirCmd extends _Comando {

	@Autowired
	private AtividadeDao atividadeDao;

	private PlatformTransactionManager transactionManager;
	
	private TransactionStatus transactionStatus;


	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		
		transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		
		transactionStatus = (TransactionStatus) contexto.get("transactionStatus");

		try {
			atividadeDao.deleteAll();
			atividadeDao.flush();
			
			transactionManager.commit(transactionStatus);
			transactionStatus
			transactionStatus = transactionManager.getTransaction(arg0) 
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
		}

		return false;
	}

}