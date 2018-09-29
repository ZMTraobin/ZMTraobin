package com.cmig.future.csportal.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 17:38 2017/8/31.
 * @Modified by zhangtao on 17:38 2017/8/31.
 */
public class ApiLogJson extends BaseRichBolt {
	private static final Log LOG = LogFactory.getLog(ApiLogJson.class);
	private static final long serialVersionUID = 886149197481637894L;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String line = input.getString(0);
		LOG.info("RECV[kafka -> logJsonString] " + line);
		JSONObject jsonObject=JSONObject.parseObject(line);
		if(null!=jsonObject){
			String createTime=jsonObject.get("createTime")==null?"":jsonObject.get("createTime").toString();
			String url=jsonObject.get("requestUri")==null?"":jsonObject.get("requestUri").toString();
			String appUserId=jsonObject.get("appUserId")==null?"":jsonObject.get("appUserId").toString();
			String deviceType=jsonObject.get("deviceType")==null?"":jsonObject.get("deviceType").toString();
			String deviceModel=jsonObject.get("deviceModel")==null?"":jsonObject.get("deviceModel").toString();
			String equipmentModel=jsonObject.get("equipmentModel")==null?"":jsonObject.get("equipmentModel").toString();
			if(StringUtils.isNotEmpty(url)){
				collector.emit(input, new Values(createTime,appUserId,deviceType,deviceModel,equipmentModel,url));
			}
		}
		collector.ack(input);//标识这条消息被正确处理
	}



	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("createTime","appUserId","deviceType","deviceModel","equipmentModel","url"));
	}
}
