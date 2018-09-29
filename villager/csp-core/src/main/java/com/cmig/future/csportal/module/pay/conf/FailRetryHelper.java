package com.cmig.future.csportal.module.pay.conf;

import com.cmig.future.csportal.common.utils.StringUtils;

import java.util.HashMap;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 9:30 2017/12/22.
 * @Modified by zhangtao on 9:30 2017/12/22.
 */
public class FailRetryHelper {

	/**
	 * 重复通知时间间隔
	 */
	private static HashMap<Integer, Integer> notifyNextMap = new HashMap<>();
	private static final Integer defaultSeconds=24*60*60;
	static {
		// 5s、10s、2min、5min、10min、30min、1h、2h、6h、15h、24h
		notifyNextMap.put(1, 5);
		notifyNextMap.put(2, 10);
		notifyNextMap.put(3, 2*60);
		notifyNextMap.put(4, 5*60);
		notifyNextMap.put(5, 10*60);
		notifyNextMap.put(6, 30*60);
		notifyNextMap.put(7, 1*60*60);
		notifyNextMap.put(8, 2*60*60);
		notifyNextMap.put(9, 6*60*60);
		notifyNextMap.put(10, 15*60*60);
		notifyNextMap.put(11, 24*60*60);
	}

	/**
	 * 获取下次通知时间间隔（秒）
	 * @param times
	 * @return
	 */
	public static int getNextTimeSeconds(Integer times){
		if(!StringUtils.isEmpty(times)&&notifyNextMap.containsKey(times)){
			return notifyNextMap.get(times);
		}
		return defaultSeconds;
	}
}
