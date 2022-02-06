package com.leo.boot.aop.annotation;

import com.leo.boot.aop.lock.RedisLock;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lock {

	String id() default ""; // use SpEL expression

	String key() default RedisLock.COMMON_LOCK_KEY;

	long timeout() default RedisLock.COMMON_TIME_OUT;

	int retryTimes() default 0;

	boolean interrupt() default true;
}
