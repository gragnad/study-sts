package com.nalsstudio.config;

import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.hibernate.SpringImplicitNamingStrategy;
import org.springframework.boot.orm.jpa.hibernate.SpringPhysicalNamingStrategy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.io.IOException;
import java.util.*;

@Slf4j
@Configuration
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class})
public class DataConfig {
	
	
		@Bean(name = "masterDataSource")
	    @ConfigurationProperties(prefix="spring.datasource.master")
	    public DataSource masterDataSource() {
	        return DataSourceBuilder.create().type(HikariDataSource.class).build();
	    }

	    @Bean(name = "slaveDataSource")
	    @ConfigurationProperties(prefix="spring.datasource.slave")
	    public DataSource slaveDataSource() {
	        return DataSourceBuilder.create().type(HikariDataSource.class).build();
	    }

	    @Bean(name = "routingDataSource")
	    public DataSource routingDataSource(@Qualifier("masterDataSource")DataSource masterDataSource, @Qualifier("slaveDataSource")DataSource slaveDataSource) {

	        ReplicationRoutingDataSource routingDataSource = new ReplicationRoutingDataSource();
	        Map<Object, Object> dataSourceMap = new HashMap<>();
	        dataSourceMap.put("master", masterDataSource);
	        dataSourceMap.put("slave", slaveDataSource);
	        routingDataSource.setTargetDataSources(dataSourceMap);
	        routingDataSource.setDefaultTargetDataSource(masterDataSource);
	        return routingDataSource;

	    }

	    @Bean(name = "dataSource")
	    public DataSource datasource(@Qualifier("routingDataSource")DataSource routingDataSource) {
	        return new LazyConnectionDataSourceProxy(routingDataSource);
	    }


	    @Primary
	    @Bean(name = "entityManagerFactory")
	    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
	        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
	        em.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
	        em.setDataSource(dataSource);
	        String[] domainlist = new String[]{"com.smsb.baedal.market.domain", "com.smsb.baedal.cmessage.domain"};
	        em.setPackagesToScan(domainlist);


	        Map<String, Object> properties = new HashMap<>();
	        properties.put("hibernate.physical_naming_strategy",
	                SpringPhysicalNamingStrategy.class.getName());
	        properties.put("hibernate.implicit_naming_strategy",
	                SpringImplicitNamingStrategy.class.getName());
	        //properties.put("hibernate.hbm2ddl.auto", env.getProperty("spring.jpa.hibernate.ddl-auto"));
	        em.setJpaPropertyMap(properties);
	        return em;
	    }

	    @Primary
	    @Bean
	    public PlatformTransactionManager transactionManager(
	            EntityManagerFactory entityManagerFactory) {
	        JpaTransactionManager transactionManager = new JpaTransactionManager();
	        transactionManager.setEntityManagerFactory(entityManagerFactory);

	        return transactionManager;
	    } 
	
}
