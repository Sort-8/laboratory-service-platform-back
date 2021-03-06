package com.miku.lab.util;

import redis.clients.jedis.*;

/**
 * redis工具类
 * @author：miku
 * @date：2021年6月9日
 * @version:1.0
 */
public class RedisUtil {
    private static JedisPool jedisPool = null;


    /**
     * 初始化
     */
    static {
        try {
            JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
            jedisPoolConfig.setMaxIdle(Constant.REDIS_MAX_IDLE);
            jedisPoolConfig.setMaxTotal(Constant.REDIS_MAX_ACTIVE);
            jedisPoolConfig.setMaxWaitMillis(Constant.REDIS_MAX_WAIT);
            jedisPoolConfig.setTestOnBorrow(Constant.TEST_ON_BORROW);
            //jedisPool = new JedisPool(jedisPoolConfig, Constant.REDIS_HOST, Constant.REDIS_PORT, Constant.REDIS_TIMEOUT);
            jedisPool = getPool(jedisPoolConfig, Constant.REDIS_HOST, Constant.REDIS_PORT);
        } catch (Exception e) {
            System.out.println("初始化redis失败");
            e.printStackTrace();
        }
    }

    public static JedisPool getPool(JedisPoolConfig config,String host,int port) {
        if (jedisPool == null) {
            jedisPool = new JedisPool(config, host, port);
        }
        return jedisPool;
    }
    //获取连接池对象
    public synchronized  Jedis getJedis() {
        Jedis jedis = null;
        try {
            jedis = jedisPool.getResource();
            return jedis;
        } catch (Exception e) {
        } finally {
            //注意这里不是关闭连接，在JedisPool模式下，Jedis会被归还给资源池。
            if (jedis != null)
                jedis.close();
        }
        return null;
    }
//         try {
//             if (jedisPool != null) {
//                 Jedis resource = jedisPool.getResource();
//                 return resource;
//             } else {
//                 return null;
//             }
//         } catch (Exception e) {
//             e.printStackTrace();
//             return null;
//         }
    /**
     * 释放资源
     * @param jedis
     */
    public synchronized void returnResource(final Jedis jedis) {
        if(jedis !=null) {
            jedisPool.returnResource(jedis);
        }
    }
    /**
     *
     * @param key
     * @param value
     * @return
     */
    public int setString(String key, String value) {
        if(key==null)
            return 0;
        Jedis jedis = getJedis();
        jedis.set(key,value);
        jedis.expire(key,Constant.REDIS_EXPIRE_TIME);
        return 1;
    }
    /**
     *
     * @param key
     * @param redisExpireTime
     * @return
     */
    public int flushExpire(String key, int redisExpireTime) {
        if(key==null)
            return 0;
        Jedis jedis = getJedis();
        jedis.expire(key,Constant.REDIS_EXPIRE_TIME);
        return 1;
    }

    /**
     *
     * @param key
     * @return
     */
    public String getString(String key) {
        if(key==null)
            return null;
        try {
            Jedis jedis = getJedis();
            String value = jedis.get(key);
            return value;
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    /**
     *
     * @param key
     * @return
     */
    public int delString(String key) {
        if(key==null)
            return 0;
        Jedis jedis = getJedis();
        jedis.del(key);
        return 1;
    }


}
