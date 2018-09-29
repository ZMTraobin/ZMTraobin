package com.cmig.future.csportal.common.utils.cache;


import com.cmig.future.csportal.common.utils.JedisUtils;
import com.cmig.future.csportal.common.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * 调用ehcache缓存, ehcache缓存名为customCache
 * @author luzongwei
 */
public class FinderCache {

    /**
     * 多个数据库表VOImpl类分隔符
     */
    public static final String GROUP_SEPARATOR = "::";

    /**
     * 方法名和参数分隔符
     */
    private static final String _PARAMS_SEPARATOR = ",";

    /**
     * 缓存范围限定分隔符
     */
    private static final String CACHE_SERIAL_KEY_SEPARATOR = "-";

    /**
     * customcache分隔符
     */
    private static final String POUND = "#";

    /**
     * 针对返回结果是null的情况,也可以被缓存
     */
    public static final String IS_NULL = "IS_NULL";

    private static Log _log = LogFactory.getLog(FinderCache.class);

    private static String API_CACHE_KEY_PREFIX="api.key.";

    private static String API_CACHE_KEY_GROUP=API_CACHE_KEY_PREFIX+"group.HashMap";

    private static String API_CACHE_KEY_RESULT=API_CACHE_KEY_PREFIX+"result.HashMap";

   public static  HashMap<String, CopyOnWriteArraySet<String>> getClassGroup() {
       if (JedisUtils.exists(API_CACHE_KEY_GROUP)) {
           return (HashMap<String, CopyOnWriteArraySet<String>>) JedisUtils.getObject(API_CACHE_KEY_GROUP);
       } else {
           HashMap<String, CopyOnWriteArraySet<String>> group = new HashMap<String, CopyOnWriteArraySet<String>>();
           setClassGroup(group);
           return group ;
       }
   }

    public static void setClassGroup(HashMap<String, CopyOnWriteArraySet<String>> group){
        JedisUtils.setObject(API_CACHE_KEY_GROUP, group);
    }

       public static HashMap<String, Object> getCacheMap(){
        if(JedisUtils.exists(API_CACHE_KEY_RESULT)){
            return (HashMap<String, Object>) JedisUtils.getObjectMap(API_CACHE_KEY_RESULT);
        }else{
            HashMap<String, Object> cacheMap=new HashMap<String, Object>();
            //setCacheMap(cacheMap);
            return cacheMap;
        }
    }

    public static void setCacheMap(HashMap<String, Object> cacheMap){
        JedisUtils.setObjectMap(API_CACHE_KEY_RESULT, cacheMap,0);
    }

    public static String getKey(String key){
        return API_CACHE_KEY_PREFIX+key;
    }

	/**
	 * 清除所有customCache的ehcache缓存
	 */
	public static void clearCache() {
        HashMap<String, Object> cacheMap=new HashMap<String, Object>();
         setCacheMap(cacheMap);
		if(_log.isInfoEnabled()) {
			_log.info("REMOVE ALL");
		}
	}

	/**
	 * 清除一个表的所有缓存
	 * @param clasz 缓存涉及的数据库表VOImpl.class
	 */
	public static void clearCache(Class clasz) {
		if (clasz != null) {
			clearCache(clasz.getName());
		} else {
			throw new RuntimeException("model class is null");
		}
	}

	/**
	 * 清除一个表的所有缓存
	 * @param className 缓存涉及的数据库表VOImpl.class.getName()
	 */
	public static void clearCache(String className) {
        HashMap<String, CopyOnWriteArraySet<String>> group = getClassGroup();
		CopyOnWriteArraySet<String> set = group.get(className);
		if (set == null||set.size()==0) {
			return;
		}
		for (String key : set) {
            JedisUtils.mapRemove(API_CACHE_KEY_RESULT,key);
			set.remove(key);
			if(_log.isInfoEnabled()) {
				_log.info("REMOVE key=" + key);
			}
		}
        setClassGroup(group);
	}

	/**
	 * 取得缓存. 用法如下:
	 * Object cacheresult = FinderCache.get(key);
	 * 	if (cacheresult != null) {
	 * 		return FinderCache.IS_NULL.equals(cacheresult) ? null : (BudgetaccountsVOImpl) cacheresult;
	 * 	}
	 * @param key 缓存主键
	 * @return
	 */
	public static Object get(String key) {
		return getCacheMap().get(key)!= null ?getCacheMap().get(key):null;
	}

