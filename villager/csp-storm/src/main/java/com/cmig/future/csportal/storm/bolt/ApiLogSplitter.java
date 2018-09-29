package com.cmig.future.csportal.storm.bolt;

import backtype.storm.task.OutputCollector;
import backtype.storm.task.TopologyContext;
import backtype.storm.topology.OutputFieldsDeclarer;
import backtype.storm.topology.base.BaseRichBolt;
import backtype.storm.tuple.Fields;
import backtype.storm.tuple.Tuple;
import backtype.storm.tuple.Values;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 17:38 2017/8/31.
 * @Modified by zhangtao on 17:38 2017/8/31.
 */
public class ApiLogSplitter extends BaseRichBolt {
	private static final Log LOG = LogFactory.getLog(ApiLogSplitter.class);
	private static final long serialVersionUID = 886149197481637894L;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String line = input.getString(0);
		LOG.info("RECV[kafka -> splitter] " + line);
		String[] words = line.split("logJson->");
		if(words!=null&&words.length>1){
			String logJsonString=words[1];
			collector.emit(input, new Values(logJsonString));
		}
		collector.ack(input);//标识这条消息被正确处理
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("logJsonString"));
	}
}
