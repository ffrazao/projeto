package br.gov.df.emater.aterwebsrv.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class _SegurancaConfig extends WebSecurityConfigurerAdapter {

	public _SegurancaConfig() {
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// trocar a linha a seguir
		http.authorizeRequests().anyRequest().access("permitAll").and().csrf().csrfTokenRepository(csrfTokenRepository()).and().addFilterAfter(new CsrfHeaderFilter(), CsrfFilter.class);
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/resources/**");
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
	}

	@Bean(name = "customAuthenticationProvider")
	public CustomAuthenticationProvider setupCustomAuthenticationProvider() {
		// Customização da autenticação do usuário
		CustomAuthenticationProvider result = new CustomAuthenticationProvider();
		return result;
	}

	@Bean(name = "customUsernamePasswordAuthenticationFilter")
	public CustomUsernamePasswordAuthenticationFilter setupCustomUsernamePasswordAuthenticationFilter() throws Exception {
		// Necessario para habilitar a captação do campo administrador da tela
		// de login
		CustomUsernamePasswordAuthenticationFilter result = new CustomUsernamePasswordAuthenticationFilter();
		result.setAuthenticationManager(authenticationManagerBean());
		result.setAuthenticationSuccessHandler(setupSuccessHandler());
		result.setAuthenticationFailureHandler(setupFailureHandler());
		return result;
	}

	@Bean
	public DefaultWebSecurityExpressionHandler setupDefaultWebSecurityExpressionHandler() {
		// evaluating security expressions
		DefaultWebSecurityExpressionHandler result = new DefaultWebSecurityExpressionHandler();
		return result;
	}

	@Bean(name = "failureHandler")
	public SimpleUrlAuthenticationFailureHandler setupFailureHandler() {
		// Necessario para habilitar a captação do campo administrador da tela
		// de login
		SimpleUrlAuthenticationFailureHandler result = new SimpleUrlAuthenticationFailureHandler();
		result.setDefaultFailureUrl("/login?login_error=true");
		return result;
	}

	@Bean(name = "successHandler")
	public SavedRequestAwareAuthenticationSuccessHandler setupSuccessHandler() {
		// Necessario para habilitar a captação do campo administrador da tela
		// de login
		SavedRequestAwareAuthenticationSuccessHandler result = new SavedRequestAwareAuthenticationSuccessHandler();
		result.setDefaultTargetUrl("/");
		return result;
	}
}