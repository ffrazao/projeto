package br.gov.df.emater.aterwebsrv.seguranca;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class _SecurityConfiguration extends WebSecurityConfigurerAdapter {
	
	public _SecurityConfiguration() {
		super(true);
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

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Autowired
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication().withUser("marissa").password("koala").roles("USER").and().withUser("paul")
//                .password("emu").roles("USER");
    	auth.userDetailsService(userDetailsService).passwordEncoder(new VerificaSenhaEncoder());
    }
    
	@Autowired
	private TokenAuthenticationService tokenAuthenticationService;

	@Autowired
	private UserDetailsService userDetailsService;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/webjars/**", "/images/**", "/oauth/uncache_approvals", "/oauth/cache_approvals");
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
                 http
            .authorizeRequests()
                .antMatchers("/login").permitAll()
                .anyRequest().hasRole("USER")
                .and()
            .exceptionHandling()
                .accessDeniedPage("/login?authorization_error=true")
                .and()
            // TODO: put CSRF protection back into this endpoint
            .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable()
            .logout()
            	.logoutUrl("/logout")
                .logoutSuccessUrl("/login")
                .and()
            .formLogin()
            	.loginProcessingUrl("/login")
                .failureUrl("/login?authentication_error=true")
                .loginPage("/login");
        // @formatter:on
    }
}
