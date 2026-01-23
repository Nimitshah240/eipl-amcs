package com.eipl.amcs.config;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.LoggerContext;
import com.eipl.amcs.exception.AuthenticationFailException;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

import static com.eipl.amcs.base.controller.ActivationController.recoverFromPreferences;
import static com.eipl.amcs.utils.AppConstant.*;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(
        repositoryBaseClass = com.eipl.amcs.base.repository.BaseRepositoryImpl.class
)
@Slf4j
public class HibernateConfig {

    @Bean
    public DataSource dataSource() {
        try {
            recoverFromPreferences();
            log.info(DB_LOC);
            if (DB_LOC == null) {
                log.error("----------- NO DATABASE FOUND -------------");
                return null;
            }

            HikariDataSource dataSource = new HikariDataSource();
            //        dataSource.setJdbcUrl("jdbc:mysql://" + DB_LOC +":3366/eipl_amcs_db?characterEncoding=UTF-8&useSSL=false");
            dataSource.setJdbcUrl("jdbc:mysql://" + DB_LOC + ":3366/" + EIPL_DB_NAME + "?allowPublicKeyRetrieval=true&characterEncoding=UTF-8&useSSL=false");
            dataSource.setUsername("root");
            dataSource.setPassword(EIPL_DB_PASS);
            dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
            dataSource.setMaximumPoolSize(5);
            dataSource.getConnection().close();
            return dataSource;
        } catch (Exception e) {
            log.error(e.getMessage());
            throw new AuthenticationFailException(HibernateConfig.class, "Failed to connect Database");
        }
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        Properties properties = new Properties();
        properties.put("hibernate.hbm2ddl.auto", "none");
        properties.put("hibernate.show_sql", "false");
        properties.setProperty(
                "hibernate.physical_naming_strategy",
                "org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy"
        );

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setDataSource(dataSource());
        entityManager.setJpaVendorAdapter(new HibernateJpaVendorAdapter());
        entityManager.setPackagesToScan("com.eipl.amcs");
        entityManager.setJpaProperties(properties);
        return entityManager;
    }

    @Bean(name = "transactionManager")
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }

    @PostConstruct
    public void configureJasperReportsLogging() {

        LoggerContext context = (LoggerContext) LoggerFactory.getILoggerFactory();
        ch.qos.logback.classic.Logger jdbcQueryLogger =
                context.getLogger("net.sf.jasperreports.engine.query.JRJdbcQueryExecuter");

        if (jdbcQueryLogger != null) {
            jdbcQueryLogger.setLevel(Level.DEBUG);
            System.out.println("--- Programmatically set logging level for JRJdbcQueryExecuter to DEBUG ---");
        }
    }
}
