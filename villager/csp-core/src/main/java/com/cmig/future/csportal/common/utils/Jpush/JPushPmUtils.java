package com.cmig.future.csportal.common.utils.Jpush;

import cn.jpush.api.push.PushClient;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 此类描述的是：乐家慧app极光推送
 *
 * @author:zhangtao107@126.com
 * @version: 2016-5-5 上午11:24:45
 */

public class JPushPmUtils extends BasePush {
    private static Log log = LogFactory.getLog(JPushPmUtils.class);
    private static String master = "d746d229958bb5735b7eeade";
    private static String appkey = "049107fec6033d132f23d656";
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

    public static void main(String[] args) throws Exception {
        JPushPmUtils.pushAlertWithMessage("尊敬的xx小区业主，您好！您发起的故障报修已安排社区专属管家进行处理，请您安心等待。有问题可致电4008115251垂询，我们将竭诚为您服务", "{\"msg_id\":2119313893,\"sendno\":2073388677}", "18210225831");
    }

}
