package com.cmig.future.csportal.common.utils.Jpush;

import cn.jpush.api.push.PushClient;
import com.cmig.future.csportal.module.sys.notifyrecord.dto.SysNotifyRecord;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 此类描述的是：中民居家app极光推送
 * https://docs.jiguang.cn/jpush/guideline/intro/
 *
 * @author:zhangtao107@126.com
 * @version: 2016-5-5 上午11:25:02
 */

public class JPushUtils extends BasePush {
    private static Log log = LogFactory.getLog(JPushUtils.class);
    private static String master = "116cecc304efede2be87eedc";
    private static String appkey = "91433496bd0ec5af987808db";
    private static PushClient pushClient=new PushClient(master, appkey);

    /**
     * 极光推送-异步
     *
     * @param context 推送内容，不允许为空，最大长度100。
     * @param mobile  推送用户的mobile，不允许为null。
     */
    public static void pushAlert(String context, String... mobile) {
        pushAlert(pushClient,context,mobile);
    }

    /**
     * 极光推送-异步
     *
     * @param context 推送内容，不允许为空，最大长度100。
     * @param message message，允许为null。
     * @param mobile 推送用户的mobile，不允许为null。
     */
    public static void pushAlertWithMessage(final String context, final String message,final String... mobile) {
        pushAlertWithMessage(pushClient,context,null,mobile);
    }

    /**
     * 极光推送-同步
     *
     * @param context 推送内容，不允许为空，最大长度100。
     * @param message message，允许为null。
     * @param mobile  推送用户的mobile，不允许为null。
     */
    public static void pushAlertWithMessageSyn(String context, String message,String... mobile) {
        pushAlertWithMessageSyn(pushClient,context,message,mobile);
    }


    /**
     * 极光推送-记录状态-异步
     * @param sysNotifyRecord
     */
    public static void pushAlertWithMessage(final SysNotifyRecord sysNotifyRecord) {
        pushAlertWithMessage(pushClient,sysNotifyRecord);
    }

    /**
     * 极光推送-记录状态-同步
     * @param sysNotifyRecord
     */
    public static void pushAlertWithMessageSyn(SysNotifyRecord sysNotifyRecord) {
        pushAlertWithMessageSyn(pushClient,sysNotifyRecord);
    }

    public static void main(String[] args) throws Exception {
        JPushUtils.pushAlertWithMessage("尊敬的xx小区业主，您好！您发起的故障报修已安排社区专属管家进行处理，请您安心等待。有问题可致电4008115251垂询，我们将竭诚为您服务", "{\"msg_id\":2119313893,\"sendno\":2073388677}", "18210225831");
    }

}
