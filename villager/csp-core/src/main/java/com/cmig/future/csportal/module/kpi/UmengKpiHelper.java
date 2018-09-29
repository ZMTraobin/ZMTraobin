package com.cmig.future.csportal.module.kpi;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.module.kpi.response.Duration;
import com.cmig.future.csportal.module.kpi.response.UmengAppResponse;
import com.cmig.future.csportal.module.kpi.response.UmengBaseInfo;
import com.cmig.future.csportal.module.kpi.response.UmengEventGroup;
import com.cmig.future.csportal.module.kpi.response.UserBaseData;
import org.apache.commons.codec.binary.Base64;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 10:31 2018/1/25.
 * @Modified by zhangtao on 10:31 2018/1/25.
 */
public class UmengKpiHelper {

	private static String serverUrl;
	private static String authorization;
	private static Map headMap=new HashMap();

	/**
	 * daily :日使用时长;
	 * daily_per_launch :单次使用时长;
	 */
	public static final String PERIOD_TYPE_DAILY="daily";
	public static final String PERIOD_TYPE_DAILY_PER_LAUNCH="daily_per_launch";

	static{
		serverUrl= Global.getConfig("UMENG.serverUrl");
		authorization=getAuthorization();
		headMap.put("Authorization",authorization);
	}

	public static String getAuthorization() {
		String account=Global.getConfig("UMENG.username","");
		String password=Global.getConfig("UMENG.password","");
		String authorization = null;
		try {
			authorization = Base64.encodeBase64String(new String(account + ":" + password).getBytes("UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "Basic "+authorization;
	}

	/**
	 * 获取友盟app信息
	 * @return
	 * @throws Exception
	 */
	public static List<UmengAppResponse> getApps() throws Exception {
		String url=serverUrl+"/apps?per_page=20&page=1";
		String result= HttpUtil.get(url,headMap);
		return JSONArray.parseArray(result, UmengAppResponse.class);
	}

	/**
	 * 获取pv、uv信息
	 * @param appkey
	 * @param date
	 * @return
	 * @throws Exception
	 */
	public static UserBaseData getUserKpi(String appkey, String date) throws Exception {
		String url=serverUrl+"/base_data?appkey="+appkey+"&date="+date;
		String result= HttpUtil.get(url,headMap);
		return JSONObject.parseObject(result,UserBaseData.class);
	}

	/**
	 * 获取使用时长信息
	 * @param appkey
	 * @param date
	 * @param periodType
	 * @return
	 * @throws Exception
	 */
	public static Duration getDurationKpi(String appkey,String date,String periodType) throws Exception {
		StringBuilder url=new StringBuilder();
		url.append(serverUrl+"/durations");
		url.append("?appkey="+appkey);
		url.append("&start_date="+date);
		url.append("&end_date="+date);
		url.append("&period_type="+periodType);
		String result= HttpUtil.get(url.toString(),headMap);
		return JSONObject.parseObject(result,Duration.class);
	}

	/**
	 * 获取当日和昨日pv、uv信息
	 * @return
	 * @throws Exception
	 */
	public static UmengBaseInfo getAllBaseInfo() throws Exception {
		String url=serverUrl+"/apps/base_data";
		String result= HttpUtil.get(url,headMap);
		return JSONObject.parseObject(result,UmengBaseInfo.class);
	}

	/**
	 * 获取自定义事件计数信息
	 * @param appkey
	 * @param date
	 * @param pageSize
	 * @param pageNo
	 * @return
	 * @throws Exception
	 */
	public static List<UmengEventGroup> getEventGroupKpi(String appkey, String date,int pageSize,int pageNo) throws Exception {
		StringBuilder url=new StringBuilder();
		url.append(serverUrl+"/events/group_list");
		url.append("?appkey="+appkey);
		url.append("&start_date="+date);
		url.append("&end_date="+date);
		url.append("&per_page="+pageSize);
		url.append("&page="+pageNo);
		String result= HttpUtil.get(url.toString(),headMap);
		return JSONArray.parseArray(result, UmengEventGroup.class);
	}

}
