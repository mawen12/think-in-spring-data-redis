package com.mawen.think.in.spring.data.redis.advanced.core.domain;

import java.lang.reflect.Method;
import java.util.List;

import com.mawen.think.in.spring.data.redis.advanced.core.annotation.BatchCacheable;
import com.mawen.think.in.spring.data.redis.advanced.core.parser.IBaseKeyParser;
import com.mawen.think.in.spring.data.redis.advanced.pojo.IBase;
import com.mawen.think.in.spring.data.redis.advanced.pojo.User;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import org.springframework.data.domain.Example;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test for {@link BatchCacheableMethodInfo}
 */
class BatchCacheableMethodInfoUnitTest {

	@Test
	@Order(1)
	void shouldParseCorrectly() throws Throwable {

		// given
		Method listByIds = ExampleMethod.class.getMethod("listByIds", List.class);

		// when
		BatchCacheableMethodInfo<Object> methodInfo = new BatchCacheableMethodInfo<>(listByIds);

		// then
		// ========================= Param =============================
		BatchCacheableParamInfo paramInfo = methodInfo.getParamInfo();
		assertThat(paramInfo.isValid()).isTrue();
		assertThat(paramInfo.getParamIndex()).isEqualTo(0);
		assertThat(paramInfo.getParamType()).isEqualTo(List.class);
		assertThat(paramInfo.getRawParamType()).isEqualTo(Long.class);

		// ========================= Return =============================
		BatchCacheableReturnInfo<Object> returnInfo = methodInfo.getReturnInfo();
		assertThat(returnInfo.isValid()).isTrue();
		assertThat(returnInfo.getReturnType()).isEqualTo(List.class);
		assertThat(returnInfo.getRawReturnType()).isEqualTo(User.class);
		assertThat(returnInfo.getKeyParser()).isNotNull();
		assertThat(returnInfo.getKeyParser()).isEqualTo(IBaseKeyParser.INSTANCE);

		// ========================= Method =============================
		assertThat(methodInfo.isValid()).isTrue();
		assertThat(methodInfo.getAnnotationKey()).isEqualTo("abc");
		assertThat(methodInfo.getExpiration()).isNull();

		// ========================= Annotation =========================
		BatchCacheable annotation = methodInfo.getBatchCacheable();
		assertThat(annotation.key()).isEqualTo("abc");
		assertThat(annotation.argIndex()).isEqualTo(0);
		assertThat(annotation.timeout()).isEqualTo(-1);
		assertThat(annotation.type()).isEqualTo(IBase.class);
	}

	@Test
	@Order(2)
	void shouldParseFailedWhenReturnIsInvalid() throws NoSuchMethodException {

		// given
		Method listByIdsWithVoid = ExampleMethod.class.getMethod("listByIdsWithVoid", List.class);

		// when
		BatchCacheableMethodInfo<Object> methodInfo = new BatchCacheableMethodInfo<>(listByIdsWithVoid);

		// then
		// ========================= Param =============================
		BatchCacheableParamInfo paramInfo = methodInfo.getParamInfo();
		assertThat(paramInfo.isValid()).isTrue();
		assertThat(paramInfo.getParamIndex()).isEqualTo(0);
		assertThat(paramInfo.getParamType()).isEqualTo(List.class);
		assertThat(paramInfo.getRawParamType()).isEqualTo(Long.class);

		// ========================= Return =============================
		BatchCacheableReturnInfo<Object> returnInfo = methodInfo.getReturnInfo();
		assertThat(returnInfo.isValid()).isFalse();
		assertThat(returnInfo.getReturnType()).isEqualTo(void.class);
		assertThat(returnInfo.getRawReturnType()).isNull();
		assertThat(returnInfo.getKeyParser()).isNull();

		// ========================= Method =============================
		assertThat(methodInfo.isValid()).isFalse();
		assertThat(methodInfo.getAnnotationKey()).isEqualTo("abc");
		assertThat(methodInfo.getExpiration()).isNull();

		// ========================= Annotation =========================
		BatchCacheable annotation = methodInfo.getBatchCacheable();
		assertThat(annotation.key()).isEqualTo("abc");
		assertThat(annotation.argIndex()).isEqualTo(0);
		assertThat(annotation.timeout()).isEqualTo(-1);
		assertThat(annotation.type()).isEqualTo(IBase.class);
	}

