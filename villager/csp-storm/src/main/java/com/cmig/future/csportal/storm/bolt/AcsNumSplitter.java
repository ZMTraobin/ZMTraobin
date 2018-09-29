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
 * @Date Created in 15:44 2017/8/31.
 * @Modified by zhangtao on 15:44 2017/8/31.
 */
public class AcsNumSplitter extends BaseRichBolt{

	private static final Log LOG = LogFactory.getLog(AcsNumSplitter.class);
	private static final long serialVersionUID = 886149197481637894L;
	private OutputCollector collector;

	@Override
	public void prepare(Map stormConf, TopologyContext context,
	                    OutputCollector collector) {
		this.collector = collector;
	}

	@Override
	public void execute(Tuple input) {
		String line = input.getString(0);
		LOG.info("RECV[kafka -> splitter] " + line);
		String[] words = line.split("\\s+");
		collector.emit(input, new Values(words[0], words[1].trim()));
		collector.ack(input);
	}

	@Override
	public void declareOutputFields(OutputFieldsDeclarer declarer) {
		declarer.declare(new Fields("time", "count"));
	}

}
