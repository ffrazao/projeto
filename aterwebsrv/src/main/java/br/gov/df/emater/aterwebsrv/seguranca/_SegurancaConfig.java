package br.gov.df.emater.aterwebsrv.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import br.gov.df.emater.aterwebsrv.seguranca.filter.AutenticacaoFilter;
import br.gov.df.emater.aterwebsrv.seguranca.filter.LoginFilter;

@EnableWebSecurity
@Configuration
@Order(1)
public class _SegurancaConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AutenticacaoProvider customAuthenticationProvider;

	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

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
		auth.authenticationProvider(customAuthenticationProvider);
	}

	// @Bean(name = "customAuthenticationProvider")
	// public CustomAuthenticationProvider getCustomAuthenticationProvider() {
	// // Customização da autenticação do usuário
	// CustomAuthenticationProvider result = new CustomAuthenticationProvider();
	// result.setUserDetailsService(userDetailsService);
	// result.setSaltSource(null);
	// result.setPasswordEncoder(new BCryptPasswordEncoder());
	// return result;
	// }

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.exceptionHandling().and().anonymous().and().servletApi().and() // .headers().cacheControl().and()
				.authorizeRequests()

				// allow anonymous resource requests
				.antMatchers("/").permitAll().antMatchers("/*").permitAll()

				.antMatchers("/casa/**").permitAll().antMatchers("/contrato/**").permitAll().antMatchers("/css/**").permitAll().antMatchers("/directive/**").permitAll().antMatchers("/img/**").permitAll().antMatchers("/js/**").permitAll().antMatchers("/login/**").permitAll()
//				.antMatchers("/api/esqueci-senha/**").permitAll().antMatchers("/node_modules/**").permitAll().antMatchers("/pessoa/**").permitAll().antMatchers("/propriedade/**").permitAll().antMatchers("/service/**").permitAll().antMatchers("/resources/**").permitAll()
				.antMatchers("/api/esqueci-senha/**").permitAll().antMatchers("/relatorio/**").permitAll().antMatchers("/service/**").permitAll().antMatchers("/resources/**").permitAll()

				// allow anonymous POSTs to login
				.antMatchers(HttpMethod.POST, "/api/login").permitAll()

				// defined Admin only API area
				.antMatchers("/admin/**").hasRole("ADMIN")

				// all other request need to be authenticated
				.anyRequest().fullyAuthenticated().and()

				// custom JSON based authentication by POST of
				// {"username":"<name>","password":"<password>"} which sets the
				// token header upon authentication
				.addFilterBefore(new LoginFilter("/api/login", tokenAuthenticationService, authenticationManager()), UsernamePasswordAuthenticationFilter.class)

				// custom Token based authentication based on the header
				// previously given to the client
				.addFilterBefore(new AutenticacaoFilter(tokenAuthenticationService), UsernamePasswordAuthenticationFilter.class);
	}

}
