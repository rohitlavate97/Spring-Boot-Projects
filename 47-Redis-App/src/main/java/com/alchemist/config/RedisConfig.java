package com.alchemist.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

import com.alchemist.binding.Country;

@Configuration
public class RedisConfig {
	@Bean
	public JedisConnectionFactory jedisconn() {
		JedisConnectionFactory jedis = new JedisConnectionFactory();
		//we can set redis server properties here
		/*
		 * jedis.setHostName("127.0.0.1"); jedis.setPort(6379);
		 * jedis.setPassword("yourPassword");
		 */
		return jedis;
	}
	
	@Bean
	public RedisTemplate<String, Country> redisTemplate(){         //kind of data stored in redis i.e Country type
		RedisTemplate<String, Country> rt = new RedisTemplate<>();    
		rt.setConnectionFactory(jedisconn());             //Injecting JedisConnectionFactory object here
		return rt;
	}
	
	
}
