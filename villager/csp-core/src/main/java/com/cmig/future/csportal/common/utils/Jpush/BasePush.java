package com.cmig.future.csportal.common.utils.Jpush;

import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;
import cn.jpush.api.push.model.notification.PlatformNotification;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.Constants;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import com.cmig.future.csportal.module.sys.notifyrecord.service.ISysNotifyRecordService;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by zhangtao107@126.com on 2016/11/28.
 */
public class BasePush {
    private static Log log = LogFactory.getLog(BasePush.class);
    private static boolean apnsProduction = true;// 仅对ios有用。生产环境：true；开发环境：false；
    private static Options options = Options.sendno();
    static{
        options.setApnsProduction(apnsProduction);
    }
    private static ISysNotifyRecordService sysNotifyRecordService= SpringContextHolder.getBean("sysNotifyRecordServiceImpl");

    //创建一个可重用固定线程数的线程池
    static ExecutorService pool = Executors.newFixedThreadPool(2);
    /**
     * 极光推送-异步
     *
     * @param context 推送内容，不允许为空，最大长度100。
     * @param mobile  推送用户的mobile，不允许为null。
     */
    protected static void pushAlert(PushClient jpushClient, String context, String... mobile) {
        pushAlertWithMessage(jpushClient,context,null,mobile);
    }

    /**
     * 极光推送-异步
     *
     * @param context 推送内容，不允许为空，最大长度100。
     * @param message message，允许为null。
     * @param mobile 推送用户的mobile，不允许为null。
     */
    protected static void pushAlertWithMessage(final PushClient jpushClient,final String context, final String message,final String... mobile) {
        Runnable r= new Runnable(){
            public void run() {
                pushAlertWithMessageSyn(jpushClient,context,message,mobile);
            }
        };
        pool.execute(r);
    }


    /**
     * 极光推送-同步
     *
     * @param context 推送内容，不允许为空，最大长度100。
     * @param contentExt contentExt，允许为null。
     * @param mobile  推送用户的mobile，不允许为null。
     */
    protected static void pushAlertWithMessageSyn(PushClient jpushClient,String context, String contentExt,String... mobile) {
        String sendContext = context;
        if (StringUtils.isEmpty(sendContext) || StringUtils.isEmpty(mobile)) {
            return;
        }

        PushPayload payload =null;
        if(StringUtils.isEmpty(contentExt)){
            payload = buildPushObject_all_alias_alert(substringContext(sendContext), mobile);
        }else{
            payload = buildPushObject_all_alias_alertWithMessage(substringContext(sendContext),contentExt, mobile);
        }
        push(jpushClient, payload);
    }


    /**
     * 极光推送-记录状态-异步
     * @param sysNotifyRecord
     */
    protected static void pushAlertWithMessage(final PushClient jpushClient,final SysNotifyRecord sysNotifyRecord) {
        Runnable r= new Runnable(){
            public void run() {
                pushAlertWithMessageSyn(jpushClient,sysNotifyRecord);
            }
        };
        pool.execute(r);
    }

    /**
     * 极光推送-记录状态-同步
     * @param sysNotifyRecord
     */
    protected static void pushAlertWithMessageSyn(PushClient jpushClient,SysNotifyRecord sysNotifyRecord) {
        if(null==sysNotifyRecord) return ;
        String sendContext = sysNotifyRecord.getContent();
        if (StringUtils.isEmpty(sendContext) || StringUtils.isEmpty(sysNotifyRecord.getReceiverInfo())) {
            return;
        }

        String contentExt=sysNotifyRecord.getContentExt()==null?"":sysNotifyRecord.getContentExt();
        PushPayload payload = buildPushObject_all_alias_alertWithMessage(substringContext(sendContext),contentExt, sysNotifyRecord.getReceiverInfo());
        PushResult rlt=null;
        try {
            rlt= push(jpushClient, payload);
        }catch (DataWarnningException e){
            sysNotifyRecord.setRemark(e.getMessage());
        }
        savePushRssult(rlt, sysNotifyRecord);
    }


    /**
     * 发送内容长度控制
     *
     * @param sendContext
     * @return
     */
    private static String substringContext(String sendContext) {
        /*sendContext+="【"+Global.getConfig("productName")+"】";*/
        if (sendContext.length() > 100) {
            sendContext = sendContext.substring(0, 100);
        }
        return sendContext;
    }

    private static PushResult push(PushClient jpushClient, PushPayload payload){
        log.info("极光推送内容：" + payload.toJSON());
        try {
            PushResult rlt = jpushClient.sendPush(payload);
            log.info("极光推送结果成功：" + rlt.toString());
            return rlt;
        } catch (APIConnectionException e) {
            log.error("极光推送结果失败：Connection error, should retry later", e);
            throw new DataWarnningException("连接极光服务器失败，请稍候重试。");
        } catch (APIRequestException e) {
            log.error("极光推送结果失败：Should review the error, and fix the request", e);
            log.info("HTTP Status: " + e.getStatus());
            log.info("Error Code: " + e.getErrorCode());
            log.info("Error Message: " + e.getErrorMessage());
            if(1011==e.getErrorCode()) {
                throw new DataWarnningException("未能匹配到用户");
            }else{
                throw new DataWarnningException("其他异常");
            }
        } catch (Exception e) {
            log.error("极光推送结果失败：" + e.getMessage());
        }
        return null ;
    }

