package com.cmig.future.csportal.module.sys.code;

import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.hand.hap.cache.impl.SysCodeCache;
import com.hand.hap.system.dto.CodeValue;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 19:20 2017/6/5.
 * @Modified by zhangtao on 19:20 2017/6/5.
 */
public class CodeUtil {
	private static SysCodeCache sysCodeCache=SpringContextHolder.getBean("codeCache");

	/**
	 *
	 * @param label
	 * @param type
	 * @param defaultLabel
	 * @param language
	 * @return
	 */
	public static String getDictValue(String label, String type, String defaultLabel,String language){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(label)){
			if(StringUtils.isEmpty(language)) language="zh_CN";
			for (CodeValue codeValue : sysCodeCache.getValue(type+"."+language).getCodeValues()){
				if (label.equals(codeValue.getMeaning())){
					return codeValue.getValue();
				}
			}
		}
		return defaultLabel;
	}

	/**
	 *
	 * @param value
	 * @param type
	 * @param defaultValue
	 * @param language
	 * @return
	 */
	public static String getDictLabel(String value, String type, String defaultValue,String language){
		if (StringUtils.isNotBlank(type) && StringUtils.isNotBlank(value)){
			if(StringUtils.isEmpty(language)) language="zh_CN";
			for (CodeValue codeValue : sysCodeCache.getValue(type+"."+language).getCodeValues()){
				if (value.equals(codeValue.getValue())){
					return codeValue.getMeaning();
				}
			}
		}
		return defaultValue;
	}

	/**
	 *
	 * @param type
	 * @param language
	 * @return
	 */
	public static List<CodeValue> getDictList(String type,String language) {
		if(StringUtils.isEmpty(language)) language="zh_CN";
		return sysCodeCache.getValue(type+"."+language).getCodeValues();
	}


	/**
	 * 校验字典值是否存在，存在true，不存在false
	 * @param type
	 * @param value
	 * @return
	 */
	public static boolean checkDicValueExists(String type,String value){
		return !StringUtils.isEmpty(getDictLabel(value,type,null,null));
	}
}
