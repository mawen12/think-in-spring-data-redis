package com.mawen.think.in.spring.data.redis.advanced.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/17
 * @see com.mawen.think.in.spring.data.redis.advanced.interceptor.BatchCacheableAspect
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface BatchCacheable {

	String key() default "";

	int argIndex() default 0;

	Class<?> type();
}
