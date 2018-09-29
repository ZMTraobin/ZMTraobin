package com.cmig.future.csportal.module.pay.payment.service;


import com.cmig.future.csportal.module.pay.payment.dto.PaymentConfig;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;
import java.util.Map;

public interface IPaymentConfigService extends IBaseService<PaymentConfig>, ProxySelf<IPaymentConfigService> {

    /**
     * 添加PaymentConfig
     * @param dto
     */
    public void addPaymentConfig(PaymentConfig dto);

    /**
     * 根据主键获取对象
     */
    public PaymentConfig getPaymentById(PaymentConfig dto);

    /**
     * 更新对象
     * @param dto
     */
    public void updatePayment(PaymentConfig dto);

    /**
     * 进行批量更新
     * @param map
     */
    public void bathupdatePayment(Map<String, Object> map);

    /**
     * 分页查询
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     */
    public List<PaymentConfig> selectPayment(IRequest requestContext, PaymentConfig dto, int page, int pageSize);

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