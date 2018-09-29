package com.cmig.future.csportal.module.hap.core;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.data.redis.connection.RedisSentinelConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.JedisPoolConfig;

/**
 * hap框架password取值错误，此处覆盖hap框架代码JedisConnectionFactoryBean
 * @Author:zhangtao
 * @Description:
 * @Date Created in 14:42 2017/10/26.
 * @Modified by zhangtao on 14:42 2017/10/26.
 */
public class CspJedisConnectionFactoryBean implements FactoryBean<JedisConnectionFactory> {
	private boolean useSentinel = false;
	private RedisSentinelConfiguration sentinelConfiguration;
	private String hostName;
	private int port;
	private int database = 0;
	private String password;
	private JedisPoolConfig poolConfig;
	private volatile JedisConnectionFactory cacheObject;

	public CspJedisConnectionFactoryBean() {
	}

	public JedisConnectionFactory getObject() throws Exception {
		if(this.cacheObject == null) {
			synchronized(this) {
				if(this.cacheObject == null) {
					this.doCreate();
				}
			}
		}

		return this.cacheObject;
	}

	private void doCreate() {
		if(this.useSentinel) {
			this.cacheObject = new JedisConnectionFactory(this.sentinelConfiguration);
		} else {
			this.cacheObject = new JedisConnectionFactory();
			this.cacheObject.setHostName(this.hostName);
			this.cacheObject.setPort(this.port);
		}

		if(StringUtils.isNotEmpty(this.password)) {
			this.cacheObject.setPassword(this.password);
		}
		this.cacheObject.setDatabase(this.database);
		this.cacheObject.setUsePool(true);
		this.cacheObject.setPoolConfig(this.poolConfig);
		this.cacheObject.afterPropertiesSet();
	}

	public Class<?> getObjectType() {
		return JedisConnectionFactory.class;
	}

	public boolean isSingleton() {
		return true;
	}

	public boolean isUseSentinel() {
		return this.useSentinel;
	}

	public void setUseSentinel(boolean useSentinel) {
		this.useSentinel = useSentinel;
	}

	public RedisSentinelConfiguration getSentinelConfiguration() {
		return this.sentinelConfiguration;
	}

	public void setSentinelConfiguration(RedisSentinelConfiguration sentinelConfiguration) {
		this.sentinelConfiguration = sentinelConfiguration;
	}

	public String getHostName() {
		return this.hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getDatabase() {
		return this.database;
	}

	public void setDatabase(int database) {
		this.database = database;
	}

	public String getPassword() {
		return this.password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public JedisPoolConfig getPoolConfig() {
		return this.poolConfig;
	}

	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}
}
