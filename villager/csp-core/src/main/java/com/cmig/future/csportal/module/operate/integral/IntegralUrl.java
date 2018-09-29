package com.cmig.future.csportal.module.operate.integral;

import com.cmig.future.csportal.common.config.Global;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/7/14.
 */
public class IntegralUrl {

    private static Logger log = LoggerFactory.getLogger(IntegralUrl.class);
    //增加积分
    public static String ADD_INTEGRAL;
    //扣除积分
    public static String DEDUCTION_INTEGRAL;
    //积分商城登陆
    public static String AUTO_LOGIN;
    //积分余额查询
    public static String QUERY_BLANCE;
    //每日特定场景下积分抵扣剩余额度查询
    public static String DAILY_DEDUCTION_BALANCE;
    //积分冻结
    public static String FREEZE_INTEGRAL;
    //积分解冻
    public static String UNFREEZE_INTEGRAL;

	public static Map<String,String> headerMap=new HashMap<>();

    static{
        String integralServerUrl = Global.getHspServerUrl("INTEGRAL_SERVER_URL");
        if(Global.getBoolean("HSP.ENABLE_FLAG",false)){
            ADD_INTEGRAL=integralServerUrl+"/credit/api/addCredit";
            DEDUCTION_INTEGRAL=integralServerUrl+"/credit/api/creditConsume";
            AUTO_LOGIN=integralServerUrl+"/credit/api/autoLogin";
            QUERY_BLANCE=integralServerUrl+"/credit/api/queryBalance";
            DAILY_DEDUCTION_BALANCE=integralServerUrl+"/credit/api/dailyDeductionBalance";
            FREEZE_INTEGRAL=integralServerUrl+"/credit/api/freezeCredits";
            UNFREEZE_INTEGRAL=integralServerUrl+"/credit/api/unfreezeCredits";

	        headerMap.put("Authorization",Global.getHspAuthorization());
        }else{
            ADD_INTEGRAL=integralServerUrl+"/api/v1.0/addCredit";
            DEDUCTION_INTEGRAL=integralServerUrl+"/api/v1.0/creditConsume";
            AUTO_LOGIN=integralServerUrl+"/api/v1.0/autoLogin";
            QUERY_BLANCE=integralServerUrl+"/api/v1.0/queryBalance";
            DAILY_DEDUCTION_BALANCE=integralServerUrl+"/api/v1.0/dailyDeductionBalance";
            FREEZE_INTEGRAL=integralServerUrl+"/api/v1.0/freezeCredits";
            UNFREEZE_INTEGRAL=integralServerUrl+"/api/v1.0/unfreezeCredits";
        }

    }
}
