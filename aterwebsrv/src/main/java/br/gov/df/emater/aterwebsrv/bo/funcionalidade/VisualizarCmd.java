package br.gov.df.emater.aterwebsrv.bo.funcionalidade;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.FuncionalidadeDao;
import br.gov.df.emater.aterwebsrv.modelo.EntidadeBase;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Funcionalidade;
import br.gov.df.emater.aterwebsrv.modelo.sistema.FuncionalidadeComando;
import br.gov.df.emater.aterwebsrv.modelo.sistema.ModuloFuncionalidade;

@Service("FuncionalidadeVisualizarCmd")
public class VisualizarCmd extends _Comando {

	@Autowired
	private FuncionalidadeDao dao;

	@PersistenceContext(unitName = EntidadeBase.PERSISTENCE_UNIT)
	private EntityManager em;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Integer id = (Integer) contexto.getRequisicao();
		Funcionalidade funcionalidade = dao.findOne(id);

		if (funcionalidade == null) {
			throw new BoException("Registro não localizado");
		}
		// fetch nas dependencias
		for (ModuloFuncionalidade moduloFuncionalidade : funcionalidade.getModuloFuncionalidadeList()) {
			moduloFuncionalidade.setFuncionalidade(null);
			moduloFuncionalidade.setModulo(moduloFuncionalidade.getModulo().infoBasica());
		}
		for (FuncionalidadeComando funcionalidadeComando : funcionalidade.getFuncionalidadeComandoList()) {
			funcionalidadeComando.setFuncionalidade(null);
			funcionalidadeComando.setComando(funcionalidadeComando.getComando().infoBasica());
		}
		em.detach(funcionalidade);

		Funcionalidade result = funcionalidade.infoBasica();
		result.setModuloFuncionalidadeList(funcionalidade.getModuloFuncionalidadeList());
		result.setFuncionalidadeComandoList(funcionalidade.getFuncionalidadeComandoList());

		contexto.setResposta(result);

		return false;
	}
}