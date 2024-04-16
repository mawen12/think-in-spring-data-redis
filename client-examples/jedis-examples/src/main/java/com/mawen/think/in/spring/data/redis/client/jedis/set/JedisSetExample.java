package com.mawen.think.in.spring.data.redis.client.jedis.set;

import java.util.List;
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
			jedis.flushDB();

			sadd(jedis);
			sscan(jedis);
			scard(jedis);

			sdiff(jedis);
			sdiffstore(jedis);
			sinter(jedis);
			sintercard(jedis);
			sinterstore(jedis);

			sismember(jedis);
			smembers(jedis);
			smismembers(jedis);

			smove(jedis);
			spop(jedis);
			srandmember(jedis);
			sunion(jedis);
			srem(jedis);

			jedis.flushDB();
		}


		pool.close();
	}

	static void sadd(Jedis jedis) {
		System.out.println("=====================sadd=====================");
		long ret = jedis.sadd("abc", "a", "b", "c");
		System.out.println(ret);
	}

	static void sscan(Jedis jedis) {
		System.out.println("=====================sscan=====================");
		ScanResult<String> result = jedis.sscan("abc", "0");
		System.out.println(String.join(",", result.getResult()).equals("a,b,c"));
	}

	static void scard(Jedis jedis) {
		System.out.println("=====================scard=====================");
		long count = jedis.scard("abc");
		System.out.println(count == 3);
	}

	static void sdiff(Jedis jedis) {
		System.out.println("=====================sdiff=====================");
		jedis.sadd("bcd", "b", "c", "d");
		Set<String> diff = jedis.sdiff("abc", "bcd");
		System.out.println(diff);

		Set<String> sdiff = jedis.sdiff("bcd", "abc");
		System.out.println(sdiff);
	}

	static void sdiffstore(Jedis jedis) {
		System.out.println("=====================sdiffstore=====================");
		long ret = jedis.sdiffstore("cde", "abc", "bcd");
		System.out.println(ret);
		System.out.println(String.join(",", jedis.sscan("cde", "0").getResult()));
	}

	static void sinter(Jedis jedis) {
		System.out.println("=====================sinter=====================");
		Set<String> ret = jedis.sinter("abc", "bcd", "cde");
		System.out.println(String.join(",", ret).equals("c"));
	}

	static void sintercard(Jedis jedis) {
		System.out.println("=====================sintercard=====================");
		long count = jedis.sintercard("abc", "bcd", "cde");
		System.out.println(count == 1);
	}

	static void sinterstore(Jedis jedis) {
		System.out.println("=====================sinterstore=====================");
		long ret = jedis.sinterstore("z", "abc", "bcd", "cde");
		System.out.println(ret);

		System.out.println(jedis.smembers("z"));
	}

	static void sismember(Jedis jedis) {
		System.out.println("=====================sismember=====================");
		boolean ret = jedis.sismember("abc", "a");
		System.out.println(ret);
	}

	static void smembers(Jedis jedis) {
		System.out.println("=====================smembers=====================");
		Set<String> ret = jedis.smembers("abc");
		System.out.println(ret);
	}

	static void smismembers(Jedis jedis) {
		System.out.println("=====================smismembers=====================");
		List<Boolean> ret = jedis.smismember("abc", "a", "d");
		System.out.println(ret);
	}

	static void smove(Jedis jedis) {
		System.out.println("=====================smove=====================");
		long smove = jedis.smove("abc", "y", "a");
		System.out.println(jedis.smembers("y"));
	}

	static void spop(Jedis jedis) {
		System.out.println("=====================spop=====================");
		String abc = jedis.spop("abc");
		System.out.println(abc);

		long count = jedis.scard("abc");
		System.out.println(count);
	}

	static void srandmember(Jedis jedis) {
		System.out.println("=====================srandmember=====================");
		String abc = jedis.srandmember("abc");
		System.out.println(abc);
	}

	static void sunion(Jedis jedis) {
		System.out.println("=====================sunion=====================");
		Set<String> ret = jedis.sunion("abc", "bcd", "cde");
		System.out.println(ret);
		System.out.println(String.join(",", ret).equals("a,b,c,d,e"));
	}

	static void srem(Jedis jedis) {
		System.out.println("=====================srem=====================");
		System.out.println(jedis.smembers("abc"));

		long srem = jedis.srem("abc", "a");
		System.out.println(srem);

		System.out.println(jedis.smembers("abc"));
	}


}
