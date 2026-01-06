package com.eipl.amcs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EmcsAppContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmcsAppContext.class);

    private static AnnotationConfigApplicationContext context;

    public static void initializeEmcsAppContext() {
        try {
            context = new AnnotationConfigApplicationContext(ApplicationConfig.class);

            TomcatServletWebServerFactory factory = context.getBean(TomcatServletWebServerFactory.class);
            var customizer = context.getBean(WebServerFactoryCustomizer.class);
            customizer.customize(factory);
            WebServer webServer = factory.getWebServer();
            webServer.start();
        } catch (Exception e) {
            LOGGER.error("EmcsAppContextInitialization Error", e);
        }
    }

    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
