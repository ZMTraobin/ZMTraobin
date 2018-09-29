package com.cmig.future.csportal.module.operate.integral;

import com.cmig.future.csportal.common.utils.StringUtils;

import java.net.URLEncoder;
import java.util.*;

/**
 * Created by ThinkPad on 2017/5/31.
 */
public class IntegralRuleSign {

	public static String formatMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
		String buff = "";
		Map<String, String> tmpMap = paraMap;
		try {
			List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
			// 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
			Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {
				@Override
				public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
					return (o1.getKey()).toString().compareTo(o2.getKey());
				}
			});
			// 构造URL 键值对的格式
			StringBuilder buf = new StringBuilder();
			for (Map.Entry<String, String> item : infoIds) {
				if (StringUtils.isNotBlank(item.getKey())) {
					String key = item.getKey();
					String val = item.getValue();
					if (urlEncode) {
						val = URLEncoder.encode(val, "utf-8");
					}
					if (keyToLower) {
						buf.append(key.toLowerCase() + "=" + val);
					} else {
						buf.append(key + "=" + val);
					}
					buf.append("&");
				}

			}
			buff = buf.toString();
			if (buff.isEmpty() == false) {
				buff = buff.substring(0, buff.length() - 1);
			}
		} catch (Exception e) {
			return null;
		}
		return buff;
	}


}
