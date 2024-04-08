//package com.fyp.hotel.config;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
//import com.fyp.hotel.dto.DisplayHotelWithAmenitiesDto;
//import com.fyp.hotel.dto.vendorDto.VendorRevenueDTO;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.data.redis.cache.RedisCacheConfiguration;
//import org.springframework.data.redis.cache.RedisCacheManager;
//import org.springframework.data.redis.connection.RedisConnectionFactory;
//import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
//import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.serializer.*;
//
//import java.time.Duration;
//
//@Configuration
//public class RedisConfig {
//
//    @Value("${redis.host}")
//    private String redisHost;
//
//    @Value("${redis.port}")
//    private int redisPort;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    // lettuce connection factory is the most common way to connect to Redis from a Spring application
//    // it is a thread-safe connection factory that allows multiple threads to share a single connection
//    //it takes a RedisStandaloneConfiguration object as an argument
//    @Bean
//    public LettuceConnectionFactory redisConnectionFactory() {
//        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(redisHost, redisPort);
//        return new LettuceConnectionFactory(configuration);
//    }
//
//    // redis cache configuration is used to configure the cache to store the data in Redis
//    //it helps to set the expiration time of the cache
//    //it modifies the default cache configuration
//    private RedisCacheConfiguration myDefaultCacheConfig(Duration duration) {
//        return RedisCacheConfiguration
//                .defaultCacheConfig()
//                .entryTtl(duration);
//    }
//
//    // redis template is used to interact with Redis
//    //it is used to store and retrieve data from Redis
//    //redis connection factory is used to connect to Redis, it is like a connection pool
////    @Bean
////    public RedisTemplate<String, DisplayHotelWithAmenitiesDto> redisTemplate(RedisConnectionFactory connectionFactory) {
////
////        // create a new RedisTemplate object to interact with Redis
////        // set the connection factory to the RedisConnectionFactory object
////        //it uses the Jackson2JsonRedisSerializer to serialize and deserialize the data
////        RedisTemplate<String, DisplayHotelWithAmenitiesDto> template = new RedisTemplate<>();
////
////        // Setting the connection factory because RedisTemplate requires it for initialization of the connection
////        template.setConnectionFactory(connectionFactory);
////
////        // Use Jackson for JSON serialization/deserialization
////        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer(objectMapper);
////
////        //setdefaultserializer is used to set the default serializer for the RedisTemplate
////        //it is used to serialize the data before storing it in Redis
////        //it is used to deserialize the data after retrieving it from Redis
////        //so we set it as jsonSerializer because we are using Jackson for serialization/deserialization
////        template.setDefaultSerializer(jsonSerializer);
////
////        //setkeyserializer is used to set the serializer for the key
////        //it is used to serialize the key before storing it in Redis
////        template.setKeySerializer(new StringRedisSerializer());
////
////        //setvalueserializer is used to set the serializer for the value
////        //it is used to serialize the value before storing it in Redis
////        template.setValueSerializer(jsonSerializer);
////
////        return template;
////    }
//
//    @Bean
//    public RedisTemplate<String, DisplayHotelWithAmenitiesDto> redisTemplateGetHotels(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, DisplayHotelWithAmenitiesDto> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer( new GenericJackson2JsonRedisSerializer(objectMapper));
//        return template;
//    }
//
//    @Bean
//    public RedisTemplate<String, VendorRevenueDTO> redisTemplateVendorRevenue(RedisConnectionFactory connectionFactory) {
//        RedisTemplate<String, VendorRevenueDTO> template = new RedisTemplate<>();
//        template.setConnectionFactory(connectionFactory);
//        template.setDefaultSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//        template.setKeySerializer(new StringRedisSerializer());
//        template.setValueSerializer(new GenericJackson2JsonRedisSerializer(objectMapper));
//        return template;
//    }
//
//
//
//    // redis cache manager is used to manage the cache in Redis
//    //additionally, it is used to set the default cache configuration like the expiration time of the cache, etc.
//    @Bean
//    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
//
//        // create a new RedisCacheConfiguration object to set the default cache configuration
//        //.entryTtl is used to set the expiration time of the cache
//        //disableCachingNullValues is used to disable caching null values means if the value is null then it will not be cached
//        RedisCacheConfiguration cacheConfig = myDefaultCacheConfig(Duration.ofMinutes(10)).disableCachingNullValues();
//
//        return RedisCacheManager.builder(connectionFactory)
//                .cacheDefaults(cacheConfig)
//                .withCacheConfiguration("hotels", myDefaultCacheConfig(Duration.ofMinutes(5)).serializeValuesWith(
//                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(String.valueOf(DisplayHotelWithAmenitiesDto.class)))))
//                .withCacheConfiguration("vendorRevenue", myDefaultCacheConfig(Duration.ofMinutes(5)).serializeValuesWith(
//                        RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer(String.valueOf(VendorRevenueDTO.class)))))
//                .build();
//    }
//
//    // Jackson2JsonRedisSerializer is used to serialize and deserialize the data in Redis
//    @Bean
//    public Jackson2JsonRedisSerializer jackson2JsonRedisSerializer() {
//
//        ObjectMapper objectMapper = new ObjectMapper();
//        //objectMapper is used to serialize and deserialize the data
//        //activateDefaultTyping is used to enable the default typing for the object mapper
//        //means it will include the type information in the JSON string
//        //for example, if we have a class A and class B extends class A then it will include the type information in the JSON string
//        objectMapper.activateDefaultTyping(LaissezFaireSubTypeValidator.instance, ObjectMapper.DefaultTyping.NON_FINAL);
//
//        return new Jackson2JsonRedisSerializer<>(Object.class);
//    }
//}
//
//
//
