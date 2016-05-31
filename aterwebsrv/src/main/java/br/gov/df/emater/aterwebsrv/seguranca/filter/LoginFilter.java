package br.gov.df.emater.aterwebsrv.seguranca.filter;

import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.TokenAuthenticationService;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final String DATA = "DATA";

	private static final String MODULO = "MODULO";

	private static final String NUMERO_IP = "NUMERO_IP";

	private static final String ORIGIN = "ORIGIN";

	private static final String REFERER = "REFERER";

	private static final String USER_AGENT = "USER_AGENT";

	private final TokenAuthenticationService tokenAuthenticationService;

	public LoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(urlMapping, "POST"));
		this.tokenAuthenticationService = tokenAuthenticationService;
		setAuthenticationManager(authManager);
	}

	@Override
	@Transactional(readOnly = true)
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		final Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		Map<String, Object> details = new HashMap<String, Object>();
		details.put(DATA, Calendar.getInstance());
		details.put(MODULO, user.getModulo());
		details.put(NUMERO_IP, request.getRemoteAddr());
		details.put(USER_AGENT, request.getHeader("user-agent"));
		details.put(ORIGIN, request.getHeader("origin"));
		details.put(REFERER, request.getHeader("referer"));
		loginToken.setDetails(details);
		Authentication result = getAuthenticationManager().authenticate(loginToken);
		return result;
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
		Usuario usuario = (Usuario) authentication.getPrincipal();

		// Lookup the complete User object from the database and create an
		// Authentication for it
		final UserAuthentication userAuthentication = new UserAuthentication(usuario);

		// Add the custom token as HTTP header to the response
		tokenAuthenticationService.addAuthentication(response, userAuthentication);

		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
	}
}