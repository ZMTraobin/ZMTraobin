package com.cmig.future.csportal.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
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
public class AcsNumCounter extends BaseRichBolt {

	private static final Log LOG = LogFactory.getLog(AcsNumCounter.class);
	private static final long serialVersionUID = 886149197481637894L;
	private OutputCollector collector;
	private Map<String, AtomicInteger> counterMap;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
	                    OutputCollector collector) {
		this.collector = collector;
		this.counterMap = new HashMap<String, AtomicInteger>();
	}

	@Override
	public void execute(Tuple input) {
		LOG.debug(input);
		String time = input.getString(0);
		String num = input.getString(1);
		LOG.info("RECV[splitter -> num] " + time + " : " + num);
		collector.ack(input);

		try {
			MyDbUtils.update(MyDbUtils.INSERT_LOG,time,num);
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
		declarer.declare(new Fields("time", "count"));
	}
}