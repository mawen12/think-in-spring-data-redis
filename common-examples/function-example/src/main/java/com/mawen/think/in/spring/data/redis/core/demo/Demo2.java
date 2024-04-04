package com.mawen.think.in.spring.data.redis.core.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import com.mawen.think.in.spring.data.redis.core.ListFunction;
import com.mawen.think.in.spring.data.redis.pojo.User;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/4
 */
public class Demo2 {

	public static List<User> DB_USER = new ArrayList<>();

	static {
		DB_USER.add(new User(1L, "mawen", 20));
		DB_USER.add(new User(2L, "lucy", 30));
		DB_USER.add(new User(3L, "jake", 40));
		DB_USER.add(new User(4L, "bob", 50));
	}

	public static void main(String[] args) {
		List<Long> ids = List.of(1L, 2L, 3L);

		fun1(ids);
	}

	public static void fun1(List<Long> ids) {
		Function<List<Long>, List<User>> sourceGetter = Demo2::findInDb;

		Function<List<Long>, List<User>> wrapperGetter = ListFunction.ofMap(sourceGetter, User::id);

		print(ids, wrapperGetter);
		System.out.println();
		print(ids, wrapperGetter);
		System.out.println();
		print(ids, wrapperGetter);
	}

	public static List<User> findInDb(List<Long> ids) {
		System.out.println("Call findInDb with parameter: " + ids + ".");
		return DB_USER.stream()
				.filter(user -> ids.contains(user.id()))
				.toList();
	}

	private static void print(List<Long> ids, Function<List<Long>, List<User>> userGetter) {
		List<User> result = userGetter.apply(ids);
		if (result.size() != ids.size()) {
			System.err.println("ids: " + ids + ", size: " + result.size());
		}
		else {
			System.out.println(result);
		}
	}
}
