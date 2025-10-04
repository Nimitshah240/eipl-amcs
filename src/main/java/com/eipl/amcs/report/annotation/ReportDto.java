package com.eipl.amcs.report.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

@Documented
@Component
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ReportDto {

}
