package com.mawen.think.in.spring.data.redis.client.jedis.connect;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPooled;

/**
 * {@link JedisPooled} compare with {@link JedisPool} that without try-with-resource
 *
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/12
 */
public class JedisPooledExample {

	public static void main(String[] args) {

		JedisPooled jedis = new JedisPooled("localhost", 6379);

		String ret = jedis.set("hello".getBytes(), "redis".getBytes());
		System.out.println(ret); // OK

		long delRet = jedis.del("hello");
		System.out.println(delRet); // 1

		jedis.close();
	}
}
