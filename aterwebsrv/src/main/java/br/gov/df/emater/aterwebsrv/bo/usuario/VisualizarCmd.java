package br.gov.df.emater.aterwebsrv.bo.usuario;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service("UsuarioVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private UsuarioDao dao;

	@Autowired
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Usuario usuario = dao.findOne(id);

		if (usuario == null) {
			throw new BoException("Registro não localizado");
		}
		// fetch nas dependencias
		// for (ModuloFuncionalidade moduloFuncionalidade:
		// funcionalidade.getModuloFuncionalidadeList()) {
		// moduloFuncionalidade.setFuncionalidade(null);
		// moduloFuncionalidade.setModulo(moduloFuncionalidade.getModulo().infoBasica());
		// }
		em.detach(usuario);

		Usuario result = usuario.infoBasica();
		// result.setModuloFuncionalidadeList(funcionalidade.getModuloFuncionalidadeList());

		contexto.setResposta(result);

		return false;
	}
}