	/**
	 * 取得缓存. 用法如下:
	 * Object cacheresult = FinderCache.get(DimvalCombinationVOImpl.class.getName() + FinderCache.GROUP_SEPARATOR + PolicyImplementationsVOImpl.class.getName(), "findBudgetAccountBySetIdAndAccountCode", "budgetAccountCode", budgetAccountCode);
	 * 	if (cacheresult != null) {
	 * 		return FinderCache.IS_NULL.equals(cacheresult) ? null : (BudgetaccountsVOImpl) cacheresult;
	 * 	}
	 * @param className 缓存涉及的数据库表VOImpl.class.getName(), 多个时用FinderCache.GROUP_SEPARATOR分隔
	 * @param methodName java调用的方法名
	 * @param param java调用的方法参数名
	 * @param arg java调用的方法参数值
	 * @return
	 */
	public static Object get(String className, String methodName, String param, Object arg) {
		return get(className, methodName, new String[]{param}, new Object[]{arg});
	}

	/**
	 * 取得缓存. 用法如下:
	 * Object cacheresult = FinderCache.get(DimvalCombinationVOImpl.class.getName() + FinderCache.GROUP_SEPARATOR + PolicyImplementationsVOImpl.class.getName(), "findBudgetAccountBySetIdAndAccountCode", new String[] { "budgetAccountCode", "budgetAccountSetId" }, new Object[] { budgetAccountCode, budgetAccountSetId });
	 * 	if (cacheresult != null) {
	 * 		return FinderCache.IS_NULL.equals(cacheresult) ? null : (BudgetaccountsVOImpl) cacheresult;
	 * 	}
	 * @param className 缓存涉及的数据库表VOImpl.class.getName(), 多个时用FinderCache.GROUP_SEPARATOR分隔
	 * @param methodName java调用的方法名
	 * @param params java调用的方法参数名
	 * @param args java调用的方法参数值
	 * @return
	 */
	public static Object get(String className, String methodName, String[] params, Object[] args) {
		String key = _encodeKey(className, methodName, params, args);
		if (key == null) {
			return null;
		}
		Object result = get(key);
		if (result != null) {
			if(_log.isInfoEnabled()) {
				_log.info("GET key=" + key + ":" + printResult(result));
			}
		} else {
			if(_log.isInfoEnabled()) {
				_log.info("NO  key=" + key);
			}
		}
		return result;
	}

	private static String printResult(Object result) {
		if (result == null) {
			return "null";
		}
		if (result instanceof String || result instanceof Integer || result instanceof BigDecimal) {
			return result.toString();
		}
		return "HASHCODE:" + String.valueOf(result.hashCode());
	}

	/**
	 * 取得缓存. 用法如下:
	 * Object cacheresult = FinderCache.get(DimvalCombinationVOImpl.class.getName() + FinderCache.GROUP_SEPARATOR + PolicyImplementationsVOImpl.class.getName(), "findBudgetAccountBySetIdAndAccountCode", new String[] { "budgetAccountCode", "budgetAccountSetId" }, new Object[] { budgetAccountCode, budgetAccountSetId }, cacheSerialKey);
	 * 	if (cacheresult != null) {
	 * 		return FinderCache.IS_NULL.equals(cacheresult) ? null : (BudgetaccountsVOImpl) cacheresult;
	 * 	}
	 * @param className 缓存涉及的数据库表VOImpl.class.getName(), 多个时用FinderCache.GROUP_SEPARATOR分隔
	 * @param methodName java调用的方法名
	 * @param params java调用的方法参数名
	 * @param args java调用的方法参数值
	 * @param cacheSerialKey 缓存范围限定 可为null, 为null时不查询缓存
	 * @return
	 */
	public static Object get(String className, String methodName, String[] params, Object[] args, String cacheSerialKey) {
		if (StringUtils.isEmpty(cacheSerialKey)) {
			return null;
		}
		return get(cacheSerialKey + FinderCache.CACHE_SERIAL_KEY_SEPARATOR + className, methodName, params, args);
	}

	/**
	 * 保存缓存. 用法如下:
	 * FinderCache.put(key, result);
	 * 		
	 * @param key 缓存主键
	 * @param result 结果
	 * @return
	 */
	public static void put(String key, Object result) {
        HashMap<String, Object> cacheMap=getCacheMap();
        cacheMap.put(key, result == null ? FinderCache.IS_NULL : result);
        setCacheMap(cacheMap);
	}

	/**
	 * 保存缓存. 用法如下:
	 * FinderCache.put(DimvalCombinationVOImpl.class.getName() + FinderCache.GROUP_SEPARATOR + PolicyImplementationsVOImpl.class.getName(), "findDimCombRollUpByCombinationId", "dimvalCombinationId" dimvalCombinationId, dimvalCombinationRollupAndPolicyImplementList, cacheSerialKey);
	 * 		
	 * @param className 缓存涉及的数据库表VOImpl.class.getName(), 多个时用FinderCache.GROUP_SEPARATOR分隔
	 * @param methodName java调用的方法名
	 * @param param java调用的方法参数名
	 * @param arg java调用的方法参数值
	 * @param result 结果
	 * @return
	 */
	public static void put(String className, String methodName, String param, Object arg, Object result) {
		put(className, methodName, new String[]{param}, new Object[]{arg}, result);
	}

