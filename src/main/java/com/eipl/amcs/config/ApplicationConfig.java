package com.eipl.amcs.config;

import com.eipl.amcs.MainApp;
import com.eipl.amcs.network.LoggingRequestInterceptor;
import com.eipl.amcs.network.SystemParamInterceptor;
import com.eipl.amcs.utils.ApiJsonUtil;
import com.eipl.amcs.utils.AppConstant;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.*;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
@Import(value = {HibernateConfig.class})
@ComponentScan(basePackages = {"com.eipl.amcs"})
@PropertySource("classpath:application.properties")
@EnableScheduling
public class ApplicationConfig {

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
