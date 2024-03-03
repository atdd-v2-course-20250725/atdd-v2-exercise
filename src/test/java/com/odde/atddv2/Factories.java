package com.odde.atddv2;

import com.github.leeonky.jfactory.JFactory;
import com.github.leeonky.jfactory.repo.JPADataRepository;
import com.odde.atddv2.entity.OrderTransaction;
import com.odde.atddv2.entity.StandaloneOrderTransaction;
import lombok.SneakyThrows;
import okhttp3.OkHttpClient;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.net.URL;
import java.util.Map;

@Configuration
public class Factories {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @SneakyThrows
    @Bean
    public MockServerClient createMockServerClient(@Value("${mock-server.endpoint}") String endpoint) {
        URL url = new URL(endpoint);
        return new MockServerClient(url.getHost(), url.getPort()) {
            @Override
            public void close() {
            }
        };
    }

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Bean
    public RedisTemplate<String, Object> redisUserTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        // 可以换一种你们常用的序列化方式
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer(Map.class));
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }

    @Bean
    public JFactory factorySet() {
        RedisTemplate<String, Object> redisTemplate = redisUserTemplate();
        return new EntityFactory(
                /**
                 * 可改进这个类的实现，主要实现思路是override save 和 queryAll 两个方法
                 *  save 负责保存数据到具体的数据源
                 *  queryAll 负责从某一个数据源获取某一个特定类型的全部数据
                 *  这个例子只是通过简单的类型匹配然后将实例分别保存到数据库或者redis
                 */
                new JPADataRepository(entityManagerFactory.createEntityManager()) {
                    @Override
                    public void save(Object object) {
                        // 你可以改进通过包名或者Super class来改进这个 if 判断
                        if (object instanceof StandaloneOrderTransaction) {
                            StandaloneOrderTransaction transaction = (StandaloneOrderTransaction) object;
                            redisTemplate.opsForValue().set("TRANSACTION_" + transaction.getOrderCode(), object);
                        } else if (object instanceof OrderTransaction) {
                            OrderTransaction transaction = (OrderTransaction) object;
                            redisTemplate.opsForValue().set("TRANSACTION_" + transaction.getOrder().getCode(), object);
                        } else
                            super.save(object);
                    }
                });
    }

    @Bean
    public OkHttpClient okHttpClient() {
        return new OkHttpClient();
    }
}
