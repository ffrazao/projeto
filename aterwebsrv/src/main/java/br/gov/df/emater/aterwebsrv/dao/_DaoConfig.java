package br.gov.df.emater.aterwebsrv.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaSessionFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "br.gov.df.emater.aterwebsrv.dao")
// @EntityScan(basePackages = "br.gov.df.emater.aterwebsrv.modelo")
@EnableTransactionManagement
public class _DaoConfig {

	// TODO descomentar esta linha para o ambiente de produção
	// @Bean(name = "dataSource")
	// public DataSource dataSource() {
	// JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	// dsLookup.setResourceRef(true);
	// DataSource dataSource = dsLookup.getDataSource("jdbc/ematerDS");
	// return dataSource;
	// }

	@Autowired
	private EntityManagerFactoryBuilder builder;

	@Bean
	@Qualifier(value = "dataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource dataSource() {
		DataSource result = DataSourceBuilder.create().build();
		return result;
	}

	@Bean
	@Qualifier(value = "dataSourcePlanejamento")
	@ConfigurationProperties(prefix = "planejamento.datasource")
	public DataSource dataSourcePlanejamento() {
		DataSource result = DataSourceBuilder.create().build();
		return result;
	}

	@Bean
	@Qualifier(value = "entityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
		LocalContainerEntityManagerFactoryBean result = builder.dataSource(dataSource()).packages("br.gov.df.emater.aterwebsrv.modelo").persistenceUnit("default").build();
		return result;
	}

	@Bean
	@Qualifier(value = "entityManager")
	@Primary
	public EntityManager entityManager() {
		EntityManager result = entityManagerFactory().getNativeEntityManagerFactory().createEntityManager();
		return result;
	}

	@Bean
	@Qualifier(value = "sessionFactory")
	@Primary
	public HibernateJpaSessionFactoryBean sessionFactory() {
		HibernateJpaSessionFactoryBean result = new HibernateJpaSessionFactoryBean();
		return result;
	}

	@Bean
	@Qualifier(value = "transactionManager")
	@Primary
	@Autowired
	public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

}