package br.gov.df.emater.aterwebsrv.seguranca.filter;

import java.io.IOException;
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

import com.fasterxml.jackson.databind.ObjectMapper;

import br.gov.df.emater.aterwebsrv.modelo.sistema.Usuario;
import br.gov.df.emater.aterwebsrv.seguranca.TokenAuthenticationService;
import br.gov.df.emater.aterwebsrv.seguranca.UserAuthentication;

public class LoginFilter extends AbstractAuthenticationProcessingFilter {

	private static final String MODULO = "MODULO";

	private final TokenAuthenticationService tokenAuthenticationService;
	
	public LoginFilter(String urlMapping, TokenAuthenticationService tokenAuthenticationService, AuthenticationManager authManager) {
		super(new AntPathRequestMatcher(urlMapping, "POST"));
		this.tokenAuthenticationService = tokenAuthenticationService;
		setAuthenticationManager(authManager);
	}

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
		final Usuario user = new ObjectMapper().readValue(request.getInputStream(), Usuario.class);
		final UsernamePasswordAuthenticationToken loginToken = new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword());
		Map<String, Object> details = new HashMap<String, Object>();
		details.put(MODULO, user.getModulo());
		loginToken.setDetails(details);
		return getAuthenticationManager().authenticate(loginToken);
	}

	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {

		// Lookup the complete User object from the database and create an
		// Authentication for it
		final UserAuthentication userAuthentication = new UserAuthentication((Usuario) authentication.getPrincipal());

		// Add the custom token as HTTP header to the response
		tokenAuthenticationService.addAuthentication(response, userAuthentication);

		// Add the authentication to the Security context
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);
		// response.getWriter().close();
		// response.flushBuffer();
		// chain.doFilter(request, response);
	}
}