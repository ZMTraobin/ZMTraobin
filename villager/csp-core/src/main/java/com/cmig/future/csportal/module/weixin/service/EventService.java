package com.cmig.future.csportal.module.weixin.service;

import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.weixin.entry.Event;
import com.cmig.future.csportal.module.weixin.helper.ChangeType;
import com.cmig.future.csportal.module.weixin.helper.EventType;
import com.cmig.future.csportal.module.weixin.helper.MessageType;
import com.cmig.future.csportal.module.weixin.helper.WorkWxHelper;
import com.cmig.future.csportal.module.weixin.mp.aes.AesException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.Date;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 13:22 2017/12/21.
 * @Modified by zhangtao on 13:22 2017/12/21.
 */
@Service
public class EventService extends BaseService {

	private static final Logger logger = LoggerFactory.getLogger(EventService.class);

	private static final String ELEMENT_TAG_NAME_MSG_TYPE = "MsgType";
	private static final String ELEMENT_TAG_NAME_EVENT_TYPE = "Event";
	private static final String ELEMENT_TAG_NAME_CHANGE_TYPE = "ChangeType";

	@Autowired
	private MessageService messageService;

	public String callBack(Event event) throws Exception {
		String respons = "";
		if (!StringUtils.isEmpty(event.getVerifyEchoStr())) {
			logger.debug("消息服务器连通性确认");
			//需要返回的明文
			String sEchoStr = messageService.validationMessage(event.getAgentNo(), event.getVerifyEchoStr(), event.getVerifyMsgSig(), event.getVerifyTimeStamp(), event.getVerifyNonce());
			respons = sEchoStr;
		} else {
			Element root = getRootElement(event);
			String msgType = getElementValue(root,ELEMENT_TAG_NAME_MSG_TYPE);
			logger.debug("消息类型 {} ", msgType);
			if(MessageType.contains(msgType)) {
				MessageType messageType = MessageType.valueOf(msgType);
				switch (messageType) {
					case text:
						respons = doNormalMessage(event, root);
						break;
					case event:
						respons = doEventMessage(event, root);
						break;
					default:
						break;
				}
			}
		}
		return respons;
	}

	/**
	 * 接收事件消息
	 * @param event
	 * @param root
	 * @return
	 */
	private String doEventMessage(Event event, Element root) {
		String respons=null;
		String eventTypeStr = getElementValue(root,ELEMENT_TAG_NAME_EVENT_TYPE);
		logger.debug("接收事件消息 {} ",eventTypeStr);
		if(EventType.contains(eventTypeStr)) {
			EventType eventType = EventType.valueOf(eventTypeStr);
			switch (eventType) {
				case change_contact:
					respons = doContactEvent(event, root);
					break;
				case location:
					break;
				default:
					break;
			}
		}
		return respons;
	}

	/**
	 * 接收通讯录变更事件
	 * @param event
	 * @param root
	 * @return
	 */
	private String doContactEvent(Event event, Element root) {
		String respons=null;
		String changeTypeStr = getElementValue(root,ELEMENT_TAG_NAME_CHANGE_TYPE);
		logger.debug("接收通讯录变更事件 {} ",changeTypeStr);
		if(ChangeType.contains(changeTypeStr)) {
			ChangeType changeType = ChangeType.valueOf(changeTypeStr);
			switch (changeType) {
				case create_user:
					break;
				case update_user:
					respons = doUpdateUer(event, root);
					break;
				default:
					break;
			}
		}
		return respons;
	}



	/**
	 * 成员更新
	 * @param event
	 * @param root
	 * @return
	 */
	private String doUpdateUer(Event event, Element root) {
		String corpId=getElementValue(root,"ToUserName");
		String userId=getElementValue(root,"UserID");
		String avatar=getElementValue(root,"Avatar");
		logger.debug("更新用户信息 userId {} avatar {}" ,userId,avatar);
		String serverUrl= WorkWxHelper.getCorp(corpId).getServerUrl();
		if(!StringUtils.isEmpty(serverUrl)) {
			String url = serverUrl + "/wechat_incident/upload_head_url";
			try {
				String result=HttpUtil.get(url+"?people_id="+userId+"&head_url="+java.net.URLEncoder.encode(avatar,"utf-8"));
				logger.info("更新用户信息结果 userId {} avatar {} result {} " ,userId,avatar,result);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return null ;
	}

	/**
	 * 接收普通消息
	 * @param event
	 * @param root
	 * @return
	 * @throws AesException
	 */
	private String doNormalMessage(Event event, Element root) throws AesException {
		String Content = getElementValue(root,"Content");
		String ToUserName = getElementValue(root,"ToUserName");
		String replyMsg = messageService.getReplyMsgXml(event.getAgentNo(), Content, ToUserName);
		String nonce = StringUtils.getRandomNum(6);
		String sEncryptMsg = messageService.EncryptMsg(event.getAgentNo(), replyMsg, new Long(new Date().getTime()).toString(), nonce);
		return sEncryptMsg;
	}

	/**
	 * 获取某一个元素的值
	 * @param root
	 * @param tagName
	 * @return
	 */
	private String getElementValue(Element root,String tagName) {
		NodeList changeTypeList = root.getElementsByTagName(tagName);
		if(null!=changeTypeList&&changeTypeList.getLength()>0){
			return changeTypeList.item(0).getTextContent();
		}
		return null;
	}

	/**
	 * 请求体中转XML
	 * @param event
	 * @return
	 * @throws AesException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	private Element getRootElement(Event event) throws AesException, ParserConfigurationException, SAXException, IOException {
		String sMsg = messageService.DecryptMsg(event.getAgentNo(), event.getVerifyMsgSig(), event.getVerifyTimeStamp(), event.getVerifyNonce(), event.getBody());
		logger.debug("after decrypt msg {} ", sMsg);
		return messageService.getRootElement(sMsg);
	}
}
