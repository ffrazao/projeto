package br.gov.df.emater.aterwebsrv.bo.projeto_credito_rural;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.projeto_credito_rural.ProjetoCreditoRuralDao;
import br.gov.df.emater.aterwebsrv.modelo.projeto_credito_rural.ProjetoCreditoRural;

@Service("ProjetoCreditoRuralVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private ProjetoCreditoRuralDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		ProjetoCreditoRural result = dao.findOne(id);

		if (result == null) {
			throw new BoException("Registro não localizado");
		}

		em.detach(result);
		contexto.setResposta(result);

		return false;
	}
}