package com.acme.biz.redis.connection;

import io.micrometer.core.instrument.*;
import io.micrometer.core.instrument.Timer;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.KeyScanOptions;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.types.Expiration;
import org.springframework.data.redis.core.types.RedisClientInfo;
import org.springframework.data.redis.domain.geo.GeoReference;
import org.springframework.data.redis.domain.geo.GeoShape;
import org.springframework.lang.Nullable;
import org.springframework.util.ClassUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.time.Duration;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class DelegateRedisConnectionFactory implements RedisConnectionFactory {

    private final RedisConnectionFactory delegate;

    private final MeterRegistry meterRegistry;

    private final Map<String, Counter> completionCounter = new ConcurrentHashMap<>();
    private final Map<String, Timer> completionTimer = new ConcurrentHashMap<>();

    private final Map<String, SuccessRate> completionSuccessRate = new ConcurrentHashMap<>();

    public DelegateRedisConnectionFactory(RedisConnectionFactory redisConnectionFactory, MeterRegistry meterRegistry) {
        this.delegate = redisConnectionFactory;
        this.meterRegistry = meterRegistry;
    }

    @Override
    public RedisConnection getConnection() {
        RedisConnection connection = delegate.getConnection();

        return createStaticRedisConnectionProxy(connection);
    }

    protected RedisConnection createStaticRedisConnectionProxy(RedisConnection connection) {

        return new DelegateRedisConnectionProxy(connection);
    }

    protected RedisConnection createDynamicRedisConnectionProxy(RedisConnection connection) {

        Class<?>[] ifcs = ClassUtils.getAllInterfacesForClass(connection.getClass(), getClass().getClassLoader());

        return (RedisConnection) Proxy.newProxyInstance(connection.getClass().getClassLoader(), ifcs,
                new JDKDynamicProxyRedisConnection(connection));
    }

    @Override
    public RedisClusterConnection getClusterConnection() {
        return delegate.getClusterConnection();
    }

    @Override
    public boolean getConvertPipelineAndTxResults() {
        return delegate.getConvertPipelineAndTxResults();
    }

    @Override
    public RedisSentinelConnection getSentinelConnection() {
        return delegate.getSentinelConnection();
    }

    @Override
    public DataAccessException translateExceptionIfPossible(RuntimeException ex) {
        return delegate.translateExceptionIfPossible(ex);
    }

    private SuccessRate getSuccessRate(String key) {
        SuccessRate successRate = completionSuccessRate.computeIfAbsent(key, k -> new SuccessRate());
        return meterRegistry.gauge("redis.command.success.rate",
                Tags.of(Tag.of("command", "set"), Tag.of("redis_key", key)).stream().toList(),
                successRate,
                SuccessRate::getSuccessRate);
    }

    private Timer getTimer(String key) {
        return completionTimer.computeIfAbsent(key, k ->
                Timer.builder("redis.command.set.timer")
                .tag("command", "set")
                .tag("redis_key", key).register(meterRegistry));
    }

    private Counter getCounter(String key) {
        return completionCounter.computeIfAbsent(key, k ->
                Counter.builder("redis.command.set.counter")
                .tag("command", "set")
                .tag("redis_key", k).register(meterRegistry));
    }

    class DelegateRedisConnectionProxy implements RedisConnectionUtils.RedisConnectionProxy {

        private RedisConnection delegate;

        public DelegateRedisConnectionProxy(RedisConnection delegate) {
            this.delegate = delegate;
        }

        @Override
        public Boolean set(byte[] key, byte[] value) {
            System.out.println("test");
            String keyValue = new String(key);
            Counter counter = getCounter(keyValue);
            Timer timer =getTimer(keyValue);
            SuccessRate successRate = getSuccessRate(keyValue);
            Boolean result = timer.record(() -> {
                try {
                    return delegate.set(key, value);
                } catch (Exception e) {
                    successRate.failIncrease();
                    throw e;
                } finally {
                    successRate.totalIncrease();
                }
            });

            counter.increment();
            return result;
        }



        @Override
        public RedisGeoCommands geoCommands() {
            return delegate.geoCommands();
        }

        @Override
        public RedisHashCommands hashCommands() {
            return delegate.hashCommands();
        }

        @Override
        public RedisHyperLogLogCommands hyperLogLogCommands() {
            return delegate.hyperLogLogCommands();
        }

        @Override
        public RedisKeyCommands keyCommands() {
            return delegate.keyCommands();
        }

        @Override
        public RedisListCommands listCommands() {
            return delegate.listCommands();
        }

        @Override
        public RedisSetCommands setCommands() {
            return delegate.setCommands();
        }

        @Override
        public RedisScriptingCommands scriptingCommands() {
            return delegate.scriptingCommands();
        }

        @Override
        public RedisServerCommands serverCommands() {
            return delegate.serverCommands();
        }

        @Override
        public RedisStreamCommands streamCommands() {
            return delegate.streamCommands();
        }

        @Override
        public RedisStringCommands stringCommands() {
            return delegate.stringCommands();
        }

        @Override
        public RedisZSetCommands zSetCommands() {
            return delegate.zSetCommands();
        }

        @Override
        public void close() throws DataAccessException {
            delegate.close();
        }

        @Override
        public boolean isClosed() {
            return delegate.isClosed();
        }

        @Override
        public Object getNativeConnection() {
            return delegate.getNativeConnection();
        }

        @Override
        public boolean isQueueing() {
            return delegate.isQueueing();
        }

        @Override
        public boolean isPipelined() {
            return delegate.isPipelined();
        }

        @Override
        public void openPipeline() {
            delegate.openPipeline();
        }

        @Override
        public List<Object> closePipeline() throws RedisPipelineException {
            return delegate.closePipeline();
        }

        @Override
        public RedisSentinelConnection getSentinelConnection() {
            return delegate.getSentinelConnection();
        }

        @Override
        @Nullable
        public Object execute(String command, byte[]... args) {
            return delegate.execute(command, args);
        }

        @Override
        @Nullable
        public Boolean copy(byte[] sourceKey, byte[] targetKey, boolean replace) {
            return delegate.copy(sourceKey, targetKey, replace);
        }

        @Override
        @Nullable
        public Boolean exists(byte[] key) {
            return delegate.exists(key);
        }

        @Override
        @Nullable
        public Long exists(byte[]... keys) {
            return delegate.exists(keys);
        }

        @Override
        @Nullable
        public Long del(byte[]... keys) {
            return delegate.del(keys);
        }

        @Override
        @Nullable
        public Long unlink(byte[]... keys) {
            return delegate.unlink(keys);
        }

        @Override
        @Nullable
        public DataType type(byte[] key) {
            return delegate.type(key);
        }

        @Override
        @Nullable
        public Long touch(byte[]... keys) {
            return delegate.touch(keys);
        }

        @Override
        @Nullable
        public Set<byte[]> keys(byte[] pattern) {
            return delegate.keys(pattern);
        }

        @Override
        public Cursor<byte[]> scan(KeyScanOptions options) {
            return delegate.scan(options);
        }

        @Override
        public Cursor<byte[]> scan(ScanOptions options) {
            return delegate.scan(options);
        }

        @Override
        @Nullable
        public byte[] randomKey() {
            return delegate.randomKey();
        }

        @Override
        public void rename(byte[] oldKey, byte[] newKey) {
            delegate.rename(oldKey, newKey);
        }

        @Override
        @Nullable
        public Boolean renameNX(byte[] oldKey, byte[] newKey) {
            return delegate.renameNX(oldKey, newKey);
        }

        @Override
        @Nullable
        public Boolean expire(byte[] key, long seconds) {
            return delegate.expire(key, seconds);
        }

        @Override
        @Nullable
        public Boolean pExpire(byte[] key, long millis) {
            return delegate.pExpire(key, millis);
        }

        @Override
        @Nullable
        public Boolean expireAt(byte[] key, long unixTime) {
            return delegate.expireAt(key, unixTime);
        }

        @Override
        @Nullable
        public Boolean pExpireAt(byte[] key, long unixTimeInMillis) {
            return delegate.pExpireAt(key, unixTimeInMillis);
        }

        @Override
        @Nullable
        public Boolean persist(byte[] key) {
            return delegate.persist(key);
        }

        @Override
        @Nullable
        public Boolean move(byte[] key, int dbIndex) {
            return delegate.move(key, dbIndex);
        }

        @Override
        @Nullable
        public Long ttl(byte[] key) {
            return delegate.ttl(key);
        }

        @Override
        @Nullable
        public Long ttl(byte[] key, TimeUnit timeUnit) {
            return delegate.ttl(key, timeUnit);
        }

        @Override
        @Nullable
        public Long pTtl(byte[] key) {
            return delegate.pTtl(key);
        }

        @Override
        @Nullable
        public Long pTtl(byte[] key, TimeUnit timeUnit) {
            return delegate.pTtl(key, timeUnit);
        }

        @Override
        @Nullable
        public List<byte[]> sort(byte[] key, SortParameters params) {
            return delegate.sort(key, params);
        }

        @Override
        @Nullable
        public Long sort(byte[] key, SortParameters params, byte[] storeKey) {
            return delegate.sort(key, params, storeKey);
        }

        @Override
        @Nullable
        public byte[] dump(byte[] key) {
            return delegate.dump(key);
        }

        @Override
        public void restore(byte[] key, long ttlInMillis, byte[] serializedValue) {
            delegate.restore(key, ttlInMillis, serializedValue);
        }

        @Override
        public void restore(byte[] key, long ttlInMillis, byte[] serializedValue, boolean replace) {
            delegate.restore(key, ttlInMillis, serializedValue, replace);
        }

        @Override
        @Nullable
        public ValueEncoding encodingOf(byte[] key) {
            return delegate.encodingOf(key);
        }

        @Override
        @Nullable
        public Duration idletime(byte[] key) {
            return delegate.idletime(key);
        }

        @Override
        @Nullable
        public Long refcount(byte[] key) {
            return delegate.refcount(key);
        }

        @Override
        @Nullable
        public byte[] get(byte[] key) {
            return delegate.get(key);
        }

        @Override
        @Nullable
        public byte[] getDel(byte[] key) {
            return delegate.getDel(key);
        }

        @Override
        @Nullable
        public byte[] getEx(byte[] key, Expiration expiration) {
            return delegate.getEx(key, expiration);
        }

        @Override
        @Nullable
        public byte[] getSet(byte[] key, byte[] value) {
            return delegate.getSet(key, value);
        }

        @Override
        @Nullable
        public List<byte[]> mGet(byte[]... keys) {
            return delegate.mGet(keys);
        }

        @Override
        @Nullable
        public Boolean set(byte[] key, byte[] value, Expiration expiration, SetOption option) {
            return delegate.set(key, value, expiration, option);
        }

        @Override
        @Nullable
        public Boolean setNX(byte[] key, byte[] value) {
            return delegate.setNX(key, value);
        }

        @Override
        @Nullable
        public Boolean setEx(byte[] key, long seconds, byte[] value) {
            return delegate.setEx(key, seconds, value);
        }

        @Override
        @Nullable
        public Boolean pSetEx(byte[] key, long milliseconds, byte[] value) {
            return delegate.pSetEx(key, milliseconds, value);
        }

        @Override
        @Nullable
        public Boolean mSet(Map<byte[], byte[]> tuple) {
            return delegate.mSet(tuple);
        }

        @Override
        @Nullable
        public Boolean mSetNX(Map<byte[], byte[]> tuple) {
            return delegate.mSetNX(tuple);
        }

        @Override
        @Nullable
        public Long incr(byte[] key) {
            return delegate.incr(key);
        }

        @Override
        @Nullable
        public Long incrBy(byte[] key, long value) {
            return delegate.incrBy(key, value);
        }

        @Override
        @Nullable
        public Double incrBy(byte[] key, double value) {
            return delegate.incrBy(key, value);
        }

        @Override
        @Nullable
        public Long decr(byte[] key) {
            return delegate.decr(key);
        }

        @Override
        @Nullable
        public Long decrBy(byte[] key, long value) {
            return delegate.decrBy(key, value);
        }

        @Override
        @Nullable
        public Long append(byte[] key, byte[] value) {
            return delegate.append(key, value);
        }

        @Override
        @Nullable
        public byte[] getRange(byte[] key, long start, long end) {
            return delegate.getRange(key, start, end);
        }

        @Override
        public void setRange(byte[] key, byte[] value, long offset) {
            delegate.setRange(key, value, offset);
        }

        @Override
        @Nullable
        public Boolean getBit(byte[] key, long offset) {
            return delegate.getBit(key, offset);
        }

        @Override
        @Nullable
        public Boolean setBit(byte[] key, long offset, boolean value) {
            return delegate.setBit(key, offset, value);
        }

        @Override
        @Nullable
        public Long bitCount(byte[] key) {
            return delegate.bitCount(key);
        }

        @Override
        @Nullable
        public Long bitCount(byte[] key, long start, long end) {
            return delegate.bitCount(key, start, end);
        }

        @Override
        @Nullable
        public List<Long> bitField(byte[] key, BitFieldSubCommands subCommands) {
            return delegate.bitField(key, subCommands);
        }

        @Override
        @Nullable
        public Long bitOp(BitOperation op, byte[] destination, byte[]... keys) {
            return delegate.bitOp(op, destination, keys);
        }

        @Override
        @Nullable
        public Long bitPos(byte[] key, boolean bit) {
            return delegate.bitPos(key, bit);
        }

        @Override
        @Nullable
        public Long bitPos(byte[] key, boolean bit, org.springframework.data.domain.Range<Long> range) {
            return delegate.bitPos(key, bit, range);
        }

        @Override
        @Nullable
        public Long strLen(byte[] key) {
            return delegate.strLen(key);
        }

        @Override
        @Nullable
        public Long rPush(byte[] key, byte[]... values) {
            return delegate.rPush(key, values);
        }

        @Override
        @Nullable
        public Long lPos(byte[] key, byte[] element) {
            return delegate.lPos(key, element);
        }

        @Override
        @Nullable
        public List<Long> lPos(byte[] key, byte[] element, Integer rank, Integer count) {
            return delegate.lPos(key, element, rank, count);
        }

        @Override
        @Nullable
        public Long lPush(byte[] key, byte[]... values) {
            return delegate.lPush(key, values);
        }

        @Override
        @Nullable
        public Long rPushX(byte[] key, byte[] value) {
            return delegate.rPushX(key, value);
        }

        @Override
        @Nullable
        public Long lPushX(byte[] key, byte[] value) {
            return delegate.lPushX(key, value);
        }

        @Override
        @Nullable
        public Long lLen(byte[] key) {
            return delegate.lLen(key);
        }

        @Override
        @Nullable
        public List<byte[]> lRange(byte[] key, long start, long end) {
            return delegate.lRange(key, start, end);
        }

        @Override
        public void lTrim(byte[] key, long start, long end) {
            delegate.lTrim(key, start, end);
        }

        @Override
        @Nullable
        public byte[] lIndex(byte[] key, long index) {
            return delegate.lIndex(key, index);
        }

        @Override
        @Nullable
        public Long lInsert(byte[] key, Position where, byte[] pivot, byte[] value) {
            return delegate.lInsert(key, where, pivot, value);
        }

        @Override
        @Nullable
        public byte[] lMove(byte[] sourceKey, byte[] destinationKey, Direction from, Direction to) {
            return delegate.lMove(sourceKey, destinationKey, from, to);
        }

        @Override
        @Nullable
        public byte[] bLMove(byte[] sourceKey, byte[] destinationKey, Direction from, Direction to, double timeout) {
            return delegate.bLMove(sourceKey, destinationKey, from, to, timeout);
        }

        @Override
        public void lSet(byte[] key, long index, byte[] value) {
            delegate.lSet(key, index, value);
        }

        @Override
        @Nullable
        public Long lRem(byte[] key, long count, byte[] value) {
            return delegate.lRem(key, count, value);
        }

        @Override
        @Nullable
        public byte[] lPop(byte[] key) {
            return delegate.lPop(key);
        }

        @Override
        @Nullable
        public List<byte[]> lPop(byte[] key, long count) {
            return delegate.lPop(key, count);
        }

        @Override
        @Nullable
        public byte[] rPop(byte[] key) {
            return delegate.rPop(key);
        }

        @Override
        @Nullable
        public List<byte[]> rPop(byte[] key, long count) {
            return delegate.rPop(key, count);
        }

        @Override
        @Nullable
        public List<byte[]> bLPop(int timeout, byte[]... keys) {
            return delegate.bLPop(timeout, keys);
        }

        @Override
        @Nullable
        public List<byte[]> bRPop(int timeout, byte[]... keys) {
            return delegate.bRPop(timeout, keys);
        }

        @Override
        @Nullable
        public byte[] rPopLPush(byte[] srcKey, byte[] dstKey) {
            return delegate.rPopLPush(srcKey, dstKey);
        }

        @Override
        @Nullable
        public byte[] bRPopLPush(int timeout, byte[] srcKey, byte[] dstKey) {
            return delegate.bRPopLPush(timeout, srcKey, dstKey);
        }

        @Override
        @Nullable
        public Long sAdd(byte[] key, byte[]... values) {
            return delegate.sAdd(key, values);
        }

        @Override
        @Nullable
        public Long sRem(byte[] key, byte[]... values) {
            return delegate.sRem(key, values);
        }

        @Override
        @Nullable
        public byte[] sPop(byte[] key) {
            return delegate.sPop(key);
        }

        @Override
        @Nullable
        public List<byte[]> sPop(byte[] key, long count) {
            return delegate.sPop(key, count);
        }

        @Override
        @Nullable
        public Boolean sMove(byte[] srcKey, byte[] destKey, byte[] value) {
            return delegate.sMove(srcKey, destKey, value);
        }

        @Override
        @Nullable
        public Long sCard(byte[] key) {
            return delegate.sCard(key);
        }

        @Override
        @Nullable
        public Boolean sIsMember(byte[] key, byte[] value) {
            return delegate.sIsMember(key, value);
        }

        @Override
        @Nullable
        public List<Boolean> sMIsMember(byte[] key, byte[]... values) {
            return delegate.sMIsMember(key, values);
        }

        @Override
        @Nullable
        public Set<byte[]> sDiff(byte[]... keys) {
            return delegate.sDiff(keys);
        }

        @Override
        @Nullable
        public Long sDiffStore(byte[] destKey, byte[]... keys) {
            return delegate.sDiffStore(destKey, keys);
        }

        @Override
        @Nullable
        public Set<byte[]> sInter(byte[]... keys) {
            return delegate.sInter(keys);
        }

        @Override
        @Nullable
        public Long sInterStore(byte[] destKey, byte[]... keys) {
            return delegate.sInterStore(destKey, keys);
        }

        @Override
        @Nullable
        public Set<byte[]> sUnion(byte[]... keys) {
            return delegate.sUnion(keys);
        }

        @Override
        @Nullable
        public Long sUnionStore(byte[] destKey, byte[]... keys) {
            return delegate.sUnionStore(destKey, keys);
        }

        @Override
        @Nullable
        public Set<byte[]> sMembers(byte[] key) {
            return delegate.sMembers(key);
        }

        @Override
        @Nullable
        public byte[] sRandMember(byte[] key) {
            return delegate.sRandMember(key);
        }

        @Override
        @Nullable
        public List<byte[]> sRandMember(byte[] key, long count) {
            return delegate.sRandMember(key, count);
        }

        @Override
        public Cursor<byte[]> sScan(byte[] key, ScanOptions options) {
            return delegate.sScan(key, options);
        }

        @Override
        @Nullable
        public Boolean zAdd(byte[] key, double score, byte[] value) {
            return delegate.zAdd(key, score, value);
        }

        @Override
        @Nullable
        public Boolean zAdd(byte[] key, double score, byte[] value, ZAddArgs args) {
            return delegate.zAdd(key, score, value, args);
        }

        @Override
        @Nullable
        public Long zAdd(byte[] key, Set<Tuple> tuples) {
            return delegate.zAdd(key, tuples);
        }

        @Override
        public Long zAdd(byte[] key, Set<Tuple> tuples, ZAddArgs args) {
            return delegate.zAdd(key, tuples, args);
        }

        @Override
        @Nullable
        public Long zRem(byte[] key, byte[]... values) {
            return delegate.zRem(key, values);
        }

        @Override
        @Nullable
        public Double zIncrBy(byte[] key, double increment, byte[] value) {
            return delegate.zIncrBy(key, increment, value);
        }

        @Override
        @Nullable
        public byte[] zRandMember(byte[] key) {
            return delegate.zRandMember(key);
        }

        @Override
        @Nullable
        public List<byte[]> zRandMember(byte[] key, long count) {
            return delegate.zRandMember(key, count);
        }

        @Override
        @Nullable
        public Tuple zRandMemberWithScore(byte[] key) {
            return delegate.zRandMemberWithScore(key);
        }

        @Override
        @Nullable
        public List<Tuple> zRandMemberWithScore(byte[] key, long count) {
            return delegate.zRandMemberWithScore(key, count);
        }

        @Override
        @Nullable
        public Long zRank(byte[] key, byte[] value) {
            return delegate.zRank(key, value);
        }

        @Override
        @Nullable
        public Long zRevRank(byte[] key, byte[] value) {
            return delegate.zRevRank(key, value);
        }

        @Override
        @Nullable
        public Set<byte[]> zRange(byte[] key, long start, long end) {
            return delegate.zRange(key, start, end);
        }

        @Override
        @Nullable
        public Set<Tuple> zRangeWithScores(byte[] key, long start, long end) {
            return delegate.zRangeWithScores(key, start, end);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByScore(byte[] key, double min, double max) {
            return delegate.zRangeByScore(key, min, max);
        }

        @Override
        @Nullable
        public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range) {
            return delegate.zRangeByScoreWithScores(key, range);
        }

        @Override
        @Nullable
        public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max) {
            return delegate.zRangeByScoreWithScores(key, min, max);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByScore(byte[] key, double min, double max, long offset, long count) {
            return delegate.zRangeByScore(key, min, max, offset, count);
        }

        @Override
        @Nullable
        public Set<Tuple> zRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
            return delegate.zRangeByScoreWithScores(key, min, max, offset, count);
        }

        @Override
        @Nullable
        public Set<Tuple> zRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
            return delegate.zRangeByScoreWithScores(key, range, limit);
        }

        @Override
        @Nullable
        public Set<byte[]> zRevRange(byte[] key, long start, long end) {
            return delegate.zRevRange(key, start, end);
        }

        @Override
        @Nullable
        public Set<Tuple> zRevRangeWithScores(byte[] key, long start, long end) {
            return delegate.zRevRangeWithScores(key, start, end);
        }

        @Override
        @Nullable
        public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max) {
            return delegate.zRevRangeByScore(key, min, max);
        }

        @Override
        @Nullable
        public Set<byte[]> zRevRangeByScore(byte[] key, Range range) {
            return delegate.zRevRangeByScore(key, range);
        }

        @Override
        @Nullable
        public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max) {
            return delegate.zRevRangeByScoreWithScores(key, min, max);
        }

        @Override
        @Nullable
        public Set<byte[]> zRevRangeByScore(byte[] key, double min, double max, long offset, long count) {
            return delegate.zRevRangeByScore(key, min, max, offset, count);
        }

        @Override
        @Nullable
        public Set<byte[]> zRevRangeByScore(byte[] key, Range range, Limit limit) {
            return delegate.zRevRangeByScore(key, range, limit);
        }

        @Override
        @Nullable
        public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, double min, double max, long offset, long count) {
            return delegate.zRevRangeByScoreWithScores(key, min, max, offset, count);
        }

        @Override
        @Nullable
        public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range) {
            return delegate.zRevRangeByScoreWithScores(key, range);
        }

        @Override
        @Nullable
        public Set<Tuple> zRevRangeByScoreWithScores(byte[] key, Range range, Limit limit) {
            return delegate.zRevRangeByScoreWithScores(key, range, limit);
        }

        @Override
        @Nullable
        public Long zCount(byte[] key, double min, double max) {
            return delegate.zCount(key, min, max);
        }

        @Override
        @Nullable
        public Long zCount(byte[] key, Range range) {
            return delegate.zCount(key, range);
        }

        @Override
        @Nullable
        public Long zLexCount(byte[] key, Range range) {
            return delegate.zLexCount(key, range);
        }

        @Override
        @Nullable
        public Tuple zPopMin(byte[] key) {
            return delegate.zPopMin(key);
        }

        @Override
        @Nullable
        public Set<Tuple> zPopMin(byte[] key, long count) {
            return delegate.zPopMin(key, count);
        }

        @Override
        @Nullable
        public Tuple bZPopMin(byte[] key, long timeout, TimeUnit unit) {
            return delegate.bZPopMin(key, timeout, unit);
        }

        @Override
        @Nullable
        public Tuple zPopMax(byte[] key) {
            return delegate.zPopMax(key);
        }

        @Override
        @Nullable
        public Set<Tuple> zPopMax(byte[] key, long count) {
            return delegate.zPopMax(key, count);
        }

        @Override
        @Nullable
        public Tuple bZPopMax(byte[] key, long timeout, TimeUnit unit) {
            return delegate.bZPopMax(key, timeout, unit);
        }

        @Override
        @Nullable
        public Long zCard(byte[] key) {
            return delegate.zCard(key);
        }

        @Override
        @Nullable
        public Double zScore(byte[] key, byte[] value) {
            return delegate.zScore(key, value);
        }

        @Override
        @Nullable
        public List<Double> zMScore(byte[] key, byte[]... values) {
            return delegate.zMScore(key, values);
        }

        @Override
        @Nullable
        public Long zRemRange(byte[] key, long start, long end) {
            return delegate.zRemRange(key, start, end);
        }

        @Override
        public Long zRemRangeByLex(byte[] key, Range range) {
            return delegate.zRemRangeByLex(key, range);
        }

        @Override
        @Nullable
        public Long zRemRangeByScore(byte[] key, double min, double max) {
            return delegate.zRemRangeByScore(key, min, max);
        }

        @Override
        @Nullable
        public Long zRemRangeByScore(byte[] key, Range range) {
            return delegate.zRemRangeByScore(key, range);
        }

        @Override
        @Nullable
        public Set<byte[]> zDiff(byte[]... sets) {
            return delegate.zDiff(sets);
        }

        @Override
        @Nullable
        public Set<Tuple> zDiffWithScores(byte[]... sets) {
            return delegate.zDiffWithScores(sets);
        }

        @Override
        @Nullable
        public Long zDiffStore(byte[] destKey, byte[]... sets) {
            return delegate.zDiffStore(destKey, sets);
        }

        @Override
        @Nullable
        public Set<byte[]> zInter(byte[]... sets) {
            return delegate.zInter(sets);
        }

        @Override
        @Nullable
        public Set<Tuple> zInterWithScores(byte[]... sets) {
            return delegate.zInterWithScores(sets);
        }

        @Override
        @Nullable
        public Set<Tuple> zInterWithScores(Aggregate aggregate, int[] weights, byte[]... sets) {
            return delegate.zInterWithScores(aggregate, weights, sets);
        }

        @Override
        @Nullable
        public Set<Tuple> zInterWithScores(Aggregate aggregate, Weights weights, byte[]... sets) {
            return delegate.zInterWithScores(aggregate, weights, sets);
        }

        @Override
        @Nullable
        public Long zInterStore(byte[] destKey, byte[]... sets) {
            return delegate.zInterStore(destKey, sets);
        }

        @Override
        @Nullable
        public Long zInterStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
            return delegate.zInterStore(destKey, aggregate, weights, sets);
        }

        @Override
        @Nullable
        public Long zInterStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
            return delegate.zInterStore(destKey, aggregate, weights, sets);
        }

        @Override
        @Nullable
        public Set<byte[]> zUnion(byte[]... sets) {
            return delegate.zUnion(sets);
        }

        @Override
        @Nullable
        public Set<Tuple> zUnionWithScores(byte[]... sets) {
            return delegate.zUnionWithScores(sets);
        }

        @Override
        @Nullable
        public Set<Tuple> zUnionWithScores(Aggregate aggregate, int[] weights, byte[]... sets) {
            return delegate.zUnionWithScores(aggregate, weights, sets);
        }

        @Override
        @Nullable
        public Set<Tuple> zUnionWithScores(Aggregate aggregate, Weights weights, byte[]... sets) {
            return delegate.zUnionWithScores(aggregate, weights, sets);
        }

        @Override
        @Nullable
        public Long zUnionStore(byte[] destKey, byte[]... sets) {
            return delegate.zUnionStore(destKey, sets);
        }

        @Override
        @Nullable
        public Long zUnionStore(byte[] destKey, Aggregate aggregate, int[] weights, byte[]... sets) {
            return delegate.zUnionStore(destKey, aggregate, weights, sets);
        }

        @Override
        @Nullable
        public Long zUnionStore(byte[] destKey, Aggregate aggregate, Weights weights, byte[]... sets) {
            return delegate.zUnionStore(destKey, aggregate, weights, sets);
        }

        @Override
        public Cursor<Tuple> zScan(byte[] key, ScanOptions options) {
            return delegate.zScan(key, options);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByScore(byte[] key, String min, String max) {
            return delegate.zRangeByScore(key, min, max);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByScore(byte[] key, Range range) {
            return delegate.zRangeByScore(key, range);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByScore(byte[] key, String min, String max, long offset, long count) {
            return delegate.zRangeByScore(key, min, max, offset, count);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByScore(byte[] key, Range range, Limit limit) {
            return delegate.zRangeByScore(key, range, limit);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByLex(byte[] key) {
            return delegate.zRangeByLex(key);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByLex(byte[] key, Range range) {
            return delegate.zRangeByLex(key, range);
        }

        @Override
        @Nullable
        public Set<byte[]> zRangeByLex(byte[] key, Range range, Limit limit) {
            return delegate.zRangeByLex(key, range, limit);
        }

        @Override
        @Nullable
        public Set<byte[]> zRevRangeByLex(byte[] key) {
            return delegate.zRevRangeByLex(key);
        }

        @Override
        @Nullable
        public Set<byte[]> zRevRangeByLex(byte[] key, Range range) {
            return delegate.zRevRangeByLex(key, range);
        }

        @Override
        @Nullable
        public Set<byte[]> zRevRangeByLex(byte[] key, Range range, Limit limit) {
            return delegate.zRevRangeByLex(key, range, limit);
        }

        @Override
        @Nullable
        public Boolean hSet(byte[] key, byte[] field, byte[] value) {
            return delegate.hSet(key, field, value);
        }

        @Override
        @Nullable
        public Boolean hSetNX(byte[] key, byte[] field, byte[] value) {
            return delegate.hSetNX(key, field, value);
        }

        @Override
        @Nullable
        public byte[] hGet(byte[] key, byte[] field) {
            return delegate.hGet(key, field);
        }

        @Override
        @Nullable
        public List<byte[]> hMGet(byte[] key, byte[]... fields) {
            return delegate.hMGet(key, fields);
        }

        @Override
        public void hMSet(byte[] key, Map<byte[], byte[]> hashes) {
            delegate.hMSet(key, hashes);
        }

        @Override
        @Nullable
        public Long hIncrBy(byte[] key, byte[] field, long delta) {
            return delegate.hIncrBy(key, field, delta);
        }

        @Override
        @Nullable
        public Double hIncrBy(byte[] key, byte[] field, double delta) {
            return delegate.hIncrBy(key, field, delta);
        }

        @Override
        @Nullable
        public Boolean hExists(byte[] key, byte[] field) {
            return delegate.hExists(key, field);
        }

        @Override
        @Nullable
        public Long hDel(byte[] key, byte[]... fields) {
            return delegate.hDel(key, fields);
        }

        @Override
        @Nullable
        public Long hLen(byte[] key) {
            return delegate.hLen(key);
        }

        @Override
        @Nullable
        public Set<byte[]> hKeys(byte[] key) {
            return delegate.hKeys(key);
        }

        @Override
        @Nullable
        public List<byte[]> hVals(byte[] key) {
            return delegate.hVals(key);
        }

        @Override
        @Nullable
        public Map<byte[], byte[]> hGetAll(byte[] key) {
            return delegate.hGetAll(key);
        }

        @Override
        @Nullable
        public byte[] hRandField(byte[] key) {
            return delegate.hRandField(key);
        }

        @Override
        @Nullable
        public Map.Entry<byte[], byte[]> hRandFieldWithValues(byte[] key) {
            return delegate.hRandFieldWithValues(key);
        }

        @Override
        @Nullable
        public List<byte[]> hRandField(byte[] key, long count) {
            return delegate.hRandField(key, count);
        }

        @Override
        @Nullable
        public List<Map.Entry<byte[], byte[]>> hRandFieldWithValues(byte[] key, long count) {
            return delegate.hRandFieldWithValues(key, count);
        }

        @Override
        public Cursor<Map.Entry<byte[], byte[]>> hScan(byte[] key, ScanOptions options) {
            return delegate.hScan(key, options);
        }

        @Override
        @Nullable
        public Long hStrLen(byte[] key, byte[] field) {
            return delegate.hStrLen(key, field);
        }

        @Override
        public void multi() {
            delegate.multi();
        }

        @Override
        public List<Object> exec() {
            return delegate.exec();
        }

        @Override
        public void discard() {
            delegate.discard();
        }

        @Override
        public void watch(byte[]... keys) {
            delegate.watch(keys);
        }

        @Override
        public void unwatch() {
            delegate.unwatch();
        }

        @Override
        public boolean isSubscribed() {
            return delegate.isSubscribed();
        }

        @Override
        @Nullable
        public Subscription getSubscription() {
            return delegate.getSubscription();
        }

        @Override
        @Nullable
        public Long publish(byte[] channel, byte[] message) {
            return delegate.publish(channel, message);
        }

        @Override
        public void subscribe(MessageListener listener, byte[]... channels) {
            delegate.subscribe(listener, channels);
        }

        @Override
        public void pSubscribe(MessageListener listener, byte[]... patterns) {
            delegate.pSubscribe(listener, patterns);
        }

        @Override
        public void select(int dbIndex) {
            delegate.select(dbIndex);
        }

        @Override
        @Nullable
        public byte[] echo(byte[] message) {
            return delegate.echo(message);
        }

        @Override
        @Nullable
        public String ping() {
            return delegate.ping();
        }

        @Override
        @Deprecated
        public void bgWriteAof() {
            delegate.bgWriteAof();
        }

        @Override
        public void bgReWriteAof() {
            delegate.bgReWriteAof();
        }

        @Override
        public void bgSave() {
            delegate.bgSave();
        }

        @Override
        @Nullable
        public Long lastSave() {
            return delegate.lastSave();
        }

        @Override
        public void save() {
            delegate.save();
        }

        @Override
        @Nullable
        public Long dbSize() {
            return delegate.dbSize();
        }

        @Override
        public void flushDb() {
            delegate.flushDb();
        }

        @Override
        public void flushDb(FlushOption option) {
            delegate.flushDb(option);
        }

        @Override
        public void flushAll() {
            delegate.flushAll();
        }

        @Override
        public void flushAll(FlushOption option) {
            delegate.flushAll(option);
        }

        @Override
        @Nullable
        public Properties info() {
            return delegate.info();
        }

        @Override
        @Nullable
        public Properties info(String section) {
            return delegate.info(section);
        }

        @Override
        public void shutdown() {
            delegate.shutdown();
        }

        @Override
        public void shutdown(ShutdownOption option) {
            delegate.shutdown(option);
        }

        @Override
        @Nullable
        public Properties getConfig(String pattern) {
            return delegate.getConfig(pattern);
        }

        @Override
        public void setConfig(String param, String value) {
            delegate.setConfig(param, value);
        }

        @Override
        public void resetConfigStats() {
            delegate.resetConfigStats();
        }

        @Override
        public void rewriteConfig() {
            delegate.rewriteConfig();
        }

        @Override
        @Nullable
        public Long time() {
            return delegate.time();
        }

        @Override
        @Nullable
        public Long time(TimeUnit timeUnit) {
            return delegate.time(timeUnit);
        }

        @Override
        public void killClient(String host, int port) {
            delegate.killClient(host, port);
        }

        @Override
        public void setClientName(byte[] name) {
            delegate.setClientName(name);
        }

        @Override
        @Nullable
        public String getClientName() {
            return delegate.getClientName();
        }

        @Override
        @Nullable
        public List<RedisClientInfo> getClientList() {
            return delegate.getClientList();
        }

        @Override
        public void slaveOf(String host, int port) {
            delegate.slaveOf(host, port);
        }

        @Override
        public void slaveOfNoOne() {
            delegate.slaveOfNoOne();
        }

        @Override
        public void migrate(byte[] key, RedisNode target, int dbIndex, MigrateOption option) {
            delegate.migrate(key, target, dbIndex, option);
        }

        @Override
        public void migrate(byte[] key, RedisNode target, int dbIndex, MigrateOption option, long timeout) {
            delegate.migrate(key, target, dbIndex, option, timeout);
        }

        @Override
        @Nullable
        public Long xAck(byte[] key, String group, String... recordIds) {
            return delegate.xAck(key, group, recordIds);
        }

        @Override
        @Nullable
        public Long xAck(byte[] key, String group, RecordId... recordIds) {
            return delegate.xAck(key, group, recordIds);
        }

        @Override
        @Nullable
        public RecordId xAdd(byte[] key, Map<byte[], byte[]> content) {
            return delegate.xAdd(key, content);
        }

        @Override
        @Nullable
        public RecordId xAdd(MapRecord<byte[], byte[], byte[]> record) {
            return delegate.xAdd(record);
        }

        @Override
        @Nullable
        public RecordId xAdd(MapRecord<byte[], byte[], byte[]> record, XAddOptions options) {
            return delegate.xAdd(record, options);
        }

        @Override
        @Nullable
        public List<RecordId> xClaimJustId(byte[] key, String group, String newOwner, XClaimOptions options) {
            return delegate.xClaimJustId(key, group, newOwner, options);
        }

        @Override
        @Nullable
        public List<ByteRecord> xClaim(byte[] key, String group, String newOwner, Duration minIdleTime, RecordId... recordIds) {
            return delegate.xClaim(key, group, newOwner, minIdleTime, recordIds);
        }

        @Override
        @Nullable
        public List<ByteRecord> xClaim(byte[] key, String group, String newOwner, XClaimOptions options) {
            return delegate.xClaim(key, group, newOwner, options);
        }

        @Override
        @Nullable
        public Long xDel(byte[] key, String... recordIds) {
            return delegate.xDel(key, recordIds);
        }

        @Override
        @Nullable
        public Long xDel(byte[] key, RecordId... recordIds) {
            return delegate.xDel(key, recordIds);
        }

        @Override
        @Nullable
        public String xGroupCreate(byte[] key, String groupName, ReadOffset readOffset) {
            return delegate.xGroupCreate(key, groupName, readOffset);
        }

        @Override
        @Nullable
        public String xGroupCreate(byte[] key, String groupName, ReadOffset readOffset, boolean mkStream) {
            return delegate.xGroupCreate(key, groupName, readOffset, mkStream);
        }

        @Override
        @Nullable
        public Boolean xGroupDelConsumer(byte[] key, String groupName, String consumerName) {
            return delegate.xGroupDelConsumer(key, groupName, consumerName);
        }

        @Override
        @Nullable
        public Boolean xGroupDelConsumer(byte[] key, Consumer consumer) {
            return delegate.xGroupDelConsumer(key, consumer);
        }

        @Override
        @Nullable
        public Boolean xGroupDestroy(byte[] key, String groupName) {
            return delegate.xGroupDestroy(key, groupName);
        }

        @Override
        @Nullable
        public StreamInfo.XInfoStream xInfo(byte[] key) {
            return delegate.xInfo(key);
        }

        @Override
        @Nullable
        public StreamInfo.XInfoGroups xInfoGroups(byte[] key) {
            return delegate.xInfoGroups(key);
        }

        @Override
        @Nullable
        public StreamInfo.XInfoConsumers xInfoConsumers(byte[] key, String groupName) {
            return delegate.xInfoConsumers(key, groupName);
        }

        @Override
        @Nullable
        public Long xLen(byte[] key) {
            return delegate.xLen(key);
        }

        @Override
        @Nullable
        public PendingMessagesSummary xPending(byte[] key, String groupName) {
            return delegate.xPending(key, groupName);
        }

        @Override
        @Nullable
        public PendingMessages xPending(byte[] key, Consumer consumer) {
            return delegate.xPending(key, consumer);
        }

        @Override
        @Nullable
        public PendingMessages xPending(byte[] key, String groupName, String consumerName) {
            return delegate.xPending(key, groupName, consumerName);
        }

        @Override
        @Nullable
        public PendingMessages xPending(byte[] key, String groupName, org.springframework.data.domain.Range<?> range, Long count) {
            return delegate.xPending(key, groupName, range, count);
        }

        @Override
        @Nullable
        public PendingMessages xPending(byte[] key, Consumer consumer, org.springframework.data.domain.Range<?> range, Long count) {
            return delegate.xPending(key, consumer, range, count);
        }

        @Override
        @Nullable
        public PendingMessages xPending(byte[] key, String groupName, String consumerName, org.springframework.data.domain.Range<?> range, Long count) {
            return delegate.xPending(key, groupName, consumerName, range, count);
        }

        @Override
        @Nullable
        public PendingMessages xPending(byte[] key, String groupName, XPendingOptions options) {
            return delegate.xPending(key, groupName, options);
        }

        @Override
        @Nullable
        public List<ByteRecord> xRange(byte[] key, org.springframework.data.domain.Range<String> range) {
            return delegate.xRange(key, range);
        }

        @Override
        @Nullable
        public List<ByteRecord> xRange(byte[] key, org.springframework.data.domain.Range<String> range, Limit limit) {
            return delegate.xRange(key, range, limit);
        }

        @Override
        @Nullable
        public List<ByteRecord> xRead(StreamOffset<byte[]>... streams) {
            return delegate.xRead(streams);
        }

        @Override
        @Nullable
        public List<ByteRecord> xRead(StreamReadOptions readOptions, StreamOffset<byte[]>... streams) {
            return delegate.xRead(readOptions, streams);
        }

        @Override
        @Nullable
        public List<ByteRecord> xReadGroup(Consumer consumer, StreamOffset<byte[]>... streams) {
            return delegate.xReadGroup(consumer, streams);
        }

        @Override
        @Nullable
        public List<ByteRecord> xReadGroup(Consumer consumer, StreamReadOptions readOptions, StreamOffset<byte[]>... streams) {
            return delegate.xReadGroup(consumer, readOptions, streams);
        }

        @Override
        @Nullable
        public List<ByteRecord> xRevRange(byte[] key, org.springframework.data.domain.Range<String> range) {
            return delegate.xRevRange(key, range);
        }

        @Override
        @Nullable
        public List<ByteRecord> xRevRange(byte[] key, org.springframework.data.domain.Range<String> range, Limit limit) {
            return delegate.xRevRange(key, range, limit);
        }

        @Override
        @Nullable
        public Long xTrim(byte[] key, long count) {
            return delegate.xTrim(key, count);
        }

        @Override
        @Nullable
        public Long xTrim(byte[] key, long count, boolean approximateTrimming) {
            return delegate.xTrim(key, count, approximateTrimming);
        }

        @Override
        public void scriptFlush() {
            delegate.scriptFlush();
        }

        @Override
        public void scriptKill() {
            delegate.scriptKill();
        }

        @Override
        @Nullable
        public String scriptLoad(byte[] script) {
            return delegate.scriptLoad(script);
        }

        @Override
        @Nullable
        public List<Boolean> scriptExists(String... scriptShas) {
            return delegate.scriptExists(scriptShas);
        }

        @Override
        @Nullable
        public <T> T eval(byte[] script, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
            return delegate.eval(script, returnType, numKeys, keysAndArgs);
        }

        @Override
        @Nullable
        public <T> T evalSha(String scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
            return delegate.evalSha(scriptSha, returnType, numKeys, keysAndArgs);
        }

        @Override
        @Nullable
        public <T> T evalSha(byte[] scriptSha, ReturnType returnType, int numKeys, byte[]... keysAndArgs) {
            return delegate.evalSha(scriptSha, returnType, numKeys, keysAndArgs);
        }

        @Override
        @Nullable
        public Long geoAdd(byte[] key, Point point, byte[] member) {
            return delegate.geoAdd(key, point, member);
        }

        @Override
        @Nullable
        public Long geoAdd(byte[] key, GeoLocation<byte[]> location) {
            return delegate.geoAdd(key, location);
        }

        @Override
        @Nullable
        public Long geoAdd(byte[] key, Map<byte[], Point> memberCoordinateMap) {
            return delegate.geoAdd(key, memberCoordinateMap);
        }

        @Override
        @Nullable
        public Long geoAdd(byte[] key, Iterable<GeoLocation<byte[]>> locations) {
            return delegate.geoAdd(key, locations);
        }

        @Override
        @Nullable
        public Distance geoDist(byte[] key, byte[] member1, byte[] member2) {
            return delegate.geoDist(key, member1, member2);
        }

        @Override
        @Nullable
        public Distance geoDist(byte[] key, byte[] member1, byte[] member2, Metric metric) {
            return delegate.geoDist(key, member1, member2, metric);
        }

        @Override
        @Nullable
        public List<String> geoHash(byte[] key, byte[]... members) {
            return delegate.geoHash(key, members);
        }

        @Override
        @Nullable
        public List<Point> geoPos(byte[] key, byte[]... members) {
            return delegate.geoPos(key, members);
        }

        @Override
        @Nullable
        public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within) {
            return delegate.geoRadius(key, within);
        }

        @Override
        @Nullable
        public GeoResults<GeoLocation<byte[]>> geoRadius(byte[] key, Circle within, GeoRadiusCommandArgs args) {
            return delegate.geoRadius(key, within, args);
        }

        @Override
        @Nullable
        public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, double radius) {
            return delegate.geoRadiusByMember(key, member, radius);
        }

        @Override
        @Nullable
        public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius) {
            return delegate.geoRadiusByMember(key, member, radius);
        }

        @Override
        @Nullable
        public GeoResults<GeoLocation<byte[]>> geoRadiusByMember(byte[] key, byte[] member, Distance radius, GeoRadiusCommandArgs args) {
            return delegate.geoRadiusByMember(key, member, radius, args);
        }

        @Override
        @Nullable
        public Long geoRemove(byte[] key, byte[]... members) {
            return delegate.geoRemove(key, members);
        }

        @Override
        @Nullable
        public GeoResults<GeoLocation<byte[]>> geoSearch(byte[] key, GeoReference<byte[]> reference, GeoShape predicate, GeoSearchCommandArgs args) {
            return delegate.geoSearch(key, reference, predicate, args);
        }

        @Override
        @Nullable
        public Long geoSearchStore(byte[] destKey, byte[] key, GeoReference<byte[]> reference, GeoShape predicate, GeoSearchStoreCommandArgs args) {
            return delegate.geoSearchStore(destKey, key, reference, predicate, args);
        }

        @Override
        @Nullable
        public Long pfAdd(byte[] key, byte[]... values) {
            return delegate.pfAdd(key, values);
        }

        @Override
        @Nullable
        public Long pfCount(byte[]... keys) {
            return delegate.pfCount(keys);
        }

        @Override
        public void pfMerge(byte[] destinationKey, byte[]... sourceKeys) {
            delegate.pfMerge(destinationKey, sourceKeys);
        }

        @Override
        public RedisConnection getTargetConnection() {
            return delegate;
        }
    }


    static class JDKDynamicProxyRedisConnection implements InvocationHandler {

        private static final String CLOSE = "close";
        private static final String HASH_CODE = "hashCode";
        private static final String EQUALS = "equals";

        private final RedisConnection target;

        public JDKDynamicProxyRedisConnection(RedisConnection target) {
            this.target = target;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (method.getName().equals(EQUALS)) {
                // Only consider equal when proxies are identical.
                return (proxy == args[0]);
            } else if (method.getName().equals(HASH_CODE)) {
                // Use hashCode of PersistenceManager proxy.
                return System.identityHashCode(proxy);
            } else if (method.getName().equals(CLOSE)) {
                // Handle close method: suppress, not valid.
                target.close();
                return null;
            }

            // Invoke method on target RedisConnection.
            try {

                Object retVal = method.invoke(this.target, args);
                return retVal;
            } catch (InvocationTargetException ex) {
                throw ex.getTargetException();
            }
        }
    }

    static class SuccessRate {

        private AtomicInteger total = new AtomicInteger(0);
        private AtomicInteger fail = new AtomicInteger(0);

        public Double getSuccessRate() {
            int failValue = fail.get();
            int totalValue = total.get();
            if (totalValue > 0) {
                return (totalValue - failValue) * 1.0 / totalValue;
            } else {
                return 0D;
            }
        }

        public void totalIncrease() {
            total.incrementAndGet();
        }

        public void failIncrease() {
            fail.incrementAndGet();
        }
    }
}
