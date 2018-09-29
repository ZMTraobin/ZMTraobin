package com.cmig.future.csportal.module.weixin.entry;

import java.io.Serializable;
import java.util.Map;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:47 2017/12/5.
 * @Modified by zhangtao on 16:47 2017/12/5.
 */
public class Corp implements Serializable {
	/**
	 * 企业微信id
	 */
	private String corpid;
	/**
	 * 通讯录密钥
	 */
	private String addressSecret;

	private String serverUrl;

	public String getServerUrl() {
		return serverUrl;
	}

	public void setServerUrl(String serverUrl) {
		this.serverUrl = serverUrl;
	}

	/**
	 * 自建应用集合
	 */
	private Map<String,Application> applicationMap;

	public String getCorpid() {
		return corpid;
	}

	public void setCorpid(String corpid) {
		this.corpid = corpid;
	}

	public String getAddressSecret() {
		return addressSecret;
	}

	public void setAddressSecret(String addressSecret) {
		this.addressSecret = addressSecret;
	}

	public Map<String, Application> getApplicationMap() {
		return applicationMap;
	}

	public void setApplicationMap(Map<String, Application> applicationMap) {
		this.applicationMap = applicationMap;
	}

	/**
	 * 自建应用
	 */
	public static class Application  implements Serializable{

		private String corpid;

		/**
		 * 自建应用id
		 */
		private String agentId;

		private String agentNo;
		/**
		 * 自建应用密钥
		 */
		private String agentSecret;

		/**
		 * 接收消息服务器配置-token
		 */
		private String token;
		/**
		 * 接收消息服务器配置-EncodingAESKey
		 */
		private String tncodingAESKey;

		public String getCorpid() {
			return corpid;
		}

		public void setCorpid(String corpid) {
			this.corpid = corpid;
		}

		public String getAgentId() {
			return agentId;
		}

		public void setAgentId(String agentId) {
			this.agentId = agentId;
		}

		public String getAgentNo() {
			return agentNo;
		}

		public void setAgentNo(String agentNo) {
			this.agentNo = agentNo;
		}

		public String getAgentSecret() {
			return agentSecret;
		}

		public void setAgentSecret(String agentSecret) {
			this.agentSecret = agentSecret;
		}

		public String getToken() {
			return token;
		}

		public void setToken(String token) {
			this.token = token;
		}

		public String getTncodingAESKey() {
			return tncodingAESKey;
		}

		public void setTncodingAESKey(String tncodingAESKey) {
			this.tncodingAESKey = tncodingAESKey;
		}
	}
}


