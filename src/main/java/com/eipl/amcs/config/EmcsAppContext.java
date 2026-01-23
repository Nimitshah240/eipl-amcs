package com.eipl.amcs.config;

import com.eipl.amcs.exception.AuthenticationFailException;
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
        } catch (Exception e) {
            Throwable cause = e;
            while (cause != null) {
                if (cause instanceof AuthenticationFailException) {
                    throw new AuthenticationFailException(HibernateConfig.class, "Failed to connect Database");
                }
                cause = cause.getCause();
            }
            LOGGER.error("EmcsAppContextInitialization Error", e);
        }
    }

    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