	@Test
	@Order(3)
	void shouldParseFailedWhenParamIsInvalid() throws NoSuchMethodException {

		// given
		Method listByIdsWithNullParam = ExampleMethod.class.getMethod("listByIdsWithNullParam");

		// when
		BatchCacheableMethodInfo<Object> methodInfo = new BatchCacheableMethodInfo<>(listByIdsWithNullParam);

		// then
		// ========================= Param =============================
		BatchCacheableParamInfo paramInfo = methodInfo.getParamInfo();
		assertThat(paramInfo.isValid()).isFalse();
		assertThat(paramInfo.getParamIndex()).isEqualTo(-1);
		assertThat(paramInfo.getParamType()).isNull();
		assertThat(paramInfo.getRawParamType()).isNull();

		// ========================= Return =============================
		BatchCacheableReturnInfo<Object> returnInfo = methodInfo.getReturnInfo();
		assertThat(returnInfo.isValid()).isTrue();
		assertThat(returnInfo.getReturnType()).isEqualTo(List.class);
		assertThat(returnInfo.getRawReturnType()).isEqualTo(User.class);
		assertThat(returnInfo.getKeyParser()).isNotNull();
		assertThat(returnInfo.getKeyParser()).isEqualTo(IBaseKeyParser.INSTANCE);

		// ========================= Method =============================
		assertThat(methodInfo.isValid()).isFalse();
		assertThat(methodInfo.getAnnotationKey()).isEqualTo("abc");
		assertThat(methodInfo.getExpiration()).isNull();

		// ========================= Annotation =========================
		BatchCacheable annotation = methodInfo.getBatchCacheable();
		assertThat(annotation.key()).isEqualTo("abc");
		assertThat(annotation.argIndex()).isEqualTo(0);
		assertThat(annotation.timeout()).isEqualTo(-1);
		assertThat(annotation.type()).isEqualTo(IBase.class);
	}

	@Test
	@Order(4)
	void shouldParseFailedWhenParamAndReturnIsInvalid() throws NoSuchMethodException {

		// given
		Method listByIdsWithVoidAndNullParam = ExampleMethod.class.getMethod("listByIdsWithVoidAndNullParam");

		// when
		BatchCacheableMethodInfo<Object> methodInfo = new BatchCacheableMethodInfo<>(listByIdsWithVoidAndNullParam);

		// then
		// ========================= Param =============================
		BatchCacheableParamInfo paramInfo = methodInfo.getParamInfo();
		assertThat(paramInfo.isValid()).isFalse();
		assertThat(paramInfo.getParamIndex()).isEqualTo(-1);
		assertThat(paramInfo.getParamType()).isNull();
		assertThat(paramInfo.getRawParamType()).isNull();

		// ========================= Return =============================
		BatchCacheableReturnInfo<Object> returnInfo = methodInfo.getReturnInfo();
		assertThat(returnInfo.isValid()).isFalse();
		assertThat(returnInfo.getReturnType()).isEqualTo(void.class);
		assertThat(returnInfo.getRawReturnType()).isNull();
		assertThat(returnInfo.getKeyParser()).isNull();

		// ========================= Method =============================
		assertThat(methodInfo.isValid()).isFalse();
		assertThat(methodInfo.getAnnotationKey()).isEqualTo("abc");
		assertThat(methodInfo.getExpiration()).isNull();

		// ========================= Annotation =========================
		BatchCacheable annotation = methodInfo.getBatchCacheable();
		assertThat(annotation.key()).isEqualTo("abc");
		assertThat(annotation.argIndex()).isEqualTo(0);
		assertThat(annotation.timeout()).isEqualTo(-1);
		assertThat(annotation.type()).isEqualTo(IBase.class);
	}


	interface ExampleMethod {

		@BatchCacheable(key = "abc", argIndex = 0, timeout = -1, type = IBase.class)
		List<User> listByIds(List<Long> ids);

		@BatchCacheable(key = "abc", argIndex = 0, timeout = -1, type = IBase.class)
		void listByIdsWithVoid(List<Long> ids);

		@BatchCacheable(key = "abc", argIndex = 0, timeout = -1, type = IBase.class)
		List<User> listByIdsWithNullParam();

		@BatchCacheable(key = "abc", argIndex = 0, timeout = -1, type = IBase.class)
		void listByIdsWithVoidAndNullParam();
	}

}