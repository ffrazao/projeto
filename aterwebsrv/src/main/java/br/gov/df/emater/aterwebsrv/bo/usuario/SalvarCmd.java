package br.gov.df.emater.aterwebsrv.bo.usuario;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.BoException;
import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
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

	@Autowired
	private FacadeBo facadeBo;

	public SalvarCmd() {
	}

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		Usuario result = (Usuario) contexto.getRequisicao();
		Usuario salvo = null;
		String enviaEmail = null;
		
		// flag para enviar automaticamente a senha ao usuário ou não
		Boolean enviarEmailUsuario = (Boolean) contexto.get("enviarEmailUsuario");
		if (enviarEmailUsuario == null) {
			enviarEmailUsuario = true;
		}

		if (result.getId() == null) {
			// novos usuários
			result.setUsuarioInclusao(getUsuario(contexto.getUsuario() == null ? null : contexto.getUsuario().getName()));
			result.setInclusaoData(Calendar.getInstance());
			if (result.getPessoaEmail() == null || result.getPessoaEmail().getEmail() == null || result.getPessoaEmail().getEmail().getEndereco() == null || result.getPessoaEmail().getEmail().getEndereco().trim().length() == 0) {
				throw new BoException("E-mail do usuário não informado!");
			}
			enviaEmail = result.getPessoaEmail().getEmail().getEndereco();
			if (dao.findByPessoaEmailEmailEndereco(enviaEmail) != null) {
				throw new BoException("O e-mail deste usuário já esta em uso por outra conta do sistema. Utilize um outro e-mail!");
			}
		} else {
			// usuários existentes
			result.setUsuarioInclusao(getUsuario(result.getUsuarioInclusao() == null ? null : result.getUsuarioInclusao().getUsername()));
			salvo = dao.findOne(result.getId());
		}
		result.setUsuarioAlteracao(getUsuario(contexto.getUsuario() == null ? null : contexto.getUsuario().getName()));
		result.setAlteracaoData(Calendar.getInstance());

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
			//facadeBo.segurancaEsqueciSenha(enviaEmail);
		}

		contexto.setResposta(result.getId());
		return false;
	}
}