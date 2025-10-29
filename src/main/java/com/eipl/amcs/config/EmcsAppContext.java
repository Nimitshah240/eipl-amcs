package com.eipl.amcs.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class EmcsAppContext {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmcsAppContext.class);

    private static AnnotationConfigApplicationContext context;

    public static void initializeEmcsAppContext() {
        try {
            System.out.println("---------------------------- Nimit : Starting ---------------------------------------------");
            context = new AnnotationConfigApplicationContext(ApplicationConfig.class);
            System.out.println("---------------------------- Nimit : Completed ---------------------------------------------");

            LOGGER.info("EmcsAppContextInitialization Success");
        } catch (Exception e) {
            System.out.println("---------------------------- Nimit : Error ---------------------------------------------");
            LOGGER.error("EmcsAppContextInitialization Error", e);
            System.out.println("---------------------------- Nimit : Error ---------------------------------------------");
        }
    }

    public static AnnotationConfigApplicationContext getContext() {
        return context;
    }
}
