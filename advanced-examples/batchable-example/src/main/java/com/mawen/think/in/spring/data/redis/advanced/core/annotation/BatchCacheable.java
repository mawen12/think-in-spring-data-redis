package com.mawen.think.in.spring.data.redis.advanced.core.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

import com.mawen.think.in.spring.data.redis.advanced.core.interceptor.BatchCacheableAspect;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 * @see BatchCacheableAspect
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BatchCacheable {

	String key() default "";

	int argIndex() default 0;

	long timeout() default 0;

	TimeUnit unit() default TimeUnit.SECONDS;

	Class<?> type();
}
