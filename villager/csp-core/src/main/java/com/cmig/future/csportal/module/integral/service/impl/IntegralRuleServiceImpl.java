package com.cmig.future.csportal.module.integral.service.impl;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation;
import com.cmig.future.csportal.common.utils.Arith;
import com.cmig.future.csportal.module.integral.dto.IntegralRule;
import com.cmig.future.csportal.module.integral.mapper.IntegralRuleMapper;
import com.cmig.future.csportal.module.integral.service.IIntegralRuleService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

@Service
@Transactional
public class IntegralRuleServiceImpl extends BaseServiceImpl<IntegralRule> implements IIntegralRuleService {

    @Autowired
    private IntegralRuleMapper integralRuleMapper;

	@Autowired
	private IntegralRuleOperation integralRuleOperation;
    /**
     * 保存积分规则
     * @param dto
     */
    @Override
    public void save(IntegralRule dto) {
        integralRuleMapper.insertSelective(dto);
    }

    /**
     * 根据主键获取积分规则对象
     * @param dto
     * @return
     */
    @Override
    public IntegralRule getIntegralRuleById(IntegralRule dto) {
        return integralRuleMapper.selectByPrimaryKey(dto);
    }

    /**
     * 停止规则积分状态
     * @param dto
     */
    @Override
    public void stopRuleStatus(IntegralRule dto) {
        integralRuleMapper.updateByPrimaryKeySelective(dto);
    }

    /**
     * 开启积分规则状态
     * @param dto
     */
    @Override
    public void startRuleStatus(IntegralRule dto) {
        integralRuleMapper.updateByPrimaryKeySelective(dto);
    }

    /**
     * 更新积分规则
     * @param dto
     */
    @Override
    public void updateIntgralRule(IntegralRule dto) {
        integralRuleMapper.updateByPrimaryKeySelective(dto);
    }

	/**
	 * 计算当前订单最大可抵扣积分、金额
	 * @param type
	 * @param orderAmount
	 * @param openid
	 * @return
	 * @throws Exception
	 */
	@Override
	public JSONObject getMaxAvailableIntegral(String type, String orderAmount, String openid) throws Exception {
		JSONObject result=new JSONObject();
		result.put("deductionBalance",0);//当日剩余可抵扣额度
		result.put("maxAvailableNum",0);//最大可抵扣积分
		result.put("maxAvailableAmount", 0);//最大可抵扣金额
		Integer minIntegral;//积分系统是否开启
		if(Boolean.valueOf(Global.getConfig("INTEGRAL.ISENABLE_OR_DISABLED"))){
			//查询用户积分余额
			Map<String, String> paramMap = new HashMap<String, String>();
			paramMap.put("uid", openid);
			JSONObject balanceObject = integralRuleOperation.integralSelect(paramMap);
			String returnCode=balanceObject.get("returnCode").toString();
			String message=balanceObject.get("message").toString();
			if(!IntegralRuleOperation.SUCCESS_CODE.equals(returnCode)){
				throw new DataWarnningException(message);
			}
			//积分可用余额
			Integer balance = new Integer(balanceObject.get("usefulBalance")==null?"0":balanceObject.get("usefulBalance").toString());
			//订单金额转积分
			Integer orderAmountIntegral=new Integer(orderAmount);
			if(balance.compareTo(orderAmountIntegral)<0){
				minIntegral=balance;
			}else{
				minIntegral=orderAmountIntegral;
			}
			//查询用户在当前场景下剩余可抵扣积分额度
			paramMap.put("type",type);
			JSONObject jsonObject =  integralRuleOperation.selectIntegralByType(paramMap);
			returnCode = jsonObject.get("returnCode").toString();
			message = jsonObject.get("message").toString();
			if(!IntegralRuleOperation.SUCCESS_CODE.equals(returnCode)){
				throw new DataWarnningException(message);
			}
			//当日剩余可抵扣额度
			String deductionBalance=jsonObject.get("deductionBalance")==null?"0":jsonObject.get("deductionBalance").toString();
			if(!IntegralRuleOperation.DAILY_DEDUCTION_BALANCE_NO_LIMIT.equals(deductionBalance)){
				if(new Integer(deductionBalance).compareTo(minIntegral)<0){
					minIntegral=new Integer(deductionBalance);
				}
			}
			//如果最大可抵扣积分小于0，则置为0
			Double maxAvailableAmount = Arith.div(new Double(minIntegral),new Double(IntegralRuleOperation.RMB_TO_INTEGRAL),2);
			if(minIntegral < 0){
				minIntegral = 0;
				maxAvailableAmount = 0.00;
			}
			result.put("deductionBalance",deductionBalance);//当日剩余可抵扣额度
			result.put("maxAvailableNum",minIntegral);//最大可抵扣积分
			result.put("maxAvailableAmount", maxAvailableAmount);//最大可抵扣金额
		}
		return result;
	}

}