package com.mawen.think.in.spring.data.redis.client.jedis.pipeline;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.Pipeline;

/**
 * @author <a href="1181963012mw@gmail.com">mawen12</a>
 * @since 2024/4/19
 */
public class PipelineExample {

	public static void main(String[] args) {
		JedisPool pool = new JedisPool(new JedisPoolConfig(), "localhost", 6379, 2000);

		try (Jedis jedis = pool.getResource()) {
			jedis.flushDB();

			Pipeline pipeline = jedis.pipelined();
			pipeline.set("hello", "world");
			pipeline.set("name", "mawen");
			pipeline.sync();

			System.out.println(jedis.get("hello"));
			System.out.println(jedis.get("name"));
		}

		pool.close();
	}
}
