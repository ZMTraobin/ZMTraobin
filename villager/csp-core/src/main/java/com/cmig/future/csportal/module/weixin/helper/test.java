package com.cmig.future.csportal.module.weixin.helper;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 16:36 2017/12/21.
 * @Modified by zhangtao on 16:36 2017/12/21.
 */
public class test {
	public static void main(String[] args) {
		String msgType = "text";
		System.out.println(MessageType.contains(msgType));

		MessageType messageType = MessageType.valueOf(msgType);
	}
}
