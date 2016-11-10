package br.gov.df.emater.aterwebsrv.bo.manual_online;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.ManualOnlineDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.ManualOnline;

@Service("ManualOnlineVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private ManualOnlineDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		ManualOnline manualOnline = dao.findOne(id);

		if (manualOnline == null) {
			throw new BoException("Registro não localizado");
		}
		// fetch nas dependencias
		em.detach(manualOnline);

		ManualOnline result = manualOnline.infoBasica();

		contexto.setResposta(result);

		return false;
	}
}