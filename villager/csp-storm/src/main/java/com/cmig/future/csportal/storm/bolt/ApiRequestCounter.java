package com.cmig.future.csportal.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import com.cmig.future.csportal.storm.db.MyDbUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 15:49 2017/8/31.
 * @Modified by zhangtao on 15:49 2017/8/31.
 */
public class ApiRequestCounter extends BaseRichBolt {

	private static final Log LOG = LogFactory.getLog(ApiRequestCounter.class);
	private static final long serialVersionUID = 886149197481637894L;
	private OutputCollector collector;
	private Map<String, AtomicInteger> counterMap;

	@Override
	public void prepare(Map stormConf, TopologyContext context,OutputCollector collector) {
		this.collector = collector;
		this.counterMap = new HashMap<String, AtomicInteger>();
	}

	@Override
	public void execute(Tuple input) {
		LOG.debug("RECV[splitter -> result] "+input);
		String createTime=input.getString(0);
		String appUserId=input.getString(1);
		String deviceType=input.getString(2);
		String deviceModel=input.getString(3);
		String equipmentModel=input.getString(4);
		String url=input.getString(5);

		AtomicInteger ai = this.counterMap.get(url);
		if(ai == null) {
			ai = new AtomicInteger();
			this.counterMap.put(url, ai);
		}
		ai.addAndGet(1);
		collector.emit(input, new Values(url,ai.get()));
		collector.ack(input);

		try {
			MyDbUtils.update(MyDbUtils.CSP_API_INSERT_LOG,appUserId,deviceType,deviceModel,equipmentModel,url,createTime);
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public void cleanup() {
		LOG.info("The final result:");
		Iterator<Map.Entry<String, AtomicInteger>> iter = this.counterMap.entrySet().iterator();
		while(iter.hasNext()) {
			Map.Entry<String, AtomicInteger> entry = iter.next();
			LOG.info(entry.getKey() + "\t:\t" + entry.getValue().get());
		}

	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("url", "count"));
	}
}