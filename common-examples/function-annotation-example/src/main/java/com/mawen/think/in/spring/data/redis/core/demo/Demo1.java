package com.mawen.think.in.spring.data.redis.core.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.mawen.think.in.spring.data.redis.core.CacheableListFunction;
import com.mawen.think.in.spring.data.redis.core.ListFunction;
import com.mawen.think.in.spring.data.redis.pojo.User;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/3
 */
public class Demo1 {

	public static List<User> USERS = new ArrayList<>();
	public static List<User> USERS_CACHE = new ArrayList<>();

	private static Function<List<Long>, List<User>> dbGetter = Demo1::findInDb;
	private static Function<User, Long> idGetter = User::id;


	static {
		USERS.add(new User(1L, "mawen", 20));
		USERS.add(new User(2L, "lucy", 30));
		USERS.add(new User(3L, "jake", 40));
		USERS.add(new User(4L, "bob", 50));

		USERS_CACHE.add(new User(1L, "mawen", 20));
	}

	public static void main(String[] args) {
		List<Long> ids = List.of(1L, 2L, 3L);
//		fun1(ids);
//		fun2(ids);
//		fun3(ids);
//		fun4(ids);
		System.out.println();
		fun5(ids);
		System.out.println();
		fun6(ids);
	}

	public static void fun1(List<Long> ids) {
		Function<List<Long>, List<User>> userGetter = Demo1::findInDb;

		print(ids, userGetter);
	}

	public static void fun2(List<Long> ids) {
		Function<List<Long>, List<User>> userGetter = Demo1::findInCache;

		print(ids, userGetter);
	}

	public static void fun3(List<Long> ids) {
		Function<List<Long>, List<User>> userGetter = params -> {
			List<User> cacheResult = findInCache(ids);

			if (cacheResult.size() != ids.size()) {
				Set<Long> cacheIds = cacheResult.stream().map(User::id).collect(Collectors.toSet());
				List<Long> nonExistIds = ids.stream().filter(id -> !cacheIds.contains(id)).toList();

				List<User> dbResult = findInDb(nonExistIds);

				List<User> result = new ArrayList<>();
				result.addAll(cacheResult);
				result.addAll(dbResult);
				return result;
			}
			return cacheResult;
		};

		print(ids, userGetter);
	}

	public static void fun4(List<Long> ids) {
		Function<List<Long>, List<User>> cacheGetter = Demo1::findInCache;

		Function<List<Long>, List<User>> composeGetter = params -> {
			List<User> cacheResult = cacheGetter.apply(params);

			if (cacheResult.size() == ids.size()) {
				return cacheResult;
			}

			Set<Long> cacheIds = cacheResult.stream().map(idGetter).collect(Collectors.toSet());
			List<Long> nonExistIds = ids.stream().filter(id -> !cacheIds.contains(id)).toList();

			List<User> dbResult = dbGetter.apply(nonExistIds);

			List<User> result = new ArrayList<>();
			result.addAll(cacheResult);
			result.addAll(dbResult);
			return result;
		};

		print(ids, composeGetter);
	}

	public static void fun5(List<Long> ids) {
		ListFunction<Long, User> cacheGetter = Demo1::findInCache;

		Function<List<Long>, List<User>> wrapperFunction = cacheGetter.compose(dbGetter, idGetter);

		print(ids, wrapperFunction);
	}

	public static void fun6(List<Long> ids) {
		CacheableListFunction<Long, User> cacheGetter = Demo1::findInCache;

		Function<List<Long>, List<User>> wrapperFunction = cacheGetter.compose(dbGetter, idGetter);

		print(ids, wrapperFunction);
	}


	// as cache method
	public static List<User> findInCache(List<Long> ids) {
		System.out.println("Call findInCache with parameter: " + ids + ".");
		return USERS_CACHE.stream()
				.filter(user -> ids.contains(user.id()))
				.toList();
	}

	// as underlying method
	public static List<User> findInDb(List<Long> ids) {
		System.out.println("Call listByIds with parameter: " + ids + ".");
		return USERS.stream()
				.filter(user -> ids.contains(user.id()))
				.toList();
	}

	private static void print(List<Long> ids, Function<List<Long>, List<User>> userGetter) {
		List<User> result = userGetter.apply(ids);
		if (result.size() != ids.size()) {
			System.err.println("ids: " + ids + ", size: " + result.size());
		} else {
			System.out.println(result);
		}
	}
}
