package br.gov.df.emater.aterwebsrv.bo.perfil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.PerfilDao;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;
import br.gov.df.emater.aterwebsrv.modelo.sistema.PerfilFuncionalidadeComando;

@Service("PerfilVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private PerfilDao dao;

	@PersistenceContext(unitName = EntidadeBase.PERSISTENCE_UNIT)
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Perfil perfil = dao.findOne(id);

		if (perfil == null) {
			throw new BoException("Registro não localizado");
		}
		// fetch nas dependencias
		for (PerfilFuncionalidadeComando perfilFuncionalidadeComando : perfil.getPerfilFuncionalidadeComandoList()) {
			perfilFuncionalidadeComando.setPerfil(null);
			perfilFuncionalidadeComando.getFuncionalidadeComando().setFuncionalidade(perfilFuncionalidadeComando.getFuncionalidadeComando().getFuncionalidade().infoBasica());
			perfilFuncionalidadeComando.getFuncionalidadeComando().setComando(perfilFuncionalidadeComando.getFuncionalidadeComando().getComando().infoBasica());
		}
		em.detach(perfil);

		Perfil result = perfil.infoBasica();
		result.setPerfilFuncionalidadeComandoList(new ArrayList<PerfilFuncionalidadeComando>(perfil.getPerfilFuncionalidadeComandoList()));

		// ordenar as funcionalidades
		Collections.sort(result.getPerfilFuncionalidadeComandoList(), new Comparator<PerfilFuncionalidadeComando>() {
			@Override
			public int compare(PerfilFuncionalidadeComando o1, PerfilFuncionalidadeComando o2) {
				return o1.getFuncionalidadeComando().getComando().getNome().compareTo(o2.getFuncionalidadeComando().getComando().getNome());
			}
		});
		Collections.sort(result.getPerfilFuncionalidadeComandoList(), new Comparator<PerfilFuncionalidadeComando>() {
			@Override
			public int compare(PerfilFuncionalidadeComando o1, PerfilFuncionalidadeComando o2) {
				return o1.getFuncionalidadeComando().getFuncionalidade().getNome().compareTo(o2.getFuncionalidadeComando().getFuncionalidade().getNome());
			}
		});

		contexto.setResposta(result);

		return false;
	}
}