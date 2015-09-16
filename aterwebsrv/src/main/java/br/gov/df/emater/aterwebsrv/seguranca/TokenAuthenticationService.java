package br.gov.df.emater.aterwebsrv.seguranca;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;

@Service
public class TokenAuthenticationService {

	private static final String AUTH_HEADER_NAME = "X-Auth-Token";
	private static final long TEN_DAYS = 1000 * 60 * 60 * 24 * 1;

	private final TokenHandler tokenHandler;

	@Autowired
	public TokenAuthenticationService(@Value("${token.secret}") String secret) {
		tokenHandler = new TokenHandler(DatatypeConverter.parseBase64Binary(secret));
	}

	public void addAuthentication(HttpServletResponse response, UserAuthentication authentication) {
		final Usuario user = authentication.getDetails();
		user.setExpires(System.currentTimeMillis() + TEN_DAYS);
		response.addHeader(AUTH_HEADER_NAME, tokenHandler.createTokenForUser(user));
	}

	public Authentication getAuthentication(HttpServletRequest request) {
		final String token = request.getHeader(AUTH_HEADER_NAME);
		if (token != null) {
			final Usuario user = tokenHandler.parseUserFromToken(token);
			if (user != null) {
				return new UserAuthentication(user);
			}
		}
		return null;
	}
}
