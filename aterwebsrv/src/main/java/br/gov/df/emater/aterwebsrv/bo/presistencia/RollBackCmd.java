package br.gov.df.emater.aterwebsrv.bo.presistencia;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;

@Service("PersistenciaRollBackCmd")
public class RollBackCmd extends _Comando {
	
	@Autowired
	private EntityManager entityManager;

	@Transactional
	@Override
	public boolean executar(_Contexto context) throws Exception {
		entityManager.getTransaction().rollback();
		return false;
	}
	
}	
