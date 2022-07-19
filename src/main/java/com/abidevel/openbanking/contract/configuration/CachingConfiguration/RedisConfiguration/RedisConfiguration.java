package com.abidevel.openbanking.contract.configuration.CachingConfiguration.RedisConfiguration;

import java.time.Duration;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

@Configuration
@EnableCaching
public class RedisConfiguration {

// For Partial Configuration    
// @Bean
//     RedisCacheConfiguration cacheConfiguration() {
//     return RedisCacheConfiguration.defaultCacheConfig()
//       .entryTtl(Duration.ofMinutes(60))
//       .disableCachingNullValues()
//       .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()));
// }
// For Full Control
@Bean
    RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
    return builder -> builder
      .withCacheConfiguration("transactionCache",
        RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(30))
            .disableCachingNullValues()
            .serializeValuesWith(SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer())))
      .withCacheConfiguration("userCache",
        RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(5)));
}
}
