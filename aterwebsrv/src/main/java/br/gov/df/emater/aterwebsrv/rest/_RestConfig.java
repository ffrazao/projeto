package br.gov.df.emater.aterwebsrv.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.accept.ContentNegotiationManagerFactoryBean;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.DelegatingWebMvcConfiguration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.view.ContentNegotiatingViewResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import com.fasterxml.jackson.annotation.JsonInclude.Include;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "br.gov.df.emater.aterwebsrv" })
public class _RestConfig extends DelegatingWebMvcConfiguration {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	@Bean
	public ContentNegotiatingViewResolver contentViewResolver() throws Exception {
		ContentNegotiationManagerFactoryBean contentNegotiationManager = new ContentNegotiationManagerFactoryBean();
		contentNegotiationManager.addMediaType("json", MediaType.APPLICATION_JSON);

		InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();
		viewResolver.setPrefix("/WEB-INF/jsp/");
		viewResolver.setSuffix(".jsp");

		MappingJackson2JsonView defaultView = new MappingJackson2JsonView();
		defaultView.setExtractValueFromSingleKeyModel(true);

		ContentNegotiatingViewResolver contentViewResolver = new ContentNegotiatingViewResolver();
		contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
		contentViewResolver.setViewResolvers(Arrays.<ViewResolver> asList(viewResolver));
		contentViewResolver.setDefaultViews(Arrays.<View> asList(defaultView));
		return contentViewResolver;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializationInclusion(Include.NON_NULL);
		builder.serializationInclusion(Include.NON_EMPTY);

		builder.failOnEmptyBeans(false);
		builder.failOnUnknownProperties(false);

		builder.indentOutput(true).dateFormat(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
		converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver setupCommonsMultipartResolver() {
		// Manipulação de Arquivos
		CommonsMultipartResolver result = new CommonsMultipartResolver();
		return result;
	}

	@Bean
	public FilterRegistrationBean filterRegistrationBean() {
		// cuidado com o filtro a seguir, pode causar problemas com o spring
		// security
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
		characterEncodingFilter.setEncoding("UTF-8");
		characterEncodingFilter.setForceEncoding(true);
		characterEncodingFilter.setBeanName("CharacterEncodingFilter");
		registrationBean.setFilter(characterEncodingFilter);
		return registrationBean;
	}

	// TODO Habilitar o log4j

}
