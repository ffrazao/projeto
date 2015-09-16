package br.gov.df.emater.aterwebsrv.seguranca;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AccountStatusUserDetailsChecker;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.dao.sistema.UsuarioDao;
import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

	@Autowired
	private UsuarioDao userRepo;

	private final AccountStatusUserDetailsChecker detailsChecker = new AccountStatusUserDetailsChecker();

	@Override
	public final Usuario loadUserByUsername(String username) throws UsernameNotFoundException {
		final Usuario user = userRepo.findByNomeUsuario(username);
		if (user == null) {
			throw new UsernameNotFoundException("user not found");
		}
//		Hibernate.initialize(user);
//		Hibernate.initialize(user.getEmpregoList());
//		Hibernate.initialize(user.getLotacaoList());
//		Hibernate.initialize(user.getUsuarioModuloList());
//		Hibernate.initialize(user.getUsuarioPerfilList());
//		Hibernate.initialize(user.getPessoa().getArquivoList());
//		Hibernate.initialize(user.getPessoa().getPessoaRelacionamentos());
		detailsChecker.check(user);
		return user;
	}

}
