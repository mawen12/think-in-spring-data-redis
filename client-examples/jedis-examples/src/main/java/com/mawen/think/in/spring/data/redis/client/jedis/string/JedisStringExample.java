package com.mawen.think.in.spring.data.redis.client.jedis.string;

import java.util.List;
import java.util.Objects;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.params.GetExParams;
import redis.clients.jedis.params.LCSParams;
import redis.clients.jedis.resps.LCSMatchResult;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/12
 */
public class JedisStringExample {

	public static void main(String[] args) {

		JedisPool pool = new JedisPool();
		
		try (Jedis jedis = pool.getResource()) {
			jedis.flushDB();

			set(jedis);
			setex(jedis);
			setrange(jedis);
			get(jedis);
			setByte(jedis);
			getByte(jedis);
			getDel(jedis);
			getEx(jedis);
			getRange(jedis);

			jedis.flushDB();

			mset(jedis);
			mget(jedis);
			msetnx(jedis);

			jedis.flushDB();

			incrOne(jedis);
			incrOneByte(jedis);
			incrByN(jedis);
			incrByNByte(jedis);
			incrByFloat(jedis);

			jedis.flushDB();

			decrOne(jedis);
			decrOneByte(jedis);
			decrByN(jedis);
			decrByNByte(jedis);

			jedis.flushDB();

			append(jedis);
			lcs(jedis);
			substr(jedis);
			strlen(jedis);

			jedis.flushDB();


		}

		pool.close();
	}

	// region set/setex/setrange/get/getdel/getex/getrange
	static void set(Jedis jedis) {
		String ret = jedis.set("hello", "world");
		System.out.println(ret);
	}

	static void setex(Jedis jedis) {
		String ret = jedis.setex("abc", 1L, "world");
		System.out.println(ret);
		try {
			Thread.sleep(1000L);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}
		boolean exists = jedis.exists("abc");
		System.out.println(!exists);
	}

	static void setrange(Jedis jedis) {
		long ret = jedis.setrange("bcd", 1L, "hello");
		System.out.println(ret);
		String bcd = jedis.get("bcd");
		System.out.println(bcd.endsWith("ello"));
	}

	static void get(Jedis jedis) {
		String world = jedis.get("hello");
		System.out.println(Objects.equals(world, "world"));
	}

	static void setByte(Jedis jedis) {
		String ret = jedis.set("name".getBytes(), "mawen".getBytes());
		System.out.println(ret);
	}

	static void getByte(Jedis jedis) {
		byte[] bytes = jedis.get("name".getBytes());
		System.out.println(new String(bytes).equals("mawen"));
	}

	static void getDel(Jedis jedis) {
		String hello = jedis.getDel("hello");
		System.out.println(hello);
		boolean exists = jedis.exists("hello");
		System.out.println(!exists);
	}

	static void getEx(Jedis jedis) {
		String name = jedis.getEx("name", GetExParams.getExParams().ex(1L));
		System.out.println(Objects.equals(name, "mawen"));
		try {
			Thread.sleep(1000L);
		}
		catch (InterruptedException e) {
			throw new RuntimeException(e);
		}

		boolean exists = jedis.exists("name");
		System.out.println(!exists);
	}

	static void getRange(Jedis jedis) {
		jedis.set("job", "java");
		String content = jedis.getrange("job", 0, 1);
		System.out.println(Objects.equals(content, "ja"));
	}
	// endregion

	// region mset/mget/msetnx
	static void mset(Jedis jedis) {
		String ret = jedis.mset("key1", "value1", "key2", "value2");
		System.out.println(ret);
	}
	static void mget(Jedis jedis) {
		List<String> mget = jedis.mget("key1", "key2");
		System.out.println(Objects.equals(String.join(",", mget), "value1,value2"));
	}
	static void msetnx(Jedis jedis) {
		long ret = jedis.msetnx("key1", "value1", "key2", "value2");
		System.out.println(ret);
	}
	// endregion

	// region incr/incrby/decr/decrby/increbyfloat
	static void incrOne(Jedis jedis) {
		long ret = jedis.incrBy("age1", 1);
		System.out.println(ret);
		String strRet = jedis.get("age1");
		System.out.println(Objects.equals(strRet, "1"));
	}

	static void incrOneByte(Jedis jedis) {
		long ret = jedis.incrBy("age2".getBytes(), 1);
		System.out.println(ret);
		String strRet = jedis.get("age2");
		System.out.println(Objects.equals(strRet, "1"));
	}

	static void incrByN(Jedis jedis) {
		long ret = jedis.incrBy("age3".getBytes(), 10);
		System.out.println(ret);
		String strRet = jedis.get("age3");
		System.out.println(Objects.equals(strRet, "10"));
	}

	static void incrByNByte(Jedis jedis) {
		long ret = jedis.incrBy("age4".getBytes(), 10);
		System.out.println(ret);
		String strRet = jedis.get("age4");
		System.out.println(Objects.equals(strRet, "10"));
	}

	static void decrOne(Jedis jedis) {
		long ret = jedis.decr("age10");
		System.out.println(ret);
		String age10 = jedis.get("age10");
		System.out.println(Objects.equals(age10, "-1"));
	}

	static void decrOneByte(Jedis jedis) {
		long ret = jedis.decr("age11".getBytes());
		System.out.println(ret);
		String age11 = jedis.get("age11");
		System.out.println(Objects.equals(age11, "-1"));
	}

	static void decrByN(Jedis jedis) {
		long ret = jedis.decrBy("age12", 10);
		System.out.println(ret);
		String age12 = jedis.get("age12");
		System.out.println(Objects.equals(age12, "-10"));
	}

	static void decrByNByte(Jedis jedis) {
		long ret = jedis.decrBy("age13".getBytes(), 10);
		System.out.println(ret);
		String age13 = jedis.get("age13");
		System.out.println(Objects.equals(age13, "-10"));
	}

	static void incrByFloat(Jedis jedis) {
		double f = jedis.incrByFloat("float", 1f);
		System.out.println(Objects.equals(f, 1d));
	}
	// endregion

	// region append/substr
	static void append(Jedis jedis) {
		jedis.set("hello", "redis");
		long ret = jedis.append("hello", "-jedis");
		System.out.println(ret);
		String helloRet = jedis.get("hello");
		System.out.println(Objects.equals(helloRet, "redis-jedis"));
	}

	static void substr(Jedis jedis) {
		String ret = jedis.substr("hello", 0, 4);
		System.out.println(Objects.equals(ret, "redis"));
	}
	// endregion

	// region lcs
	static void lcs(Jedis jedis) {
		jedis.set("key1", "ohmytext");
		jedis.set("key2", "mynewtext");
		LCSMatchResult ret = jedis.lcs("key1", "key2", LCSParams.LCSParams());
		String matchString = ret.getMatchString();
		System.out.println(Objects.equals(matchString, "mytext"));
	}
	// endregion

	// region strlen
	static void strlen(Jedis jedis) {
		jedis.set("hello", "world");
		long length = jedis.strlen("hello");
		System.out.println(length == 5L);
	}
	// endregion
}
