package br.gov.df.emater.aterwebsrv.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.FuncionalidadeComando;
import br.gov.df.emater.aterwebsrv.modelo.sistema.PerfilFuncionalidadeComando;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.modelo.sistema.UsuarioPerfil;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UsuarioDao usuarioDao;

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

	@Override
	@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
	public final Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
		final Usuario usuario = usuarioDao.findByUsername(username);
		if (usuario == null) {
			throw new UsernameNotFoundException("user not found");
		}
		if (usuario.getAuthorities() != null) {
			for (UsuarioPerfil usuarioPerfil: usuario.getAuthorities()) {
				if (usuarioPerfil.getPerfil().getPerfilFuncionalidadeComandoList() != null) {
					for (PerfilFuncionalidadeComando pfc: usuarioPerfil.getPerfil().getPerfilFuncionalidadeComandoList()) {
						if (pfc.getFuncionalidadeComando().getFuncionalidade() != null) {
							for (FuncionalidadeComando fc: pfc.getFuncionalidadeComando().getFuncionalidade().getFuncionalidadeComandoList()) {
							}
						}
						pfc.getFuncionalidadeComando().getComando().getId();
					}
				}
			}
		}
		usuario.getAuthorities().size();
		detailsChecker.check(usuario);
		return usuario;
	}
}
