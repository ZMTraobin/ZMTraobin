package com.cmig.future.csportal.module.integral.service;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.module.integral.dto.IntegralFlowing;
import com.cmig.future.csportal.module.integral.dto.IntegralRule;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;
public interface IIntegralRuleService extends IBaseService<IntegralRule>, ProxySelf<IIntegralRuleService> {
    /**
     * 保存积分规则
     */
    void save(IntegralRule dto);

    /**
     * 根据主键获取积分规则对象
     * @param dto
     * @return
     */
    IntegralRule getIntegralRuleById(IntegralRule dto);

    /**
     * 停止积分规则状态
     * @param dto
     */
    void stopRuleStatus(IntegralRule dto);

    /**
     * 开启积分规则状态
     * @param dto
     */
    void startRuleStatus(IntegralRule dto);

    /**
     * 更新积分规则
     * @param dto
     */
    void updateIntgralRule(IntegralRule dto);

	/**
	 * 计算当前订单最大可抵扣积分、金额
	 * @param type
	 * @param orderAmount
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	JSONObject getMaxAvailableIntegral(String type, String orderAmount, String openid)throws Exception;



}