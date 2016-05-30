package br.gov.df.emater.aterwebsrv.importador.sisater;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;

@Service
public class SisaterAcompanhamentoAterExcluirCmd extends _Comando {

	@Autowired
	private AtividadeDao atividadeDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		// gestão da transação com o banco de dados
		PlatformTransactionManager transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		DefaultTransactionDefinition transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

		TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
		try {
			atividadeDao.deleteAll();
			atividadeDao.flush();

			transactionManager.commit(transactionStatus);
		} catch (Exception e) {
			logger.error(e);
			e.printStackTrace();
			transactionManager.rollback(transactionStatus);
		}

		return false;
	}

}