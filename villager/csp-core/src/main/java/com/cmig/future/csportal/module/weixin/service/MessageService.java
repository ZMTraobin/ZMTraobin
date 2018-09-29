package com.cmig.future.csportal.module.weixin.service;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.weixin.WorkWxException;
import com.cmig.future.csportal.module.weixin.entry.Corp;
import com.cmig.future.csportal.module.weixin.entry.Message;
import com.cmig.future.csportal.module.weixin.helper.MessageType;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.cmig.future.csportal.module.weixin.mp.aes.AesException;
import com.cmig.future.csportal.module.weixin.mp.aes.WXBizMsgCrypt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.Date;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:23 2017/12/5.
 * @Modified by zhangtao on 16:23 2017/12/5.
 */
@Service
public class MessageService extends BaseService{
	private static final Logger logger= LoggerFactory.getLogger(MessageService.class);

	/**
	 * 接收消息服务器配置
	 * @param agentNo
	 * @param sVerifyEchoStr
	 * @param sVerifyMsgSig
	 * @param sVerifyTimeStamp
	 * @param sVerifyNonce
	 * @return
	 * @throws AesException
	 */
	public String validationMessage(String agentNo,String sVerifyEchoStr,String sVerifyMsgSig,String sVerifyTimeStamp,String sVerifyNonce) throws AesException {
		if (!StringUtils.isEmpty(sVerifyEchoStr)) {
			WXBizMsgCrypt wxcpt = WorkWxHelper.getWxBizMsgCrypt(agentNo);
			String sEchoStr = wxcpt.VerifyURL(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sVerifyEchoStr);
			logger.debug("verifyurl echostr {} " , sEchoStr);
			// 验证URL成功，将sEchoStr返回
			return sEchoStr;
		}
		return null;
	}

