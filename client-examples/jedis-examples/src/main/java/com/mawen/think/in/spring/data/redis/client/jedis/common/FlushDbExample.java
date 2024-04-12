package com.mawen.think.in.spring.data.redis.client.jedis.common;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPooled;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/12
 */
public class FlushDbExample {

	public static void main(String[] args) throws InterruptedException {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 2000);

		try (Jedis jedis = pool.getResource()) {
			jedis.set("foo", "bar");
			jedis.flushDB();
			String hello = jedis.get("hello");
			System.out.println(hello == null);
		}

		pool.close();
	}
}
