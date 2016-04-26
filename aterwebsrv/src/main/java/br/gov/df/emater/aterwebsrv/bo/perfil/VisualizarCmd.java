package br.gov.df.emater.aterwebsrv.bo.perfil;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.PerfilDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;

@Service("PerfilVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private PerfilDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Perfil perfil = dao.findOne(id);

		if (perfil == null) {
			throw new BoException("Registro não localizado");
		}
		// fetch nas dependencias
		// for (ModuloFuncionalidade moduloFuncionalidade:
		// funcionalidade.getModuloFuncionalidadeList()) {
		// moduloFuncionalidade.setFuncionalidade(null);
		// moduloFuncionalidade.setModulo(moduloFuncionalidade.getModulo().infoBasica());
		// }
		em.detach(perfil);

		Perfil result = perfil.infoBasica();
		// result.setModuloFuncionalidadeList(funcionalidade.getModuloFuncionalidadeList());

		contexto.setResposta(result);

		return false;
	}
}