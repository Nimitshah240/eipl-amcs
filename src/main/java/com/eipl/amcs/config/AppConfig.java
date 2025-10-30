package com.eipl.amcs.config;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.network.LoggingRequestInterceptor;
import com.eipl.amcs.network.SystemParamInterceptor;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

@Configuration
@ComponentScan(basePackages = {"com.eipl.amcs"})
@EnableJpaRepositories(
        basePackages = "com.eipl.amcs",
        repositoryBaseClass = com.eipl.amcs.base.repository.BaseRepositoryImpl.class
)
@PropertySource("classpath:application.properties")

public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        if ("1".equalsIgnoreCase(MainApp.getProperty(AppConstant.Props.APP_REQUEST_DEBUG, "0"))) {
            RestTemplate restTemplate = new RestTemplate(new BufferingClientHttpRequestFactory(new SimpleClientHttpRequestFactory()));
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(new SystemParamInterceptor());
            interceptors.add(new LoggingRequestInterceptor());
            restTemplate.setInterceptors(interceptors);
            return restTemplate;
        } else {
            RestTemplate restTemplate = new RestTemplate();
            List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
            interceptors.add(new SystemParamInterceptor());
            restTemplate.setInterceptors(interceptors);
            return restTemplate;
        }
    }

    @Bean
    public ApiJsonUtil apiJsonUtil() {
        return new ApiJsonUtil();
    }
}
