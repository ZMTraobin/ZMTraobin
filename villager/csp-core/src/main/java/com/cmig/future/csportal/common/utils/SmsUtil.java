package com.cmig.future.csportal.common.utils;


import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.module.base.entity.SmsCodeEntity;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.sys.notifyrecord.service.ISysNotifyRecordService;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class SmsUtil extends SmsSender {
	
	private static final Logger log = LoggerFactory.getLogger(SmsUtil.class);
	public static final String SMS_VALIDATE_CODE = "sms.validate.code.";

	private static int timelimit;
	private static String timelimitMin;
	private static int TIMES_SMS_VALIDATE_CODE_PRE_24H;
	private static int TIMES_SMS_VALIDATE_CODE_PRE_24H_IP;
	private static final String TIMES_SMS_VALIDATE_CODE_PRE_24H_KEY="times.sms.validate.code.pre.24H:";
	static {
		// 短信生命期
		String _timelimit = Global.getConfig("sms.timelimit");
		if (_timelimit == null) {
			timelimit = 120;
			timelimitMin="2分钟";
		} else {
			timelimit = Integer.parseInt(_timelimit);
			int minute=timelimit/60;
			timelimitMin=minute+"分钟";
		}

		String temp = Global.getConfig("times.sms.validate.code.pre.24H","10");
		TIMES_SMS_VALIDATE_CODE_PRE_24H=Integer.parseInt(temp);
		TIMES_SMS_VALIDATE_CODE_PRE_24H_IP=TIMES_SMS_VALIDATE_CODE_PRE_24H*2;

	}

	private static boolean verifySMS(String telnoKey) {
		return JedisUtils.getObject(telnoKey) == null;
	}

	/**
	 * 验证码种类
	 */
	public static String SMS_CODE_COMMON="common";//历史数据
	public static String SMS_CODE_REGISTER="register";//注册
	public static String SMS_CODE_CARD_AUTH="cardAuth";//绑卡
	public static String SMS_CODE_CARD_UNBUNDLED="cardUnbundled";//解绑
	public static String SMS_CODE_FORGET_PASSWORD="forgetPassword";//忘记密码
	public static String SMS_CODE_FORGET_PASSWORD_MGT="forgetPasswordMgt";//忘记密码
	public static String SMS_CODE_MGT_OWNER_AUTH="mgtOwnerAuth";//业主认证
	public static String SMS_CODE_OPENID_LOGIN="openIdLogin";//第三方登录/注册
	public static String SMS_CODE_NEXT_OPERATE="nextOperate_";//短信验证码下一步操作
	public static String SMS_CODE_UUID_BIND_MOBILE="uuidBindMobile";//第三方uuid绑定已注册手机号

    private static ISysNotifyRecordService sysNotifyRecordService= SpringContextHolder.getBean("sysNotifyRecordServiceImpl");
    //创建一个可重用固定线程数的线程池
    static ExecutorService pool = Executors.newFixedThreadPool(2);

    /**
     * 批量发送短信-异步
     * @param content
     * @param mobiles
     */
    public static void send(OpenAppInfo openAppInfo, String content, String... mobiles) {
        if(mobiles != null && mobiles.length != 0) {
            String[] arr$ = mobiles;
            for(int i = 0; i < mobiles.length; ++i) {
                String mobile = arr$[i];
                Runnable runnable= () -> {
	                boolean result=sendSyn(openAppInfo.getAppName(),content,mobile);
                };
                pool.execute(runnable);
            }
        }
    }

    /**
     * 批量发送短信-异步
     * @param content
     * @param mobiles
     */
    public static void send(String content,String... mobiles) {
        if(mobiles != null && mobiles.length != 0) {
            String[] arr$ = mobiles;
            for(int i = 0; i < mobiles.length; ++i) {
                String mobile = arr$[i];
                Runnable runnable= () -> {
	                boolean result=sendSyn(content,mobile);
                };
                pool.execute(runnable);
            }
        }
    }

    /**
     * 单条发送短信-同步
     * @param content
     * @param mobile
     * @return
     */
    public static boolean sendSyn(String content,String mobile) {
        return sendSyn(null,content,mobile);
    }

    /**
     * 单条发送短信-同步
     * @param content
     * @param mobile
     * @return
     */
    public static boolean sendSyn(String appName,String content,String mobile) {
        /*SysNotifyRecord sysNotifyRecord = new SysNotifyRecord();
        sysNotifyRecord.setMessageType(NotifyMessageTypeEnum.SMS.getCode());
        sysNotifyRecord.setContent(content);
        sysNotifyRecord.setReceiverInfo(mobile);
        sysNotifyRecord.setSourceSystem(appName);
        sysNotifyRecordService.save(sysNotifyRecord);
        String result= sendMsg(mobile,content);
        if(RESULT_OK.equals(result)){
            sysNotifyRecord.setStatus(Constants.NOTIFY_STATUS_SUCCESS);
        }else{
            sysNotifyRecord.setStatus(Constants.NOTIFY_STATUS_FAIL);
        }
        sysNotifyRecordService.save(sysNotifyRecord);*/
        boolean result= sendMsg(mobile,content);
        return result;
    }

	/**
	 * 随机数
	 * @param count
	 * @return
	 */
    public static String authCode(int count){
        StringBuffer sb = new StringBuffer();
        String str = "0123456789";
        Random r = new Random();
        for(int i=0;i<count;i++){
            int num = r.nextInt(str.length());
            sb.append(str.charAt(num));
            str = str.replace((str.charAt(num)+""), "");
        }
        return sb.toString();
    }


	/**
	* 此方法描述的是：校验短信验证码
	* @author:zhangtao107@126.com
	* @param mobile
	* @param smsCodeInput
	* @throws DataWarnningException void
	*/
	public static void checkSmsCode(String mobile,String key, String smsCodeInput) throws DataWarnningException {

		SmsCodeEntity smsCodeEntity = (SmsCodeEntity) JedisUtils.getObject(key);
		if (null==smsCodeEntity||smsCodeEntity.getSmsCode().isEmpty()) {
			log.info("从redis获取验证码为空");
			throw new DataWarnningException("短信验证码错误或已失效，请重新获取验证码");
		}
		
		if(!mobile.equals(smsCodeEntity.getMobile())){
			throw new DataWarnningException("验证码错误");
		}

		if(smsCodeInput.equals(smsCodeEntity.getSmsCode())){
			log.info("短信验证码正确，删除redis中的值");
			JedisUtils.delObject(key);
		}else {
			log.info("短信验证码已失效或不正确");
			throw new DataWarnningException("验证码错误");
		}
	}

	/**
	 * 该方法描述的是：dynamicReplaceMsg 动态替换消息
	 * @param content
	 * @param strs
	 * @return String
	 * 2016年4月27日
	 * @author:zhengshujun
	 */
	public static String dynamicReplaceMsg(String content,Object[] strs){
		String newContent = String.format(content, strs);
		return newContent;
	}

	/**
	 * 通用发短信验证码
	 * 限制每个手机号24小时内发送次数
	 * 限制每个ip24小时内发送次数
	 * 限制相同用途验证码操作频率
	 *
	 * @param type
	 * @param mobile
	 */
	public static void sendSmsCode(String ip,String type, String mobile) {
		String telnoKey = SmsUtil.SMS_VALIDATE_CODE + type + "." + mobile;
		boolean res = verifySMS(telnoKey);
		if (!res) {
			throw new DataWarnningException("操作频繁，请您稍候再试");
		}

		if(null!=JedisUtils.get(TIMES_SMS_VALIDATE_CODE_PRE_24H_KEY+ip)
				&&new Integer(JedisUtils.get(TIMES_SMS_VALIDATE_CODE_PRE_24H_KEY+ip)).intValue()>=TIMES_SMS_VALIDATE_CODE_PRE_24H_IP){
			throw new ServiceException(RestApiExceptionEnums.EXCEEDED_LIMIT_TIMES_SMS_VALIDATE_CODE_PRE_24H);
		}

		if(null!=JedisUtils.get(TIMES_SMS_VALIDATE_CODE_PRE_24H_KEY+mobile)
				&&new Integer(JedisUtils.get(TIMES_SMS_VALIDATE_CODE_PRE_24H_KEY+mobile)).intValue()>=TIMES_SMS_VALIDATE_CODE_PRE_24H){
			throw new ServiceException(RestApiExceptionEnums.EXCEEDED_LIMIT_TIMES_SMS_VALIDATE_CODE_PRE_24H);
		}

		// 短信对象
		String code = SmsUtil.authCode(6);
		SmsCodeEntity smsCodeEntity = new SmsCodeEntity();
		smsCodeEntity.setSmsCode(code);
		smsCodeEntity.setMobile(mobile);
		smsCodeEntity.setCreateTime(new Date());
		JedisUtils.setObject(telnoKey, smsCodeEntity, timelimit);

		// 短信模版
		String message;
		if (null != type && SmsUtil.SMS_CODE_REGISTER.equals(type)) {
			message = "叮咚，欢迎加入"+Global.getProductName()+"大家庭，这是您的注册验证码"+code+"，"+getTimelimitMin()+"内有效，请勿向任何人泄露。如非本人操作，请忽略。";
		} else {
			message = "您的验证码为："+code+"，有效时间"+getTimelimitMin()+"。为了账号安全，请勿将验证码泄露给其他人。如非本人操作，请忽略。";
		}

		Runnable runnable= () -> {
			boolean result=sendSyn(message,mobile);
			if(result){
				//每发送成功一次，计数器加1
				JedisUtils.incr(TIMES_SMS_VALIDATE_CODE_PRE_24H_KEY+mobile,24*3600);
				JedisUtils.incr(TIMES_SMS_VALIDATE_CODE_PRE_24H_KEY+ip,24*3600);
			}
		};
		pool.execute(runnable);
	}


	public static int getTimelimit() {
		return timelimit;
	}

	public static String getTimelimitMin() {
		return timelimitMin;
	}

	public static void main(String[] args) {
		
		//String msg = "\r\n" +"55668";
		//System.out.println(authCode(4));
		// 13430968254,18501972564  13028897374  18320881027
		//SmsUtil su = new SmsUtil("13430968254", "6782",msg);
		//su.start();
		String content = "说电话费%s我企鹅王%s";
		String str[] = {"qq","qqq"};
		System.out.println(dynamicReplaceMsg(content, str));
	}


}
