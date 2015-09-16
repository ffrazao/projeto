package br.gov.df.emater.aterwebsrv.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import br.gov.df.emater.aterwebsrv.bo.FacadeBo;

/**
 * Classe customizada para customizar a autentica��o de usu�rios
 * 
 * @author frazao
 * 
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private FacadeBo servicoFacade;

	@Override
	public Authentication authenticate(Authentication autenticacao) throws AuthenticationException {
		// testeDao.count();
		// return testeBo.autenticaUsuario(autenticacao);
		return null;
	}

	@Override
	public boolean supports(Class<?> arg0) {
		return true;
	}
}