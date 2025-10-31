package com.eipl.amcs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EmcsAppContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmcsAppContext.class);

    private static AnnotationConfigApplicationContext context;

    public static void initializeEmcsAppContext() {
        try {
            context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
        } catch (Exception e) {
            LOGGER.error("EmcsAppContextInitialization Error", e);
        }
    }

    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
