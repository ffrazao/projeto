package br.gov.df.emater.aterwebsrv.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
//@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@Order(1)
public class _SegurancaConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Autowired
	private UserDetailsService userDetailsService;

	public _SegurancaConfig() {
		super(true);
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(new VerificaSenhaEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.exceptionHandling().and()
				.anonymous().and()
				.servletApi().and()
				.headers().cacheControl().and()
				.headers().and()
				.authorizeRequests()
								
				//allow anonymous resource requests
				.antMatchers("/").permitAll()
				.antMatchers("/favicon.ico").permitAll()
				.antMatchers("/resources/**").permitAll()
				
				//allow anonymous POSTs to login
				.antMatchers(HttpMethod.POST, "/api/login").permitAll()
				
				//allow anonymous GETs to API
				.antMatchers(HttpMethod.GET, "/api/**").permitAll()
				
				//defined Admin only API area
				.antMatchers("/admin/**").hasRole("ADMIN")
				
				//all other request need to be authenticated
				.anyRequest().hasRole("USER").and()				
		
				// custom JSON based authentication by POST of {"username":"<name>","password":"<password>"} which sets the token header upon authentication
				.addFilterBefore(new StatelessLoginFilter("/api/login", tokenAuthenticationService, userDetailsService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

				// custom Token based authentication based on the header previously given to the client
				.addFilterBefore(new StatelessAuthenticationFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	protected UserDetailsService userDetailsService() {
		return userDetailsService;
	}
}