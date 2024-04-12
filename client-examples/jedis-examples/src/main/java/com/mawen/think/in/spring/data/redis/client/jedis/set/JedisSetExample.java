package com.mawen.think.in.spring.data.redis.client.jedis.set;

import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.ScanResult;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/12
 */
public class JedisSetExample {

	public static void main(String[] args) {
		JedisPool pool = new JedisPool();

		try (Jedis jedis = pool.getResource()) {

		}

		pool.close();
	}

	static void sadd(Jedis jedis) {
		long ret = jedis.sadd("abc", "a", "b", "c");
		System.out.println(ret);
	}

	static void sscan(Jedis jedis) {
		ScanResult<String> result = jedis.sscan("abc", "0");
		System.out.println(String.join(",", result.getResult()).equals("a,b,c"));
	}

	static void scard(Jedis jedis) {
		long count = jedis.scard("abc");
		System.out.println(count == 3);
	}

	static void sdiff(Jedis jedis) {
		jedis.sadd("bcd", "b", "c", "d");
		Set<String> diff = jedis.sdiff("abc", "a", "b", "c");
		System.out.println(diff);
	}

	static void sdiffstore(Jedis jedis) {
		long ret = jedis.sdiffstore("cde", "abc", "bcd");
		System.out.println(ret);
	}

	static void sinter(Jedis jedis) {
		Set<String> ret = jedis.sinter("abc", "bcd", "cde");
		System.out.println(String.join(",", ret).equals("c"));
	}

	static void sunion(Jedis jedis) {
		Set<String> ret = jedis.sunion("abc", "bcd", "cde");
		System.out.println(String.join(",", ret).equals("a,b,c,d,e"));
	}


}
