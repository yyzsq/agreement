
package com.gemframework.config.shiro.cache;

import com.gemframework.common.utils.GemRedisUtils;
import com.gemframework.common.utils.GemSerializeUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collection;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Gem自定义Shiro缓存
 * @param <K>
 * @param <V>
 */
@Slf4j
@Component
public class GemCache<K,V> implements Cache<K,V>{

    private final String SHIRO_CACHE_PERFIX = "shiro-cache:";
    private String getKey(K key){
        return SHIRO_CACHE_PERFIX + key;
    }

    @Autowired
    GemRedisUtils<String> gemRedisUtils;

    @SneakyThrows
    @Override
    public V put(K k, V v) throws CacheException {
        log.debug("put-cache-key:"+k);
        String key = getKey(k);
        String value = GemSerializeUtils.serialize(v);
        gemRedisUtils.set(key,value);
        gemRedisUtils.expire(key,30, TimeUnit.MINUTES);
        return v;
    }

    @Override
    public V get(K k) throws CacheException {
        log.debug("get-cache-key:"+k);
        String key = getKey(k);
        String value = gemRedisUtils.get(key);
        if(StringUtils.isNotBlank(value)){
            try {
                return (V) GemSerializeUtils.serializeToObject(value);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    @Override
    public V remove(K k) throws CacheException {
        log.debug("delete-cache-key:"+k);
        V v = get(k);
        if(v != null){
            gemRedisUtils.delete(getKey(k));
        }
        return v;
    }

    @Override
    public void clear() throws CacheException {

    }

    @Override
    public int size() {
        return 0;
    }

    @Override
    public Set<K> keys() {
        return null;
    }

    @Override
    public Collection<V> values() {
        return null;
    }
}
