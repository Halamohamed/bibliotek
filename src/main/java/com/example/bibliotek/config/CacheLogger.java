package com.example.bibliotek.config;

import lombok.extern.slf4j.Slf4j;
import org.ehcache.event.CacheEvent;
import org.ehcache.event.CacheEventListener;

import javax.cache.event.CacheEntryListener;

@Slf4j
public class CacheLogger implements CacheEventListener<Object, Object> {


    @Override
    public void onEvent(CacheEvent<?, ?> cacheEvent) {
        log.info("Key: {} | EventType: {} | Ol value: {} | New value:",
                cacheEvent.getKey(),cacheEvent.getType(), cacheEvent.getOldValue(), cacheEvent.getNewValue());
    }
}
