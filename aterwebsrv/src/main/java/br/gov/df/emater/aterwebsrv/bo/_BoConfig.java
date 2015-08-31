package br.gov.df.emater.aterwebsrv.bo;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.web.WebAppConfiguration;

@Configuration
@ComponentScan(basePackages = { "br.gov.df.emater.aterwebsrv.bo" })
@WebAppConfiguration
public class _BoConfig {
	
	public _BoConfig() {
		System.out.println("novo _BoConfig");
	}

}
