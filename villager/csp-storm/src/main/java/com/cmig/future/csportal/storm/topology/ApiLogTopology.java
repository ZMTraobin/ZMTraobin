package com.cmig.future.csportal.storm.topology;


import backtype.storm.Config;
import backtype.storm.LocalCluster;
import backtype.storm.StormSubmitter;
import backtype.storm.generated.AlreadyAliveException;
import backtype.storm.generated.InvalidTopologyException;
import backtype.storm.spout.SchemeAsMultiScheme;
import backtype.storm.topology.TopologyBuilder;
import backtype.storm.tuple.Fields;
import com.cmig.future.csportal.storm.bolt.ApiLogJson;
import com.cmig.future.csportal.storm.bolt.ApiLogSplitter;
import com.cmig.future.csportal.storm.bolt.ApiRequestCounter;
import storm.kafka.BrokerHosts;
import storm.kafka.KafkaSpout;
import storm.kafka.SpoutConfig;
import storm.kafka.StringScheme;
import storm.kafka.ZkHosts;

import java.util.Arrays;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 19:06 2017/8/29.
 * @Modified by zhangtao on 19:06 2017/8/29.
 */
public class ApiLogTopology {

	public static void main(String[] args) throws AlreadyAliveException, InvalidTopologyException, InterruptedException {
		String zks = "csp-vm-1:2181,csp-vm-2:2181,csp-vm-3:2181";//storm的zookeeper
		String topic = "csp-api-log";//kafka topic
		String zkRoot = "/storm"; // default zookeeper root configuration for storm
		String id = "storms";//读取的status会被存在，/kafkastorm/id下面，所以id类似consumer group

		BrokerHosts brokerHosts = new ZkHosts(zks);
		SpoutConfig spoutConf = new SpoutConfig(brokerHosts, topic, zkRoot, id);
		spoutConf.scheme = new SchemeAsMultiScheme(new StringScheme());
		spoutConf.forceFromStart = false;
		spoutConf.zkServers = Arrays.asList(new String[] {"csp-vm-1,csp-vm-2,csp-vm-3"});
		spoutConf.zkPort = 2181;

		TopologyBuilder builder = new TopologyBuilder();
		builder.setSpout("kafka-reader", new KafkaSpout(spoutConf), 5); // Kafka我们创建了一个5分区的Topic，这里并行度设置为5
		builder.setBolt("apilog-splitter", new ApiLogSplitter(), 2).shuffleGrouping("kafka-reader");
		builder.setBolt("apilogjson-splitter", new ApiLogJson(), 2).shuffleGrouping("apilog-splitter");
		builder.setBolt("apiurl-counter", new ApiRequestCounter()).fieldsGrouping("apilogjson-splitter", new Fields("url"));

		Config conf = new Config();

		String name = ApiLogTopology.class.getSimpleName();
		if (args != null && args.length > 0) {
			// Nimbus host name passed from command line
			conf.put(Config.NIMBUS_HOST, args[0]);
			conf.setNumWorkers(3);
			StormSubmitter.submitTopologyWithProgressBar(name, conf, builder.createTopology());
		} else {
			conf.setMaxTaskParallelism(3);
			LocalCluster cluster = new LocalCluster();
			cluster.submitTopology(name, conf, builder.createTopology());
			Thread.sleep(60000);
			cluster.shutdown();
		}
	}
}
