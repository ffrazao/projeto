package br.gov.df.emater.aterwebsrv.dao;

import javax.persistence.EntityManagerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "br.gov.df.emater.aterwebsrv.dao")
@EntityScan(basePackages = "br.gov.df.emater.aterwebsrv.modelo")
@EnableTransactionManagement
public class _DaoConfig {

	@Autowired
	private EntityManagerFactory entityManagerFactory;

	// TODO descomentar esta linha para o ambiente de produção
	// @Bean(name = "dataSource")
	// public DataSource dataSource() {
	// JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	// dsLookup.setResourceRef(true);
	// DataSource dataSource = dsLookup.getDataSource("jdbc/ematerDS");
	// return dataSource;
	// }

	public _DaoConfig() {
		System.out.println("novo _DaoConfig");
	}

	public EntityManagerFactory getEntityManagerFactory() {
		return entityManagerFactory;
	}

	@Bean
	public PlatformTransactionManager transactionManager() {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(this.entityManagerFactory);
		return txManager;
	}

}