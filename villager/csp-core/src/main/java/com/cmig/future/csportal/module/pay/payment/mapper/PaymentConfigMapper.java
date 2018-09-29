package com.cmig.future.csportal.module.pay.payment.mapper;


import com.cmig.future.csportal.module.pay.payment.dto.PaymentConfig;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;
import java.util.Map;

public interface PaymentConfigMapper extends Mapper<PaymentConfig> {
    /**
     * 进行批量更新
     * @param map
     */
    public void bathupdatePayment(Map<String, Object> map);
    /**
     * 分页查询语句
     * @param dto
     */
    public List<PaymentConfig> selectPayment(PaymentConfig dto);

    /**
     * 停用该账号
     * @param dto
     */
    public void stopPayment(PaymentConfig dto);

    /**
     * 开启该账号
     * @param dto
     */
    public void startPayment(PaymentConfig dto);

    /**
     *查询默认账号
     * @return
     */
    public PaymentConfig  selectPaymentIsDefault(PaymentConfig dto);

    /**
     * 跟新默认账号
     * @param dto
     */
    public void updatePaymentDefault(PaymentConfig dto);

}