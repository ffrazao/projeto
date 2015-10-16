package br.gov.df.emater.aterwebsrv.rest;

import org.apache.catalina.Context;
import org.apache.catalina.connector.Connector;
import org.apache.tomcat.util.descriptor.web.SecurityCollection;
import org.apache.tomcat.util.descriptor.web.SecurityConstraint;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.FilterRegistrationBean;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
@EnableWebMvc
@ComponentScan(basePackages = { "br.gov.df.emater.aterwebsrv" })
public class _RestConfig extends WebMvcAutoConfiguration/*DelegatingWebMvcConfiguration*/ {

//	@Override
//	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
//		configurer.enable();
//	}
	
	// inicio redirecionamento http para https
	@Bean
	public EmbeddedServletContainerFactory servletContainer() {
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
		return tomcat;
	}

	private Connector initiateHttpConnector() {
		Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
		connector.setScheme("http");
		connector.setPort(8080);
		connector.setSecure(false);
		connector.setRedirectPort(8443);

		return connector;
	}
	// fim redirecionamento http para https

	// @Bean
	// public ContentNegotiatingViewResolver contentViewResolver() throws
	// Exception {
	// ContentNegotiationManagerFactoryBean contentNegotiationManager = new
	// ContentNegotiationManagerFactoryBean();
	// contentNegotiationManager.addMediaType("json",
	// MediaType.APPLICATION_JSON);
	//
	// InternalResourceViewResolver viewResolver = new
	// InternalResourceViewResolver();
	// viewResolver.setPrefix("/WEB-INF/jsp/");
	// viewResolver.setSuffix(".jsp");
	//
	// MappingJackson2JsonView defaultView = new MappingJackson2JsonView();
	// defaultView.setExtractValueFromSingleKeyModel(true);
	//
	// ContentNegotiatingViewResolver contentViewResolver = new
	// ContentNegotiatingViewResolver();
	// contentViewResolver.setContentNegotiationManager(contentNegotiationManager.getObject());
	// contentViewResolver.setViewResolvers(Arrays.<ViewResolver>
	// asList(viewResolver));
	// contentViewResolver.setDefaultViews(Arrays.<View> asList(defaultView));
	// return contentViewResolver;
	// }
	
	@Bean
	@Primary
	public HttpMessageConverter<Object> mappingJackson2HttpMessageConverter() {
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
		converter.setObjectMapper(mapper);
		return converter;
	}

	// @Override
	// public void configureMessageConverters(List<HttpMessageConverter<?>>
	// converters) {
	// Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
	// builder.serializationInclusion(Include.NON_NULL);
	// builder.serializationInclusion(Include.NON_EMPTY);
	//
	// builder.failOnEmptyBeans(false);
	// builder.failOnUnknownProperties(false);
	//
	// builder.indentOutput(true).dateFormat(new
	// java.text.SimpleDateFormat("dd/MM/yyyy HH:mm:ss"));
	// converters.add(new MappingJackson2HttpMessageConverter(builder.build()));
	// }

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