	/**
	 * 给全员推送文本消息
	 * @param agentNo
	 * @param content
	 * @throws Exception
	 */
	public void sendTextToAll(String agentNo, String content,int safe) throws Exception {
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		String access_token=getAccessToken(agent.getCorpid(), agent.getAgentSecret());
		String url =WORK_WX_SERVER_URL+"/cgi-bin/message/send?access_token="+access_token;
		Message message=new Message();
		message.setTouser("@all");
		message.setMsgtype(MessageType.text.getCode());
		message.setAgentid(new Integer(agent.getAgentId()));
		Message.TextBean textBean=new Message.TextBean();
		textBean.setContent(content);
		message.setText(textBean);
		message.setSafe(safe);

		String result= HttpUtil.post(url, JSONObject.toJSONString(message));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 给指定成员发送文本消息
	 * @param agentNo
	 * @param touser
	 * @param toparty
	 * @param totag
	 * @param content
	 * @param safe
	 * @throws Exception
	 */
	public void sendText(String agentNo,String touser, String toparty,String totag,String content,int safe) throws Exception {
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		String access_token=getAccessToken(agent.getCorpid(), agent.getAgentSecret());

		String url =WORK_WX_SERVER_URL+"/cgi-bin/message/send?access_token="+access_token;
		Message message=new Message();
		message.setTouser(touser);
		message.setTouser(touser);
		message.setToparty(toparty);
		message.setTotag(totag);
		message.setMsgtype(MessageType.text.getCode());
		message.setAgentid(new Integer(agent.getAgentId()));
		Message.TextBean textBean=new Message.TextBean();
		textBean.setContent(content);
		message.setText(textBean);
		message.setSafe(safe);

		String result= HttpUtil.post(url, JSONObject.toJSONString(message));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	/**
	 * 给指定成员发送文本卡片消息
	 * @param agentNo
	 * @param touser
	 * @param toparty
	 * @param totag
	 * @param title
	 * @param description
	 * @param detailUrl
	 * @param btntxt
	 * @throws Exception
	 */
	public void sendTextCard(String agentNo,String touser, String toparty,String totag,String title,String description,String detailUrl,String btntxt) throws Exception {
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		String access_token=getAccessToken(agent.getCorpid(), agent.getAgentSecret());
		String url =WORK_WX_SERVER_URL+"/cgi-bin/message/send?access_token="+access_token;
		Message message=new Message();
		message.setTouser(touser);
		message.setTouser(touser);
		message.setToparty(toparty);
		message.setTotag(totag);
		message.setMsgtype(MessageType.textcard.getCode());
		message.setAgentid(new Integer(agent.getAgentId()));
		Message.TextCard textCard=new Message.TextCard();
		textCard.setTitle(title);
		textCard.setDescription(description);
		textCard.setUrl(detailUrl);
		textCard.setBtntxt(btntxt);
		message.setTextcard(textCard);

		String result= HttpUtil.post(url, JSONObject.toJSONString(message));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}

	public void sendNews(String agentNo,Message message) throws Exception {
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		String access_token=getAccessToken(agent.getCorpid(), agent.getAgentSecret());
		String url =WORK_WX_SERVER_URL+"/cgi-bin/message/send?access_token="+access_token;

		message.setMsgtype(MessageType.news.getCode());
		String result= HttpUtil.post(url, JSONObject.toJSONString(message));
		JSONObject jsonObject=JSONObject.parseObject(result);
		if(!SUCCESS_CODE.equals(jsonObject.get("errcode").toString())){
			throw new WorkWxException(jsonObject.get("errcode").toString());
		}
	}


	/**
	 * 消息解密
	 * @param agentNo
	 * @param sVerifyMsgSig
	 * @param sVerifyTimeStamp
	 * @param sVerifyNonce
	 * @param sReqData
	 * @return
	 * @throws AesException
	 */
	public String DecryptMsg(String agentNo,String sVerifyMsgSig,String sVerifyTimeStamp,String sVerifyNonce,String sReqData) throws AesException {
		WXBizMsgCrypt wxcpt = WorkWxHelper.getWxBizMsgCrypt(agentNo);
		String sMsg = wxcpt.DecryptMsg(sVerifyMsgSig, sVerifyTimeStamp, sVerifyNonce, sReqData);
		return sMsg;
	}

	/**
	 * 消息加密
	 * @param agentNo
	 * @param replyMsg
	 * @param timeStamp
	 * @param nonce
	 * @return
	 * @throws AesException
	 */
	public String EncryptMsg(String agentNo,String replyMsg,String timeStamp,String nonce) throws AesException {
		WXBizMsgCrypt wxcpt = WorkWxHelper.getWxBizMsgCrypt(agentNo);
		return wxcpt.EncryptMsg(replyMsg,timeStamp,nonce);
	}

	/**
	 * 解析xml
	 * @param sMsg
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public Element getRootElement(String sMsg) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		StringReader sr = new StringReader(sMsg);
		InputSource is = new InputSource(sr);
		Document document = db.parse(is);

		return document.getDocumentElement();
	}

	/**
	 *
	 * @param agentNo
	 * @param content
	 * @param toUserName
	 * @return
	 */
	public String getReplyMsgXml(String agentNo, String content, String toUserName) {
		Corp.Application agent=WorkWxHelper.getAgentByNo(agentNo);
		StringBuffer replyMsg=new StringBuffer();
		replyMsg.append("<xml>");
		replyMsg.append("<ToUserName><![CDATA["+ toUserName +"]]></ToUserName>");
		replyMsg.append("<FromUserName><![CDATA["+agent.getAgentId()+"]]></FromUserName> ");
		replyMsg.append("<CreateTime>"+new Date().getTime()+"</CreateTime>");
		replyMsg.append("<MsgType><![CDATA[text]]></MsgType>");
		replyMsg.append("<Content><![CDATA["+ content +"]]></Content>");
		replyMsg.append("</xml>");
		return replyMsg.toString();
	}

}
