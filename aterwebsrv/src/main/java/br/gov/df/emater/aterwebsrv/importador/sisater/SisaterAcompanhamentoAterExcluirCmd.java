package br.gov.df.emater.aterwebsrv.importador.sisater;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeChaveSisaterDao;
import br.gov.df.emater.aterwebsrv.dao.atividade.AtividadeDao;
import br.gov.df.emater.aterwebsrv.importador.apoio.ConexaoFirebird.DbSater;

@Service
public class SisaterAcompanhamentoAterExcluirCmd extends _Comando {

	@Autowired
	private AtividadeDao atividadeDao;

	@Autowired
	private AtividadeChaveSisaterDao atividadeChaveSisaterDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {

		// gestão da transação com o banco de dados
		PlatformTransactionManager transactionManager = (PlatformTransactionManager) contexto.get("transactionManager");
		DefaultTransactionDefinition transactionDefinition = (DefaultTransactionDefinition) contexto.get("transactionDefinition");

		String base = String.format("%s%%",  ((DbSater) contexto.get("base")).name());
		
		List<Integer> atividadeChaveSisaterList = atividadeChaveSisaterDao.findAtividadePorChaveSisater(base);
		for (Integer id: atividadeChaveSisaterList) {
			TransactionStatus transactionStatus = transactionManager.getTransaction(transactionDefinition);
			try {					
				atividadeDao.delete(id);
				atividadeDao.flush();
				transactionManager.commit(transactionStatus);
			} catch (Exception e) {
				logger.error(e);
				transactionManager.rollback(transactionStatus);
			}
		}
		
		if (logger.isInfoEnabled()) {
			logger.info(String.format("Base de atividades [%s] limpa", base));
		}

		return false;
	}

}