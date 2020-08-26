package com.st.newtest.config;

import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;

/**
 * @Description: 内存缓存配置类
 * @Author: Zoutao
 * @Date: 2020/4/14
 */

@Configuration
public class UrlCache {
    @Bean
    public Cache<String, Integer> getCache() {
        return CacheBuilder.newBuilder().expireAfterWrite(2L, TimeUnit.SECONDS).build();// 缓存有效期为2秒
    }
}