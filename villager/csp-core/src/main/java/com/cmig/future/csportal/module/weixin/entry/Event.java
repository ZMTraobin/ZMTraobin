package com.cmig.future.csportal.module.weixin.entry;

import java.io.Serializable;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 13:26 2017/12/21.
 * @Modified by zhangtao on 13:26 2017/12/21.
 */
public class Event implements Serializable{

	private String corpId;
	private String agentNo;
	private String verifyMsgSig ;
	private String verifyTimeStamp ;
	private String verifyNonce;
	private String verifyEchoStr;

	private String body;

	public String getCorpId() {
		return corpId;
	}

	public void setCorpId(String corpId) {
		this.corpId = corpId;
	}

	public String getAgentNo() {
		return agentNo;
	}

	public void setAgentNo(String agentNo) {
		this.agentNo = agentNo;
	}

	public String getVerifyMsgSig() {
		return verifyMsgSig;
	}

	public void setVerifyMsgSig(String verifyMsgSig) {
		this.verifyMsgSig = verifyMsgSig;
	}

	public String getVerifyTimeStamp() {
		return verifyTimeStamp;
	}

	public void setVerifyTimeStamp(String verifyTimeStamp) {
		this.verifyTimeStamp = verifyTimeStamp;
	}

	public String getVerifyNonce() {
		return verifyNonce;
	}

	public void setVerifyNonce(String verifyNonce) {
		this.verifyNonce = verifyNonce;
	}

	public String getVerifyEchoStr() {
		return verifyEchoStr;
	}

	public void setVerifyEchoStr(String verifyEchoStr) {
		this.verifyEchoStr = verifyEchoStr;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
