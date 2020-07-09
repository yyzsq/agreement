
package com.gemframework.model.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;

//声明了一个名为ValidToken的注解体
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ METHOD, FIELD, CONSTRUCTOR, PARAMETER })
public @interface ValidToken {

    boolean isValid() default true;
}