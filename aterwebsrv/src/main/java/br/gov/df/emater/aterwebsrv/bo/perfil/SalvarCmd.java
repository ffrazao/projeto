package br.gov.df.emater.aterwebsrv.bo.perfil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.sistema.PerfilDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.PerfilFuncionalidadeComandoDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Perfil;
import br.gov.df.emater.aterwebsrv.modelo.sistema.PerfilFuncionalidadeComando;

@Service("PerfilSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private PerfilDao dao;

	@Autowired
	private PerfilFuncionalidadeComandoDao perfilFuncionalidadeComandoDao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Perfil result = (Perfil) contexto.getRequisicao();

		result.setCodigo(result.getCodigo().toUpperCase().replaceAll("\\s", "_"));

		Perfil salvo = null;
		if (result.getId() != null) {
			salvo = dao.findOne(result.getId());
		}

		// remover os itens descartados
		if (salvo != null) {
			if (salvo.getPerfilFuncionalidadeComandoList() != null) {
				for (PerfilFuncionalidadeComando perfilFuncionalidadeComandoSalvo : salvo.getPerfilFuncionalidadeComandoList()) {
					boolean encontrou = false;
					if (result.getPerfilFuncionalidadeComandoList() != null) {
						for (PerfilFuncionalidadeComando perfilFuncionalidadeComando : result.getPerfilFuncionalidadeComandoList()) {
							if (perfilFuncionalidadeComando.getFuncionalidadeComando().getId().equals(perfilFuncionalidadeComandoSalvo.getFuncionalidadeComando().getId())) {
								perfilFuncionalidadeComando.setId(perfilFuncionalidadeComandoSalvo.getId());
								encontrou = true;
								break;
							}
						}
					}
					if (!encontrou) {
						perfilFuncionalidadeComandoDao.delete(perfilFuncionalidadeComandoSalvo);
					}
				}
			}
		}

		// preparar para salvar
		dao.save(result);

		if (result.getPerfilFuncionalidadeComandoList() != null) {
			for (PerfilFuncionalidadeComando perfilFuncionalidadeComando : result.getPerfilFuncionalidadeComandoList()) {
				perfilFuncionalidadeComando.setPerfil(result);
				perfilFuncionalidadeComandoDao.save(perfilFuncionalidadeComando);
			}
		}

		contexto.setResposta(result.getId());
		return false;
	}
}