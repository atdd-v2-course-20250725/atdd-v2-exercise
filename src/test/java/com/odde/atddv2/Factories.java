package com.odde.atddv2;

import com.github.leeonky.jfactory.CompositeRepository;
import com.github.leeonky.jfactory.DataRepository;
import com.github.leeonky.jfactory.JFactory;
import com.github.leeonky.jfactory.repo.JPADataRepository;
import com.odde.atddv2.entity.mongo.Express;
import lombok.SneakyThrows;
import org.mockserver.client.MockServerClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.net.URL;
import java.util.Collection;

@Configuration
public class Factories {

    @PersistenceUnit
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private MongoTemplate mongoTemplate;

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

    @Bean
    public JFactory factorySet() {
        JPADataRepository jpaDataRepository = new JPADataRepository(entityManagerFactory.createEntityManager());
        CompositeRepository repository = new CompositeRepository();
        DataRepository mongoRepository = new DataRepository() {
            @Override
            public <T> Collection<T> queryAll(Class<T> type) {
                return mongoTemplate.findAll(type);
            }

            @Override
            public void clear() {
                mongoTemplate.getDb().drop();
            }

            @Override
            public void save(Object o) {
                mongoTemplate.save(o);
            }
        };
        repository.addRepository(t -> t.equals(Express.class), mongoRepository);
        repository.addRepository(t -> t.getPackageName().endsWith("entity"), jpaDataRepository);
        return new EntityFactory(repository);
    }
}
