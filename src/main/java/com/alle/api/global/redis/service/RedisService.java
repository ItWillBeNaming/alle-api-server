package com.alle.api.global.redis.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;


    public void setData(String key, Object value, Long time, TimeUnit timeUnit) {
        redisTemplate.opsForValue().set(key, value.toString(), time, timeUnit);
    }


    public Object getData(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public Set<Long> getLikeData(String key) {
        Set<Object> rawData = redisTemplate.opsForSet().members(key);

        if (rawData == null) {
            return Collections.emptySet();
        }

        return rawData.stream()
                .map(Object::toString)
                .map(Long::parseLong)
                .collect(Collectors.toSet());
    }

    public void deleteData(String key) {
        redisTemplate.delete(key);
    }




    public boolean isPresent(String userKey, Long summaryId) {
        return Boolean.TRUE.equals(redisTemplate.opsForSet().isMember(userKey, summaryId.toString()));
    }



}
