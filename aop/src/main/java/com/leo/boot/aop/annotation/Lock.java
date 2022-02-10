package com.leo.boot.aop.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

	String id() default ""; // use SpEL expression.

	String key() default ""; // default is method name.

	long timeout() default 3000L;

	int retryTimes() default 0;

	boolean interrupt() default true;
}
