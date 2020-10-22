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

/**
 * Redis Client
 *
 * @author Weaxs
 */
@Component
public class RedisClient<V> {

    @Autowired
    private RedisTemplate<String, V> redisTemplate;

    public Boolean setExpire(String key, Long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    public Boolean setExpire(String key, Date expireDate) {
        return redisTemplate.expireAt(key, expireDate);
    }

    public Boolean del(String key) {
        return redisTemplate.delete(key);
    }

    public Long delConllection(List<String> keys) {
        return redisTemplate.delete(keys);
    }

    // String

    public void addString(String key, V value) {
        redisTemplate.opsForValue().set(key, value);
    }

    public void addString(String key, V value, Long expireTime, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, expireTime, unit);
    }

    public V getString(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    public V getAndSetString(String key, V value) {
        return redisTemplate.opsForValue().getAndSet(key, value);
    }

    public Long increString(String key) {
        return redisTemplate.opsForValue().increment(key);
    }

    public Double increString(String key, Double increment) {
        return redisTemplate.opsForValue().increment(key, increment);
    }

    public Long decreString(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    public Long decreString(String key, Long decrement) {
        return redisTemplate.opsForValue().decrement(key, decrement);
    }

    // Hash

    public <HK, HV> void addHashStandalone(String key, HK field, HV value) {
        redisTemplate.opsForHash().put(key, field, value);
    }

    public <HK, HV> void addHashMap(String key, Map<HK, HV> mapValue) {
        redisTemplate.opsForHash().putAll(key, mapValue);
    }

    public <HK, HV> Boolean addHashStandalone(String key, HK field, HV value, Long expireTime, TimeUnit unit) {
        redisTemplate.opsForHash().put(key, field, value);
        return setExpire(key, expireTime, unit);
    }

    public <HK, HV> Boolean addHashMap(String key, Map<HK, HV> mapValue, Long expireTime, TimeUnit unit) {
        redisTemplate.opsForHash().putAll(key, mapValue);
        return setExpire(key, expireTime, unit);
    }

    public <HK, HV> Boolean addHashStandalone(String key, HK field,HV value, Date expireDate) {
        redisTemplate.opsForHash().put(key, field, value);
        return setExpire(key, expireDate);
    }

    public <HK, HV> Boolean addHashMap(String key, Map<HK, HV> mapValue, Date expireDate) {
        redisTemplate.opsForHash().putAll(key, mapValue);
        return setExpire(key, expireDate);
    }

    public <HK, HV> HV getHash(String key, HK field) {
        return redisTemplate.<HK, HV>opsForHash().get(key, field);
    }

    public <HK, HV> Map<HK, HV> getHash(String key) {
        return redisTemplate.<HK, HV>opsForHash().entries(key);
    }

    public <HK, HV> List<HV> getMultiHash(String key, List<HK> fields) {
        return redisTemplate.<HK, HV>opsForHash().multiGet(key, fields);
    }

    // List

    public void addListIndex(String key, Long index, V value) {
        redisTemplate.opsForList().set(key, index, value);
    }

    public Long addListLeft(String key, V value) {
        return redisTemplate.opsForList().leftPush(key, value);
    }

    public Long addListLeft(String key, List<V> values) {
        return redisTemplate.opsForList().leftPushAll(key, values);
    }

    public Long addListLeft(String key, V value, Long expireTime, TimeUnit unit) {
        Long lPush = redisTemplate.opsForList().leftPush(key, value);
        setExpire(key, expireTime, unit);
        return lPush;
    }

    public Long addListLeft(String key, List<V> values, Long expireTime, TimeUnit unit) {
        Long lPushAll = redisTemplate.opsForList().leftPushAll(key, values);
        setExpire(key, expireTime, unit);
        return lPushAll;
    }

    public Long addListLeft(String key, V value, Date expireDate) {
        Long lPush = redisTemplate.opsForList().leftPush(key, value);
        setExpire(key, expireDate);
        return lPush;
    }

    public Long addListLeft(String key, List<V> values, Date expireDate) {
        Long lPushAll = redisTemplate.opsForList().leftPushAll(key, values);
        setExpire(key, expireDate);
        return lPushAll;
    }

    public Long addListRight(String key, V value) {
        return redisTemplate.opsForList().rightPush(key, value);
    }

    public Long addListRight(String key,List<V> values) {
        return redisTemplate.opsForList().rightPushAll(key, values);
    }

    public Long addListRight(String key, V value, Long expireTime, TimeUnit unit) {
        Long rPush = redisTemplate.opsForList().rightPush(key, value);
        setExpire(key, expireTime, unit);
        return rPush;
    }

    public Long addListRight(String key,List<V> values, Long expireTime, TimeUnit unit) {
        Long rPushAll = redisTemplate.opsForList().rightPushAll(key, values);
        setExpire(key, expireTime, unit);
        return rPushAll;
    }

    public Long addListRight(String key, V value, Date expireDate) {
        Long rPush = redisTemplate.opsForList().rightPush(key, value);
        setExpire(key, expireDate);
        return rPush;
    }

    public Long addListRight(String key,List<V> values, Date expireDate) {
        Long rPushAll = redisTemplate.opsForList().rightPushAll(key, values);
        setExpire(key, expireDate);
        return rPushAll;
    }

    public V getListIndex(String key, Long index) {
        return redisTemplate.opsForList().index(key, index);
    }

    public List<V> getList(String key, Long start, Long end) {
        return redisTemplate.opsForList().range(key, start, end);
    }

    public List<V> getList(String key) {
        Long size = redisTemplate.opsForList().size(key);
        return size == null ? null : redisTemplate.opsForList().range(key, 0, size - 1);
    }

    public V leftPop(String key) {
        return redisTemplate.opsForList().leftPop(key);
    }

    public V rightPop(String key) {
        return redisTemplate.opsForList().rightPop(key);
    }

    public V rightPopAndLeftPush(String key) {
        return rightPopAndLeftPush(key, key);
    }

    public V rightPopAndLeftPush(String keyPop, String keyPush) {
        return redisTemplate.opsForList().rightPopAndLeftPush(keyPop, keyPush);
    }

    // Set

    @SafeVarargs
    public final Long addSet(String key, V... values) {
        return redisTemplate.opsForSet().add(key, values);
    }

    @SafeVarargs
    public final Long addSet(Long expireTime, TimeUnit unit, String key, V... values) {
        Long add = redisTemplate.opsForSet().add(key, values);
        setExpire(key, expireTime, unit);
        return add;
    }

    @SafeVarargs
    public final Long addSet(Date expireDate, String key, V... values) {
        Long add = redisTemplate.opsForSet().add(key, values);
        setExpire(key, expireDate);
        return add;
    }

    public Set<V> getSet(String key) {
        return redisTemplate.opsForSet().members(key);
    }

    public Set<V> difference(List<String> keys) {
        return redisTemplate.opsForSet().difference(keys);
    }

    public Set<V> differenceAndStore(List<String> keys, String destKey) {
        Long deferenceAndStore = redisTemplate.opsForSet().differenceAndStore(keys, destKey);
        return redisTemplate.opsForSet().members(destKey);
    }

    public Set<V> differenceAndStore(List<String> keys, String destKey, Long expireTime, TimeUnit unit) {
        Long deferenceAndStore = redisTemplate.opsForSet().differenceAndStore(keys, destKey);
        setExpire(destKey, expireTime, unit);
        return redisTemplate.opsForSet().members(destKey);
    }

    public Set<V> differenceAndStore(List<String> keys, String destKey, Date expireDate) {
        Long deferenceAndStore = redisTemplate.opsForSet().differenceAndStore(keys, destKey);
        setExpire(destKey, expireDate);
        return redisTemplate.opsForSet().members(destKey);
    }

    public Set<V> intersect(List<String> keys) {
        return redisTemplate.opsForSet().intersect(keys);
    }

    public Set<V> intersectAndStore(List<String> keys, String destKey) {
        Long intersectAndStore = redisTemplate.opsForSet().intersectAndStore(keys, destKey);
        return redisTemplate.opsForSet().members(destKey);
    }

    public Set<V> intersectAndStore(List<String> keys, String destKey, Long expireTime, TimeUnit unit) {
        Long intersectAndStore = redisTemplate.opsForSet().intersectAndStore(keys, destKey);
        setExpire(destKey, expireTime, unit);
        return redisTemplate.opsForSet().members(destKey);
    }

    public Set<V> intersectAndStore(List<String> keys, String destKey, Date expireDate) {
        Long intersectAndStore = redisTemplate.opsForSet().intersectAndStore(keys, destKey);
        setExpire(destKey, expireDate);
        return redisTemplate.opsForSet().members(destKey);
    }

    public Set<V> union(List<String> keys) {
        return redisTemplate.opsForSet().union(keys);
    }

    public Set<V> unionAndStore(List<String> keys, String destKey) {
        Long unionAndStore = redisTemplate.opsForSet().unionAndStore(keys, destKey);
        return redisTemplate.opsForSet().members(destKey);
    }

    public Set<V> unionAndStore(List<String> keys, String destKey, Long expireTime, TimeUnit unit) {
        Long unionAndStore = redisTemplate.opsForSet().unionAndStore(keys, destKey);
        setExpire(destKey, expireTime, unit);
        return redisTemplate.opsForSet().members(destKey);
    }

    public Set<V> unionAndStore(List<String> keys, String destKey, Date expireDate) {
        Long unionAndStore = redisTemplate.opsForSet().unionAndStore(keys, destKey);
        setExpire(destKey, expireDate);
        return redisTemplate.opsForSet().members(destKey);
    }

    // ZSet

    public Boolean addZSetStandalone(String key, V value, Double socre) {
        return redisTemplate.opsForZSet().add(key, value, socre);
    }

    public Long addZSetConllection(String key, Map<Double, V> value) {
        return redisTemplate.opsForZSet().add(key, map2TypedTupleSet(value));
    }

    public Boolean addZSetStandalone(String key, V value, Double socre, Date expireDate) {
        return addZSetStandalone(key, value, socre) && setExpire(key, expireDate);
    }

    public Long addZSetConllection(String key, Map<Double, V> value, Date expireDate) {
        Long add = addZSetConllection(key, value);
        setExpire(key, expireDate);
        return add;
    }

    public Boolean addZSetStandalone(String key, V value, Double socre, Long expireTime, TimeUnit unit) {
        return addZSetStandalone(key, value, socre) && setExpire(key, expireTime, unit);
    }

    public Long addZSetConllection(String key, Map<Double, V> value, Long expireTime, TimeUnit unit) {
        Long add = addZSetConllection(key, value);
        setExpire(key, expireTime, unit);
        return add;
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
        if (typedTupleSet == null) {
            return null;
        }
        Map<Double, V> values = new HashMap<>();
        for (ZSetOperations.TypedTuple<V> typedTuple:typedTupleSet) {
            values.put(typedTuple.getScore(), typedTuple.getValue());
        }
        return values;
    }

}
