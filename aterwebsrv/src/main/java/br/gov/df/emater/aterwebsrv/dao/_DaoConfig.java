package br.gov.df.emater.aterwebsrv.dao;

import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(basePackages = "br.gov.df.emater.aterwebsrv.dao")
@EntityScan(basePackages = "br.gov.df.emater.aterwebsrv.modelo")
@EnableTransactionManagement
public class _DaoConfig {
	
	public _DaoConfig() {
		System.out.println("novo _DaoConfig");
	}

	// @Bean(name = "dataSource")
	// public DataSource dataSource() {
	// JndiDataSourceLookup dsLookup = new JndiDataSourceLookup();
	// dsLookup.setResourceRef(true);
	// DataSource dataSource = dsLookup.getDataSource("jdbc/ematerDS");
	// return dataSource;
	// }

	// @Bean
	// public EntityManagerFactory entityManagerFactory() {
	//
	// HibernateJpaVendorAdapter vendorAdapter = new
	// HibernateJpaVendorAdapter();
	// vendorAdapter.setGenerateDdl(false);
	//
	// LocalContainerEntityManagerFactoryBean factory = new
	// LocalContainerEntityManagerFactoryBean();
	// factory.setJpaVendorAdapter(vendorAdapter);
	// factory.setPackagesToScan("br.gov.df.emater.aterwebsrv.modelo");
	// factory.setDataSource(dataSource());
	// factory.afterPropertiesSet();
	//
	// return factory.getObject();
	// }

	// @Bean
	// public PlatformTransactionManager transactionManager() {
	//
	// JpaTransactionManager txManager = new JpaTransactionManager();
	// txManager.setEntityManagerFactory(entityManagerFactory());
	// return txManager;
	// }

}
