/**
 * .
 */
package com.cmig.future.csportal.common.utils;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.exceptions.JedisConnectionException;
import redis.clients.jedis.exceptions.JedisException;
import redis.clients.util.Pool;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Jedis Cache 工具类
 * 
 * @author ThinkGem
 * @version 2014-6-29
 */
public class JedisUtils {

	private static Logger logger = LoggerFactory.getLogger(JedisUtils.class);
	
	private static Pool<Jedis> jedisPool;
	static {
        if(Global.getBoolean("redis.useSentinel",false)){
            jedisPool = SpringContextHolder.getBean(JedisSentinelPool.class);
        }else{
            jedisPool = SpringContextHolder.getBean(JedisPool.class);
        }
    }

	public static final String KEY_PREFIX = Global.getConfig("redis.keyPrefix");
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static String get(String key) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.get(key);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("get {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("get {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	

	/**
	 * 返回哈希表 key 中给定域 field 的值。
	 * @param key
	 * @param field
	 * @return
	 */
	public static String hget(String key,String field) {
		String value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.hexists(key,field)) {
				value = jedis.hget(key,field);
				value = StringUtils.isNotBlank(value) && !"nil".equalsIgnoreCase(value) ? value : null;
				logger.debug("hget {} = {} = {}", key,field, value);
			}
		} catch (Exception e) {
			logger.warn("hget {} = {} = {}", key,field,value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}

	/**
	 * 删除map中某个Field
	 * @param key
	 * @param field
	 * @return
	 */
	public static boolean hdel(String key,String field) {
		Jedis jedis = null;
		try {
			jedis = getResource();
			Long result=jedis.hdel(key,field);
			if (result>0) {
				return true;
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return false;
	}



	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Object getObject(String key) {
		Object value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = toObject(jedis.get(getBytesKey(key)));
				logger.debug("getObject {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObject {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String set(String key, String value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(cacheSeconds<=0){
				result = jedis.set(key,value);
			}else {
				result = jedis.setex(key, cacheSeconds, value);
			}
			logger.debug("set {} = {}", key, value);
		} catch (Exception e) {
            e.printStackTrace();
			logger.warn("set {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 判断哈希表中field是否存在
	 * @param key
	 * @param field
	 * @return
	 */
	public static boolean hexists(String key, String field) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hexists(key,field);
		} catch (Exception e) {
			logger.error(e.getMessage());
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 将哈希表 key 中的域 field 的值设为 value 
	 * @param key
	 * @param field
	 * @param value
	 * @param cacheSeconds
	 * @return
	 */
	public static Long hset(String key, String field, String value, int cacheSeconds) {
		Long result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hset(key,field,value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("hset {} = {} = {}", key, field, value);
		} catch (Exception e) {
			logger.warn("set {} = {} = {}", key, field, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObject(String key, Object value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if(cacheSeconds<=0){
				result = jedis.set(getBytesKey(key),toBytes(value));
			}else {
				result = jedis.setex(getBytesKey(key), cacheSeconds, toBytes(value));
			}
			logger.debug("setObject {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObject {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置缓存,无超时时间
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String setObject(String key, Object value) {
		return setObject(key, value, 0);
	}
	
	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<String> getList(String key) {
		List<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.lrange(key, 0, -1);
				logger.debug("getList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getList {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取List缓存
	 * @param key 键
	 * @return 值
	 */
	public static List<Object> getObjectList(String key) {
		List<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				List<byte[]> list = jedis.lrange(getBytesKey(key), 0, -1);
				value = Lists.newArrayList();
				for (byte[] bs : list){
					value.add(toObject(bs));
				}
				logger.debug("getObjectList {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectList {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置List缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setList(String key, List<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.rpush(key, (String[])value.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setList {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置List缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectList(String key, List<Object> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value){
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][])list.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectList {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectList {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.rpush(key, value);
			logger.debug("listAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listAdd {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向List缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long listObjectAdd(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			List<byte[]> list = Lists.newArrayList();
			for (Object o : value){
				list.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][])list.toArray());
			logger.debug("listObjectAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("listObjectAdd {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<String> getSet(String key) {
		Set<String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.smembers(key);
				logger.debug("getSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getSet {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取缓存
	 * @param key 键
	 * @return 值
	 */
	public static Set<Object> getObjectSet(String key) {
		Set<Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = Sets.newHashSet();
				Set<byte[]> set = jedis.smembers(getBytesKey(key));
				for (byte[] bs : set){
					value.add(toObject(bs));
				}
				logger.debug("getObjectSet {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectSet {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置Set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setSet(String key, Set<String> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.sadd(key, (String[])value.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSet {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Set缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static long setObjectSet(String key, Set<Object> value, int cacheSeconds) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value){
				set.add(toBytes(o));
			}
			result = jedis.sadd(getBytesKey(key), (byte[][])set.toArray());
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectSet {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectSet {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long setSetAdd(String key, String... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.sadd(key, value);
			logger.debug("setSetAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSetAdd {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 向Set缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static long setSetObjectAdd(String key, Object... value) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			Set<byte[]> set = Sets.newHashSet();
			for (Object o : value){
				set.add(toBytes(o));
			}
			result = jedis.rpush(getBytesKey(key), (byte[][])set.toArray());
			logger.debug("setSetObjectAdd {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setSetObjectAdd {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, String> getMap(String key) {
		Map<String, String> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				value = jedis.hgetAll(key);
				logger.debug("getMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getMap {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 获取Map缓存
	 * @param key 键
	 * @return 值
	 */
	public static Map<String, Object> getObjectMap(String key) {
		Map<String, Object> value = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				value = Maps.newHashMap();
				Map<byte[], byte[]> map = jedis.hgetAll(getBytesKey(key));
				for (Map.Entry<byte[], byte[]> e : map.entrySet()){
					value.put(StringUtils.toString(e.getKey()), toObject(e.getValue()));
				}
				logger.debug("getObjectMap {} = {}", key, value);
			}
		} catch (Exception e) {
			logger.warn("getObjectMap {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return value;
	}
	
	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setMap(String key, Map<String, String> value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)) {
				jedis.del(key);
			}
			result = jedis.hmset(key, value);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setMap {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 设置Map缓存
	 * @param key 键
	 * @param value 值
	 * @param cacheSeconds 超时时间，0为不超时
	 * @return
	 */
	public static String setObjectMap(String key, Map<String, Object> value, int cacheSeconds) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))) {
				jedis.del(key);
			}
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()){
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
			if (cacheSeconds != 0) {
				jedis.expire(key, cacheSeconds);
			}
			logger.debug("setObjectMap {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("setObjectMap {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String mapPut(String key, Map<String, String> value) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hmset(key, value);
			logger.debug("mapPut {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("mapPut {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 向Map缓存中添加值
	 * @param key 键
	 * @param value 值
	 * @return
	 */
	public static String mapObjectPut(String key, Map<String, Object> value) {
		String result = null;
		Jedis jedis = null;
		try {
			jedis = getResource();
			Map<byte[], byte[]> map = Maps.newHashMap();
			for (Map.Entry<String, Object> e : value.entrySet()){
				map.put(getBytesKey(e.getKey()), toBytes(e.getValue()));
			}
			result = jedis.hmset(getBytesKey(key), (Map<byte[], byte[]>)map);
			logger.debug("mapObjectPut {} = {}", key, value);
		} catch (Exception e) {
			logger.warn("mapObjectPut {} = {}", key, value, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 移除Map缓存中的值
	 * @param key 键
	 * @param mapKey 值
	 * @return
	 */
	public static long mapRemove(String key, String mapKey) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hdel(key, mapKey);
			logger.debug("mapRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapRemove {}  {}", key, mapKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 移除Map缓存中的值
	 * @param key 键
	 * @param mapKey 值
	 * @return
	 */
	public static long mapObjectRemove(String key, String mapKey) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hdel(getBytesKey(key), getBytesKey(mapKey));
			logger.debug("mapObjectRemove {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectRemove {}  {}", key, mapKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param mapKey 值
	 * @return
	 */
	public static boolean mapExists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hexists(key, mapKey);
			logger.debug("mapExists {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapExists {}  {}", key, mapKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 判断Map缓存中的Key是否存在
	 * @param key 键
	 * @param mapKey 值
	 * @return
	 */
	public static boolean mapObjectExists(String key, String mapKey) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.hexists(getBytesKey(key), getBytesKey(mapKey));
			logger.debug("mapObjectExists {}  {}", key, mapKey);
		} catch (Exception e) {
			logger.warn("mapObjectExists {}  {}", key, mapKey, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long del(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(key)){
				result = jedis.del(key);
				logger.debug("del {}", key);
			}else{
				logger.debug("del {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("del {}", key, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 删除缓存
	 * @param key 键
	 * @return
	 */
	public static long delObject(String key) {
		long result = 0;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis.exists(getBytesKey(key))){
				result = jedis.del(getBytesKey(key));
				logger.debug("delObject {}", key);
			}else{
				logger.debug("delObject {} not exists", key);
			}
		} catch (Exception e) {
			logger.warn("delObject {}", key, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean exists(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(key);
			logger.debug("exists {}", key);
		} catch (Exception e) {
			logger.warn("exists {}", key, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}
	
	/**
	 * 缓存是否存在
	 * @param key 键
	 * @return
	 */
	public static boolean existsObject(String key) {
		boolean result = false;
		Jedis jedis = null;
		try {
			jedis = getResource();
			result = jedis.exists(getBytesKey(key));
			logger.debug("existsObject {}", key);
		} catch (Exception e) {
			logger.warn("existsObject {}", key, e);
		} finally {
			returnResource(jedis);
		}
		return result;
	}

	/**
	 * 获取资源
	 * @return
	 * @throws JedisException
	 */
	public static Jedis getResource() throws JedisException {
		Jedis jedis = null;
		try {
			jedis = jedisPool.getResource();
			logger.debug("getResource.", jedis);
		} catch (JedisException e) {
			logger.warn("getResource.", e);
			returnBrokenResource(jedis);
			throw e;
		}
		return jedis;
	}

	/**
	 * 归还资源
	 * @param jedis
	 */
	public static void returnBrokenResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}
	
	/**
	 * 释放资源
	 * @param jedis
	 */
	public static void returnResource(Jedis jedis) {
		if (jedis != null) {
			jedis.close();
		}
	}

	/**
	 * 获取byte[]类型Key
	 * @param object
	 * @return
	 */
	public static byte[] getBytesKey(Object object){
		if(object instanceof String){
    		return StringUtils.getBytes((String)object);
    	}else{
    		return ObjectUtils.serialize(object);
    	}
	}
	
	/**
	 * Object转换byte[]类型
	 * @param object
	 * @return
	 */
	public static byte[] toBytes(Object object){
    	return ObjectUtils.serialize(object);
	}

	/**
	 * byte[]型转换Object
	 * @param bytes
	 * @return
	 */
	public static Object toObject(byte[] bytes){
		return ObjectUtils.unserialize(bytes);
	}

    /**
     * 延长失效时间
     * @param key
     * @param second
     */
    public static void expire(String key,int second){
        Jedis jedis = null;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                jedis.expire(key,second);
            }
        } catch (Exception e) {
            logger.warn("expire {} = {}", key, second, e);
        } finally {
            returnResource(jedis);
        }
    }


    /**
     * 获取key剩余生存时间
     * @param key
     * @return
     */
    public static int getLeftSeconds(String key){
        Jedis jedis = null;
        long leftSec=0;
        try {
            jedis = getResource();
            if (jedis.exists(key)) {
                leftSec=jedis.ttl(key);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            returnResource(jedis);
        }
        return new Long(leftSec).intValue();
    }

	/**
	 * incr递增1并返回递增后的结果
	 * @param key
	 * @return
	 */
	public static Long incr(String key){
		Jedis jedis = null;
		long count=0;
		try {
			jedis = getResource();
			count=jedis.incr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return count;
	}

	/**
	 * incr递增1并返回递增后的结果
	 * @param key
	 * @param second
	 * @return
	 */
	public static Long incr(String key,int second){
		Jedis jedis = null;
		long count=0;
		try {
			jedis = getResource();
			count=jedis.incr(key);
			if (second>0&&count == 1) {
				//设置有效期
				jedis.expire(key, second);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return count;
	}

	/**
	 * decr递减1并返回递减后的结果
	 * @param key
	 * @return
	 */
	public static Long decr(String key){
		Jedis jedis = null;
		long num=0;
		try {
			jedis = getResource();
			num=jedis.decr(key);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return num;
	}


	public static long hincr(String key,String field,int increment,int second){
		Jedis jedis = null;
		long count=0;
		try {
			jedis = getResource();
			count=jedis.hincrBy(key,field,increment);
			if (second>0&&count == increment) {
				//设置有效期
				jedis.expire(key, second);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			returnResource(jedis);
		}
		return count;
	}

	private static final String REDIS_LOCK_KEY="csp.lock.key.";
	private static final int REDIS_LOCK_EXPIRED_TIME=30;

	/**
	 * 锁在给定的等待时间内空闲，则获取锁成功 返回true， 否则返回false
	 * @param subKey
	 * @param timeout 如果timeout=0,取不到锁时,不等待,直接返回.
	 * @param unit
	 * @return
	 */
	public static boolean tryLock(String subKey, long timeout, TimeUnit unit) {
		String key = REDIS_LOCK_KEY + subKey;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis == null) {
				return Boolean.FALSE;
			}
			long nano = System.nanoTime();
			do {
				logger.debug("try lock key: " + key);
				Long i = jedis.setnx(key, key);
				if (i == 1) {
					jedis.expire(key, REDIS_LOCK_EXPIRED_TIME);
					logger.debug("get lock, key: " + key + " , expire in " + REDIS_LOCK_EXPIRED_TIME + " seconds.");
					return Boolean.TRUE;
				} else { // 存在锁
					if (logger.isDebugEnabled()) {
						String desc = jedis.get(key);
						logger.debug("key: " + key + " locked by another business：" + desc);
					}
				}
				if (timeout == 0) { //取不到锁时,不等待,直接返回.
					break;
				}
				Thread.sleep(200);
			} while ((System.nanoTime() - nano) < unit.toNanos(timeout));//取不到锁时等待,直到timeout
			return Boolean.FALSE;
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
			returnBrokenResource(jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(jedis);
		}
		return Boolean.FALSE;
	}


	/**
	 * 如果锁空闲立即返回   获取失败 一直等待
	 * @param subKey
	 */
	public static boolean lock(String subKey) {
		String key = REDIS_LOCK_KEY + subKey;
		Jedis jedis = null;
		boolean isLock = false;
		try {
			jedis = getResource();
			if (jedis == null) {
				return isLock;
			}
			while (true) {
				logger.debug("lock key: " + key);
				Long i = jedis.setnx(key, key);
				if (i == 1) {
					jedis.expire(key, REDIS_LOCK_EXPIRED_TIME);
					logger.debug("get lock, key: " + key + " , expire in " + REDIS_LOCK_EXPIRED_TIME + " seconds.");
					isLock = true;
				} else {
					if (logger.isDebugEnabled()) {
						String desc = jedis.get(key);
						logger.debug("key: " + key + " locked by another business：" + desc);
					}
					isLock = false;
				}
				Thread.sleep(500);
			}
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
			returnBrokenResource(jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(jedis);
		}
		return isLock;
	}
	/**
	 * 释放锁
	 * @param subKey
	 */
	public static void unLock(String subKey) {
		String key = REDIS_LOCK_KEY + subKey;
		Jedis jedis = null;
		try {
			jedis = getResource();
			if (jedis == null) {
				return;
			}
			jedis.del(key);
			logger.debug("release lock, keys :" + key);
		} catch (JedisConnectionException je) {
			logger.error(je.getMessage(), je);
			returnBrokenResource(jedis);
		} catch (Exception e) {
			logger.error(e.getMessage(), e);
		} finally {
			returnResource(jedis);
		}
	}

}
