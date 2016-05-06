package br.gov.df.emater.aterwebsrv.importador;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;
import br.gov.df.emater.aterwebsrv.bo._Comando;
import br.gov.df.emater.aterwebsrv.bo._Contexto;
import br.gov.df.emater.aterwebsrv.dao.sistema.PerfilDao;
import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.ferramenta.Criptografia;
import br.gov.df.emater.aterwebsrv.modelo.dominio.Confirmacao;
import br.gov.df.emater.aterwebsrv.modelo.pessoa.PessoaJuridica;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.modelo.sistema.UsuarioPerfil;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

@Service
public class EmaterUsuarioCmd extends _Comando {

	@Autowired
	private PerfilDao perfilDao;

	@Autowired
	private UsuarioDao usuarioDao;

	@Override
	public boolean executar(_Contexto contexto) throws Exception {
		FacadeBo facadeBo = (FacadeBo) contexto.get("facadeBo");

		PessoaJuridica emater = (PessoaJuridica) contexto.get("emater");

		Usuario usuario = usuarioDao.findOneByPessoa(emater);

		if (usuario == null) {
			usuario = new Usuario();
			usuario.setPessoa(emater);
			usuario = (Usuario) facadeBo.usuarioNovo(null, usuario).getResposta();
			usuario.setUsername("emater");
			Set<UsuarioPerfil> authorities = new HashSet<UsuarioPerfil>();
			authorities.add(new UsuarioPerfil(perfilDao.findOneByCodigo("ADMIN"), Confirmacao.S));
			usuario.setAuthorities(authorities);
			usuario.setId((Integer) facadeBo.usuarioSalvar(null, usuario).getResposta());
			usuario = usuarioDao.findOne(usuario.getId());
			usuario.setPassword(Criptografia.MD5(usuario.getId() + "emater"));

			usuarioDao.save(usuario);

			contexto.setUsuario(new UserAuthentication(usuario));
		}

		contexto.put("ematerUsuario", usuario);

		return false;
	}

}