	/**
	 * 保存缓存. 用法如下:
	 * FinderCache.put(DimvalCombinationVOImpl.class.getName() + FinderCache.GROUP_SEPARATOR + PolicyImplementationsVOImpl.class.getName(), "findDimCombRollUpByCombinationId", new String[] { "dimvalCombinationId", "setOfBooksId" }, new Object[] { 
	 *    		dimvalCombinationId, setOfBooksId}, dimvalCombinationRollupAndPolicyImplementList, cacheSerialKey);
	 * 		
	 * @param className 缓存涉及的数据库表VOImpl.class.getName(), 多个时用FinderCache.GROUP_SEPARATOR分隔
	 * @param methodName java调用的方法名
	 * @param params java调用的方法参数名
	 * @param args java调用的方法参数值
	 * @param result 结果
	 * @return
	 */
	public static void put(String className, String methodName, String[] params, Object[] args, Object result) {
		
		String key = _encodeKey(className, methodName, params, args);
		if (key == null) {
			return;
		}
        HashMap<String, Object> cacheMap=getCacheMap();
        cacheMap.put(key, result == null ? FinderCache.IS_NULL : result);
        setCacheMap(cacheMap);

		if(_log.isInfoEnabled()) {
			_log.info("PUT key=" + key + ":" + printResult(result));
		}

        HashMap<String, CopyOnWriteArraySet<String>> group=getClassGroup();
		if(className.indexOf(FinderCache.GROUP_SEPARATOR) <= 0) {
			if (group.containsKey(className)) {
				if (!group.get(className).contains(key)) {
					group.get(className).add(key);
				}
			} else {
				CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<String>();
				set.add(key);
				group.put(className, set);
			}
            setClassGroup(group);
			return;
		}
		
		for (String _className : className.indexOf(FinderCache.CACHE_SERIAL_KEY_SEPARATOR) > 0 ? className.split(FinderCache.CACHE_SERIAL_KEY_SEPARATOR)[1].split(FinderCache.GROUP_SEPARATOR) : className.split(FinderCache.GROUP_SEPARATOR)) {
			if (group.containsKey(_className)) {
				if (!group.get(_className).contains(key)) {
					group.get(_className).add(key);
				}
			} else {
				CopyOnWriteArraySet<String> set = new CopyOnWriteArraySet<String>();
				set.add(key);
				group.put(_className, set);
			}
		}
        setClassGroup(group);
		return;
	}

	/**
	 * 保存缓存. 用法如下:
	 * FinderCache.put(DimvalCombinationVOImpl.class.getName() + FinderCache.GROUP_SEPARATOR + PolicyImplementationsVOImpl.class.getName(), "findDimCombRollUpByCombinationId", new String[] { "dimvalCombinationId", "setOfBooksId" }, new Object[] { 
	 *    		dimvalCombinationId, setOfBooksId}, dimvalCombinationRollupAndPolicyImplementList, cacheSerialKey);
	 * 		
	 * @param className 缓存涉及的数据库表VOImpl.class.getName(), 多个时用FinderCache.GROUP_SEPARATOR分隔
	 * @param methodName java调用的方法名
	 * @param params java调用的方法参数名
	 * @param args java调用的方法参数值
	 * @param result 结果
	 * @param cacheSerialKey 缓存范围限定 可为null, 为null时不保存缓存
	 * @return
	 */
	public static void put(String className, String methodName, String[] params, Object[] args, Object result, String cacheSerialKey) {
		if (StringUtils.isEmpty(cacheSerialKey)) {
			return;
		}
		put(cacheSerialKey + FinderCache.CACHE_SERIAL_KEY_SEPARATOR + className, methodName, params, args, result);
	}
	
	private static String _encodeKey(String className, String methodName, String[] params, Object[] args) {
		StringBuffer _key = new StringBuffer();
		_key.append(API_CACHE_KEY_PREFIX);
		_key.append(FinderCache.POUND);
		_key.append(className);
		_key.append(FinderCache.POUND);
		_key.append(methodName);
		_key.append(FinderCache.POUND);

		for (int i = 0; i < params.length; i++) {
			if (i > 0) {
				_key.append(_PARAMS_SEPARATOR);
			}
			_key.append(params[i]);
		}

		_key.append(FinderCache.POUND);

		for (int i = 0; i < args.length; i++) {
			if (i > 0) {
				_key.append(_PARAMS_SEPARATOR);
			}
			_key.append(String.valueOf(args[i]));
		}
		return _key.toString();
	}



}
