package org.weaxsey.redis;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.DefaultTypedTuple;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.TimeUnit;

@Component
public class RedisClient<V> {

    @Autowired
    private RedisTemplate<String, V> redisTemplate;

    public Boolean setExpire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public Boolean setExpire(String key, Date date) {
        return redisTemplate.expireAt(key, date);
    }

    public Boolean addZSetStandalone(String key, V value, Double socre) {
        return redisTemplate.opsForZSet().add(key, value, socre);
    }

    public Long addZSetConllection(String key, Map<Double, V> value) {
        return redisTemplate.opsForZSet().add(key, map2TypedTupleSet(value));
    }

    public Map<Double, V> getZSetBySocre(String key, Double minSocre, Double maxSocre) {
        return typedTupleSet2Map(redisTemplate.opsForZSet().rangeByScoreWithScores(key, minSocre, maxSocre));
    }

    private Set<ZSetOperations.TypedTuple<V>> map2TypedTupleSet(@NonNull Map<Double, V> value) {
        Set<ZSetOperations.TypedTuple<V>> typedTupleSet = new HashSet<>();
        for (Map.Entry<Double, V> valueEntry:value.entrySet()) {
            ZSetOperations.TypedTuple<V> typedTuple = new DefaultTypedTuple<>(valueEntry.getValue(), valueEntry.getKey());
            typedTupleSet.add(typedTuple);
        }
        return typedTupleSet;
    }

    private Map<Double, V> typedTupleSet2Map(@Nullable Set<ZSetOperations.TypedTuple<V>> typedTupleSet) {
        if (typedTupleSet == null)
            return null;
        Map<Double, V> values = new HashMap<>();
        for (ZSetOperations.TypedTuple<V> typedTuple:typedTupleSet) {
            values.put(typedTuple.getScore(), typedTuple.getValue());
        }
        return values;
    }

}
