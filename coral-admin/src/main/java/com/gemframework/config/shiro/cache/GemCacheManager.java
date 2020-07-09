
package com.gemframework.config.shiro.cache;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.apache.shiro.cache.CacheManager;
import org.springframework.beans.factory.annotation.Autowired;

public class GemCacheManager implements CacheManager {

    @Autowired
    private GemCache gemCache;

    @Override
    public <K, V> Cache<K, V> getCache(String s) throws CacheException {
        return gemCache;
    }
}