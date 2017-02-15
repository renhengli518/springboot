package com.renhengli.config;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;

import redis.clients.jedis.JedisPoolConfig;
/**
 * 
 * @author renhengli
 *
 */
@Configuration
@EnableCaching
public class RedisConfig extends CachingConfigurerSupport {
	public static String host;

	public static Integer port;

	public static String password;

	public static Integer maxIdle;

	public static Integer minIdle;

	public static Integer timeOut;

	public static Integer maxTotal;
	
	public static Integer maxWait;

	static {
		ClassPathResource resource = null;
		Properties props = null;
		try {
			resource = new ClassPathResource("/redis.properties");
			props = PropertiesLoaderUtils.loadProperties(resource);
			host = (String) props.get("spring.redis.host");
			port = Integer.parseInt((String) props.get("spring.redis.port"));
			password = (String) props.get("spring.redis.password");
			maxIdle = Integer.parseInt((String) props.get("spring.redis.pool.maxIdle"));
			minIdle = Integer.parseInt((String) props.get("spring.redis.pool.minIdle"));
			timeOut = Integer.parseInt((String) props.get("spring.redis.timeOut"));
			maxTotal = Integer.parseInt((String) props.get("spring.redis.maxTotal"));
			maxWait = Integer.parseInt((String) props.get("spring.redis.pool.maxWait"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Bean
	public RedisConnectionFactory jedisConnectionFactory() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMinIdle(minIdle);
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMaxWaitMillis(maxWait);
		poolConfig.setTestOnBorrow(true);
		poolConfig.setTestOnReturn(true);
		JedisConnectionFactory ob = new JedisConnectionFactory(poolConfig);
		ob.setHostName(host);
		ob.setPort(port);
		ob.setPassword(password);
		ob.setTimeout(timeOut);
		ob.setUsePool(true);
		return ob;
	}

	@Bean
	public KeyGenerator keyGenerator() {
		return new KeyGenerator() {
			@Override
			public Object generate(Object target, Method method, Object... params) {
				StringBuilder sb = new StringBuilder();
				sb.append(target.getClass().getName());
				sb.append(method.getName());
				for (Object obj : params) {
					sb.append(obj.toString());
				}
				return sb.toString();
			}
		};
	}

	@Bean
	public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {
		RedisCacheManager rcm = new RedisCacheManager(redisTemplate);
		// 设置缓存过期时间
		rcm.setDefaultExpiration(600);// 秒
		return rcm;
	}

	@Bean
	public RedisTemplate<String, ?> redisTemplate(RedisConnectionFactory factory) {
		StringRedisTemplate template = new StringRedisTemplate(factory);
		Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(
				Object.class);
		ObjectMapper om = new ObjectMapper();
		om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
		om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
		jackson2JsonRedisSerializer.setObjectMapper(om);
		template.setValueSerializer(jackson2JsonRedisSerializer);
		return template;
	}

}