    /**
     * 通知 to all
     * @param context
     * @return
     */
    private static PushPayload buildPushObject_all_all_alert(String context) {
        return PushPayload.alertAll(context);
    }

    /**
     * 通知+alias
     * @param context
     * @param alias
     * @return
     */
    private static PushPayload buildPushObject_all_alias_alert(String context, String... alias) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.alert(context))
                .setOptions(options)
                .build();
    }


    private static PushPayload buildPushObject_android_and_ios(String context, String message,String title,String... alias) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert(context)
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle(title).build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1).build())
                        .build())
                .setMessage(Message.content(message))
                .setOptions(options)
                .build();
    }

    /**
     * 通知+消息+alias
     * @param alert
     * @param contentExt
     * @param alias
     * @return
     */
    private static PushPayload buildPushObject_all_alias_alertWithMessage(String alert, String contentExt, String... alias) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setAlert(alert)
                                .addExtra("contentExt", contentExt)
                                .build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(alert)
                                .addExtra("contentExt", contentExt)
                                .setBadge(0)
                                .build())
                        .build())
                //.setMessage(Message.content(jsonObject.toString()))
                .setOptions(options)
                .build();
    }


    /**
     * 消息+alias
     *
     * @param message
     * @param alias
     * @return
     */
    private static PushPayload buildPushObject_all_alias_message(String message, String... alias) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setMessage(Message.newBuilder()
                        .setMsgContent(message)
                        .build())
                .setOptions(options)
                .build();
    }


    /**
     * 此方法描述的是：构建推送对象：平台是 Android，目标是 tag 为 "tag1" 的设备，内容是 Android 通知 ALERT，并且标题为 TITLE。
     *
     * @return PushPayload
     * @author:zhangtao107@126.com
     */
    private static PushPayload buildPushObject_android_tag_alertWithTitle(String tag, String title) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android())
                .setAudience(Audience.tag(tag))
                .setNotification(Notification.android(PlatformNotification.ALERT, title, null))
                .build();
    }

    /**
     * 此方法描述的是：构建推送对象：平台是 iOS，推送目标是 "tag1", "tag_all" 的交集，推送内容同时包括通知与消息 - 通知信息是 ALERT，角标数字为 5，通知声音为 "happy"，并且附加字段 from = "JPush"；消息内容是 MSG_CONTENT。通知是 APNs 推送通道的，消息是 JPush 应用内消息通道的。APNs 的推送环境是“生产”（如果不显式设置的话，Library 会默认指定为开发）
     *
     * @return PushPayload
     * @author:zhangtao107@126.com
     */
    private static PushPayload buildPushObject_ios_tagAnd_alertWithExtrasAndMessage(String sound, String from, String msgContent, String... tags) {
        return PushPayload.newBuilder()
                .setPlatform(Platform.ios())
                .setAudience(Audience.tag_and(tags))
                .setNotification(Notification.newBuilder()
                        .addPlatformNotification(IosNotification.newBuilder()
                                .setAlert(PlatformNotification.ALERT)
                                .setBadge(5)
                                .setSound(sound)
                                .addExtra("from", from)
                                .build())
                        .build())
                .setMessage(Message.content(msgContent))
                .setOptions(Options.newBuilder()
                        .setApnsProduction(true)
                        .build())
                .build();
    }

    /**
     * 此方法描述的是：构建推送对象：平台是 Andorid 与 iOS，推送目标是 （"tag1" 与 "tag2" 的并集）且（"alias1" 与 "alias2" 的并集），推送内容是 - 内容为 MSG_CONTENT 的消息，并且附加字段 from = JPush。
     *
     * @return PushPayload
     * @author:zhangtao107@126.com
     */
    private static PushPayload buildPushObject_ios_audienceMore_messageWithExtras() {
        return PushPayload.newBuilder()
                .setPlatform(Platform.android_ios())
                .setAudience(Audience.newBuilder()
                        .addAudienceTarget(AudienceTarget.tag("tag1", "tag2"))
                        .addAudienceTarget(AudienceTarget.alias("alias1", "alias2"))
                        .build())
                .setMessage(Message.newBuilder()
                        .setMsgContent("MSG_CONTENT")
                        .addExtra("from", "JPush")
                        .build())
                .build();
    }


    private static void savePushRssult(PushResult rlt, SysNotifyRecord sysNotifyRecord) {
        if(null!=rlt){
            sysNotifyRecord.setStatus(Constants.NOTIFY_STATUS_SUCCESS);
            sysNotifyRecord.setTimes(sysNotifyRecord.getTimes()+1);
            sysNotifyRecord.setMsgId(new Long(rlt.msg_id).toString());
            sysNotifyRecord.setSendNo(new Integer(rlt.sendno).toString());
            sysNotifyRecordService.save(sysNotifyRecord);
        }else{
            sysNotifyRecord.setStatus(Constants.NOTIFY_STATUS_FAIL);
            sysNotifyRecordService.save(sysNotifyRecord);
        }
    }

}
