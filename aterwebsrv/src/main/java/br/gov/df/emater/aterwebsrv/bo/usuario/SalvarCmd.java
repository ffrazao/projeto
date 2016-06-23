package br.gov.df.emater.aterwebsrv.bo.usuario;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.bo._SalvarCmd;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioPerfilDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.modelo.sistema.UsuarioPerfil;

@Service("UsuarioSalvarCmd")
public class SalvarCmd extends _SalvarCmd {

	@Autowired
	private UsuarioDao dao;

	// @Autowired
	// private FacadeBo facadeBo;

	@Autowired
	private UsuarioPerfilDao usuarioPerfilDao;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario result = (Usuario) contexto.getRequisicao();

		// captar o registro de atualização da tabela
		logAtualizar(result, contexto);

		Usuario salvo = null;
		String enviaEmail = null;

		// flag para enviar automaticamente a senha ao usuário ou não
		Boolean enviarEmailUsuario = (Boolean) contexto.get("enviarEmailUsuario");
		if (enviarEmailUsuario == null) {
			enviarEmailUsuario = true;
		}

		if (result.getPessoaEmail() == null || result.getPessoaEmail().getEmail() == null || StringUtils.isBlank(result.getPessoaEmail().getEmail().getEndereco())) {
			throw new BoException("E-mail do usuário não informado!");
		}

		if (result.getId() == null) {
			// novos usuários
			enviaEmail = result.getPessoaEmail().getEmail().getEndereco();
			if (dao.findByPessoaEmailEmailEndereco(enviaEmail) != null) {
				throw new BoException("O e-mail [%s] já esta em uso por outro usuário do sistema. Informe um outro e-mail!", result.getPessoaEmail().getEmail().getEndereco());
			}
		} else {
			// usuários existentes
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

		// enviar senha para novos usuários
		if (enviarEmailUsuario && enviaEmail != null) {
			// FIXME avaliar necessidade deste codigo
			// facadeBo.segurancaEsqueciSenha(enviaEmail);
		}

		contexto.setResposta(result.getId());
		return false;
	}
}