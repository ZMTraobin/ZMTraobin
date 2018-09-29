package com.cmig.future.csportal.common.utils.redis;

import org.apache.commons.lang.StringUtils;

import java.util.HashSet;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 19:49 2017/10/23.
 * @Modified by zhangtao on 19:49 2017/10/23.
 */
public class RedisNodeAutoConfig extends HashSet<String> {

	public void setSentinels(String[] sentinels) {
		for (String s : sentinels) {
			if (StringUtils.isBlank(s) || s.contains("$")) {
				continue;
			}
			add(s);
		}
	}
}
