package com.sfood.eai.external.aop.annotation;

import com.sfood.eai.external.type.InterfaceType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface HttpLog {
    InterfaceType value() default InterfaceType.NONE;
}
