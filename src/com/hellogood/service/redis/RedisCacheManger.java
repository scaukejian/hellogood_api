package com.hellogood.service.redis;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import com.hellogood.exception.BusinessException;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

/**
 * Created by yanyuan on 2015/8/12.
 */
@Service
public class RedisCacheManger{

    //默认有效时间, 以秒为单位
    public static int REDIS_CACHE_EXPIRE_DEFAULT = 8 * 60 * 60;
    
    public static int REDIS_CACHE_HOUR_EXPIRE_DEFAULT = 60 * 60;
    
    public static int REDIS_CACHE_DAY_EXPIRE_DEFAULT = 24 * 60 * 60;
    
    public static int REDIS_CACHE_EXPIRE_WEEK = 7 * 24 * 60 * 60;
    
    public static int REDIS_CACHE_EXPIRE_RANKING = 30 * 60;


    private Logger logger = Logger.getLogger(RedisCacheManger.class);

    @Autowired
    private JedisPool pool ;
    
    private Gson gson = new Gson();

    public <T> T getRedisCacheInfo(String key) {
        logger.debug("get from redisCache : " + key);
        Jedis jedis = pool.getResource();
        try {
            return (T)jedis.get(key);
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            jedis.close();

        }
        return null;
    }

    public <T> boolean setRedisCacheInfo(String key, T value) {
        Jedis jedis = pool.getResource();
        try {
            logger.debug("add to redisCache : " + key);
            jedis.set(key, (String) value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    public <T> boolean delRedisCacheInfo(String key) {
        Jedis jedis = pool.getResource();
        try {
            logger.debug("del to redisCache : " + key);
            jedis.del(key);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    /**
     *
     * @param key
     * @param seconds
     * @param value
     * @param <T>
     * @return
     */
    public <T> boolean setRedisCacheInfo(String key, int seconds, T value) {
        Jedis jedis = pool.getResource();
        try {
            logger.debug("add to redisCache : " + key + "; seconds : " + seconds);
            jedis.setex(key, seconds, (String) value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void publish(String channel, String msg){
    	
    	Jedis jedis = pool.getResource();
        logger.debug("channel : " + channel);
        logger.debug("msg : " + msg);
        if(StringUtils.isBlank(channel)){
            throw new BusinessException("发布失败，渠道不能为空");
        }
        if(StringUtils.isBlank(msg)){
            throw new BusinessException("发布失败，消息不能为空");
        }
        try {
            jedis.publish(channel, msg);
        } finally {
            jedis.close();
        }
    }

    public Boolean exists(String key){
        Boolean flag = false;
        logger.debug("exists key " + key);
        Jedis jedis = pool.getResource();
        try {
            flag = jedis.exists(key);
        } finally {
            jedis.close();
        }
        return flag;
    }


    /**
     * 缓存map对象
     * @param key
     * @param map
     * @param <T>
     * @return
     */
    public <T> boolean addMap(String key, Map map) {
        return addMap(key, map, REDIS_CACHE_EXPIRE_WEEK);
    }

    /**
     * 缓存map
     * @param key
     * @param map
     * @param expireSecond
     * @param <T>
     * @return
     */
    public <T> boolean addMap(String key, Map map, int expireSecond){
        Jedis jedis = pool.getResource();
        try {
            logger.debug("addMap : " + key + "; map : " + map);
            jedis.hmset(key, map);
            jedis.expire(key, expireSecond);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }
    
    /**
     * 加入set
     * @param key
     * @param map
     * @param <T>
     * @return
     */
    public  Long addSet(String key, String... value) {
        Jedis jedis = pool.getResource();
        try {
        	   return jedis.sadd(key, value);
        	
        } catch (Exception e) {
            e.printStackTrace();
            return 0L;
        } finally {
            jedis.close();
        }
    }
    /**
     * 加入set
     * @param key
     * @param map
     * @param <T>
     * @return
     */
    public boolean isInSet(String key, String value) {
    	Jedis jedis = pool.getResource();
    	try {
    		logger.debug("isInSet : " + key + "; value : " + value);
    		  // 判断value是否在列表中  
            return jedis.sismember(key, value); 
    	} catch (Exception e) {
    		e.printStackTrace();
    		return true;
    	} finally {
    		jedis.close();
    	}
    }
    /**
     * 获取set
     * @param key
     * @param map
     * @param <T>
     * @return
     */
    public Set<String> smembers(String key) {
    	Jedis jedis = pool.getResource();
    	try {
    		logger.debug("smembers : " + key );
    		// 判断value是否在列表中  
    		return jedis.smembers(key); 
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	} finally {
    		jedis.close();
    	}
    }
    /**
     * 获取set
     * @param key
     * @param map
     * @param <T>
     * @return
     */
    public Set<String> sinter(String... keys) {
    	Jedis jedis = pool.getResource();
    	try {
    		logger.debug("smembers : " + keys );
    		// 判断value是否在列表中  
    		return jedis.sinter(keys);
    	} catch (Exception e) {
    		e.printStackTrace();
    		return null;
    	} finally {
    		jedis.close();
    	}
    }

    /**
     * 缓存Map添加数据
     * @param key
     * @param filed
     * @param <T>
     * @return
     */
    public <T> boolean mapSet(String key, String filed, T value) {
        return mapSet(key, filed, value, REDIS_CACHE_EXPIRE_WEEK);
    }

    /**
     * 缓存Map添加数据
     * @param key
     * @param filed
     * @param <T>
     * @return
     */
    public <T> boolean mapSet(String key, String filed, T value, int expireSecond) {
        Jedis jedis = pool.getResource();
        try {
            logger.debug("mapSet : " + key + "; filed : " + filed + "value : " + value);
            //有操作就更新过期时间
            jedis.hset(key, filed, (String) value);
            jedis.expire(key, expireSecond);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return false;
    }

    public long getTTL(String key){
    	Jedis jedis = pool.getResource();
    	return jedis.ttl(key);
    }

    /**
     *
     * @param key
     * @return List
     */
    public <T> T getMap(String key) {
        Jedis jedis = pool.getResource();
        try {
            logger.debug("getMap : " + key);
            if(exists(key)){
                return (T)jedis.hgetAll(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     *
     * @param key
     * @return List
     */
    public  <T> T getMapValue(String key, String field) {
        Jedis jedis = pool.getResource();
        try {
            logger.debug("getMapValue : " + key + "; field : " + field);
            if(exists(key)){
                logger.debug("value --》 " + jedis.hget(key, field));
                return (T)jedis.hget(key, field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 移除缓存map对象
     * @param key
     * @return List
     */
    public  <T> T delMap(String key) {
        Jedis jedis = pool.getResource();
        try {
            logger.debug(" delMap : " + key);
            return (T)jedis.hdel(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }

    /**
     * 移除缓存map中的数据
     * @param key
     * @return List
     */
    public  <T> T delMapValue(String key, String field) {
        Jedis jedis = pool.getResource();
        try {
            logger.debug("delMapValue : " + key + "; field : " + field);
            if(exists(key)){
                return (T)jedis.hdel(key, field);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            jedis.close();
        }
        return null;
    }
    
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public void lpush(String key, String[] value, int expireSecond) {
 
        Jedis jedis = pool.getResource();
        try {
        	jedis.lpush(key, value);
        	jedis.expire(key, expireSecond);
        } catch (Exception e) {
            e.printStackTrace();
 
        } finally {
            //返还到连接池
        	jedis.close();
 
        }
    }
 
    /**
     * 弹出一个队列数据
     * @param byte[] key 键名
     * @return
     */
    public String rpop(String key) {
 
    	Jedis jedis = pool.getResource();
    	String value = null;
        try {
        	value = jedis.rpop(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        	//返还到连接池
        	jedis.close();
 
        }
        return value;
    }
    /**
     * 获取队列数据
     * @param byte[] key 键名
     * @return
     */
    public List<String> lrange(String key) {
    	
    	Jedis jedis = pool.getResource();
    	List<String> value = null;
    	try {
    		value = jedis.lrange(key, 0, -1);
    	} catch (Exception e) {
    		e.printStackTrace();
    	} finally {
    		//返还到连接池
    		jedis.close();
    		
    	}
    	return value;
    }
    
    
    /**
	 * 按类型缓存
	 * @param type
	 * @refresh 是否刷新缓存,如果第一页，则立马刷新
	 * @return
	 */
	public Date getqueryCacheTime(String queryCacheTimeKey, boolean refresh, int expireTime) {
		Date cacheTime = null;
		String jsonStr = null;
		//是否刷新缓存
		if(false == refresh){
			jsonStr = getRedisCacheInfo(queryCacheTimeKey);
		}
		if(StringUtils.isNotBlank(jsonStr)){
			cacheTime = gson.fromJson(jsonStr, new TypeToken<Date>() {}.getType());
		}else {
			cacheTime = new Date();
			setRedisCacheInfo(queryCacheTimeKey, expireTime, gson.toJson(cacheTime));
		}
		return cacheTime;
	}
	
    public static void main(String[] args) {
    	RedisCacheManger manger = new RedisCacheManger();
    	String[] packetStrList = new String[10];
    	for(int i=0; i<10; i++) {
    		packetStrList[i] = i + "";
		}	
    	manger.lpush("test", packetStrList, REDIS_CACHE_EXPIRE_WEEK);//把所有map的key放入一个队列test中
    	
        for(int i=0; i<10; i++) {
			String key = manger.rpop("test");
			System.out.println("弹出：" + key);
		}	
    }

}
