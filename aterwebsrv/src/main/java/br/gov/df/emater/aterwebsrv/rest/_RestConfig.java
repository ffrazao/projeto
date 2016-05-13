package br.gov.df.emater.aterwebsrv.rest;

import java.util.List;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module.Feature;

@Configuration
@EnableWebMvc // estava dando um erro e comentei esta linha. Parou de fazer upload de arquivos descomentei novamente
@ComponentScan(basePackages = { "br.gov.df.emater.aterwebsrv" })
public class _RestConfig extends WebMvcConfigurerAdapter {

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {

		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
		builder.serializationInclusion(Include.NON_NULL);
		builder.serializationInclusion(Include.NON_EMPTY);
		builder.failOnEmptyBeans(false);
		builder.failOnUnknownProperties(false);
		builder.indentOutput(true).dateFormat(new java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
		MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(builder.build());
		ObjectMapper mapper = new ObjectMapper();
		mapper.setSerializationInclusion(Include.NON_NULL);
		mapper.setSerializationInclusion(Include.NON_EMPTY);
		mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		Hibernate4Module hibernate4Module = new Hibernate4Module();
		hibernate4Module.configure(Feature.USE_TRANSIENT_ANNOTATION, false);
		mapper.registerModule(hibernate4Module);
		converter.setObjectMapper(mapper);

		converters.add(converter);

		super.configureMessageConverters(converters);
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

	private Connector initiateHttpConnector() {
		// inicio redirecionamento http para https
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setSecure(false);
		connector.setRedirectPort(8443);
		// fim redirecionamento http para https

		return connector;
	}

	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
		// inicio redirecionamento http para https
		TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory() {
			@Override
			protected void postProcessContext(Context context) {
				SecurityConstraint securityConstraint = new SecurityConstraint();
				securityConstraint.setUserConstraint("CONFIDENTIAL");
				SecurityCollection collection = new SecurityCollection();
				collection.addPattern("/*");
				securityConstraint.addCollection(collection);
				context.addConstraint(securityConstraint);
			}
		};

		tomcat.addAdditionalTomcatConnectors(initiateHttpConnector());

		// fim redirecionamento http para https
		return tomcat;
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver setupCommonsMultipartResolver() {
		// Manipulação de Arquivos
		CommonsMultipartResolver result = new CommonsMultipartResolver();
		return result;
	}

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources/");
	}

}