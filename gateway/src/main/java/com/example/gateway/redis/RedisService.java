package com.example.gateway.redis;

import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import org.springframework.beans.factory.annotation.Value;

@Component
public class RedisService {

    private JedisPool jedisPool;

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    public RedisService() {
        // Можно настроить JedisPoolConfig если необходимо
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        this.jedisPool = new JedisPool(poolConfig, redisHost, redisPort);
    }

    public String get(String key) {
        try(Jedis jedis = jedisPool.getResource()) {
            return jedis.lpop(key);
        }
    }

    public void set(String key, String value) {
        try(Jedis jedis = jedisPool.getResource()) {
            jedis.rpush(key, value); // Используем lpush (добавляет в начало)
        }
    }

    public void destroy() {
        if (jedisPool != null) {
            jedisPool.close();
        }
    }
}