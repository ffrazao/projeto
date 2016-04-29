package br.gov.df.emater.aterwebsrv.bo.usuario;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioPerfilDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.modelo.sistema.UsuarioPerfil;

@Service("UsuarioSalvarCmd")
public class SalvarCmd extends _Comando {

	@Autowired
	private UsuarioDao dao;

	@Autowired
	private UsuarioPerfilDao usuarioPerfilDao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario result = (Usuario) contexto.getRequisicao();
		if (result.getId() == null) {
			result.setUsuarioInclusao(getUsuario(contexto.getUsuario().getName()));
			result.setInclusaoData(Calendar.getInstance());
		} else {
			result.setUsuarioInclusao(getUsuario(result.getUsuarioInclusao().getUsername()));
		}
		result.setUsuarioAlteracao(getUsuario(contexto.getUsuario().getName()));
		result.setAlteracaoData(Calendar.getInstance());

		Usuario salvo = null;
		if (result.getId() != null) {
			salvo = dao.findOne(result.getId());
		}

		// remover os itens descartados
		if (salvo != null) {
			if (salvo.getAuthorities() != null) {
				for (UsuarioPerfil authoritiesSalvo : salvo.getAuthorities()) {
					boolean encontrou = false;
					if (result.getAuthorities() != null) {
						for (UsuarioPerfil authorities : result.getAuthorities()) {
							if (authorities.getPerfil().getId().equals(authoritiesSalvo.getPerfil().getId())) {
								authorities.setId(authoritiesSalvo.getId());
								encontrou = true;
								break;
							}
						}
					}
					if (!encontrou) {
						usuarioPerfilDao.delete(authoritiesSalvo);
					}
				}
			}
		}

		// preparar para salvar
		dao.save(result);

		if (result.getAuthorities() != null) {
			for (UsuarioPerfil authorities : result.getAuthorities()) {
				authorities.setUsuario(result);
				usuarioPerfilDao.save(authorities);
			}
		}

		contexto.setResposta(result.getId());
		return false;
	}
}