package com.mawen.think.in.spring.data.redis.client.jedis.connect;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/12
 */
public class JedisPoolExample {

	public static void main(String[] args) {

		JedisPool pool = new JedisPool("localhost", 6379);

		try (Jedis jedis = pool.getResource()) {
			String pong = jedis.ping();
			System.out.println(pong);
		}

		pool.close();
	}
}
