package br.gov.df.emater.aterwebsrv.dao_planejamento;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.orm.jpa.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EntityScan(basePackages = { _PlanejamentoDaoConfig.MODELO })
@EnableJpaRepositories(basePackages = { _PlanejamentoDaoConfig.DAO }, entityManagerFactoryRef = "planejamentoEntityManagerFactory", transactionManagerRef = "planejamentoTransactionManager")
@EnableTransactionManagement
public class _PlanejamentoDaoConfig {

	public static final String DAO = "br.gov.df.emater.aterwebsrv.dao_planejamento";

	public static final String MODELO = "br.gov.df.emater.aterwebsrv.modelo_planejamento";

	public static final String PERSISTENCE_UNIT = "planejamento";

	@Bean(name = "planejamentoDataSource")
	@Qualifier(value = "planejamentoDataSource")
	@ConfigurationProperties(prefix = "planejamento.datasource")
	public DataSource planejamentoDataSource() {
		DataSource result = DataSourceBuilder.create().build();
		return result;
	}

	@Bean(name = "planejamentoEntityManager")
	@Qualifier(value = "planejamentoEntityManager")
	public EntityManager planejamentoEntityManager() {
		EntityManager result = planejamentoEntityManagerFactory().createEntityManager();
		return result;
	}

	@Bean(name = "planejamentoEntityManagerFactory")
	@Qualifier(value = "planejamentoEntityManagerFactory")
	public EntityManagerFactory planejamentoEntityManagerFactory() {
		LocalContainerEntityManagerFactoryBean builder = new LocalContainerEntityManagerFactoryBean();
		builder.setPersistenceUnitName(PERSISTENCE_UNIT);
		builder.setDataSource(planejamentoDataSource());
		builder.setPackagesToScan(MODELO);
		builder.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
		builder.afterPropertiesSet();
		return builder.getObject();
	}

	@Bean(name = "planejamentoTransactionManager")
	@Qualifier(value = "planejamentoTransactionManager")
	@Autowired
	public PlatformTransactionManager planejamentoTransactionManager(@Qualifier("planejamentoEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
		JpaTransactionManager txManager = new JpaTransactionManager();
		txManager.setEntityManagerFactory(entityManagerFactory);
		return txManager;
	}

}