package com.mesalab.engine.component;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.mesalab.engine.service.MetadataService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class SchemaCache {

    @Autowired
    MetadataService metadataService;

    LoadingCache<String, Object> loadingCache;

    @PostConstruct
    void initSchema() {
        loadingCache = CacheBuilder.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(30, TimeUnit.MINUTES)
                .recordStats()
                .build(new CacheLoader<String, Object>() {
                    @Override
                    public Object load(String key) throws Exception {
                        Object value = metadataService.getAllSchema();
                        return value;
                    }

                });
        log.info("SchemaCache Inited");
    }


    public Object get(String key) {
        try {
            return loadingCache.get(key);
        } catch (Exception e) {
            log.error(" Schema Cache ,get key error ", e);
            return null;
        }
    }


    public void put(String key, Object value) {
        loadingCache.put(key, value);
    }

    public void remove(String key) {
        loadingCache.invalidate(key);
    }

    public void removeAll() {
        loadingCache.invalidateAll();
    }

    public CacheStats getStats() {
        return loadingCache.stats();
    }


}
