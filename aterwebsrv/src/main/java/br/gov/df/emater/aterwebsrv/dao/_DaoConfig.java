package br.gov.df.emater.aterwebsrv.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.orm.jpa.EntityScan;
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
@EntityScan(basePackages = { _DaoConfig.MODELO })
@EnableJpaRepositories(basePackages = { _DaoConfig.DAO }, entityManagerFactoryRef = "entityManagerFactory", transactionManagerRef = "transactionManager")
@EnableTransactionManagement
public class _DaoConfig {

	public static final String DAO = "br.gov.df.emater.aterwebsrv.dao";

	public static final String MODELO = "br.gov.df.emater.aterwebsrv.modelo";

	public static final String PERSISTENCE_UNIT = "default";

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

	@Bean(name = "dataSource")
	@Primary
	@ConfigurationProperties(prefix = "spring.datasource")
	public DataSource defaultDataSource() {
		DataSource result = DataSourceBuilder.create().build();
		return result;
	}

	@Bean(name = "entityManager")
	@Qualifier(value = "entityManager")
	@Primary
	public EntityManager defaultEntityManager() {
		EntityManager result = defaultEntityManagerFactory().getNativeEntityManagerFactory().createEntityManager();
		return result;
	}

	@Bean(name = "entityManagerFactory")
	@Qualifier(value = "entityManagerFactory")
	@Primary
	public LocalContainerEntityManagerFactoryBean defaultEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean result = builder.dataSource(defaultDataSource()).packages(MODELO).persistenceUnit(PERSISTENCE_UNIT).build();
		return result;
	}

	@Bean(name = "transactionManager")
	@Qualifier(value = "transactionManager")
	@Primary
	@Autowired
	public PlatformTransactionManager defaultTransactionManager(EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

	@Bean(name = "sessionFactory")
	@Qualifier(value = "sessionFactory")
	@Primary
	public HibernateJpaSessionFactoryBean sessionFactory() {
		HibernateJpaSessionFactoryBean result = new HibernateJpaSessionFactoryBean();
		return result;
	}

}