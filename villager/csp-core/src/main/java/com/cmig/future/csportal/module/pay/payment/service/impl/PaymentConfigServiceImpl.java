package com.cmig.future.csportal.module.pay.payment.service.impl;


import com.cmig.future.csportal.module.pay.payment.dto.PaymentConfig;
import com.cmig.future.csportal.module.pay.payment.mapper.PaymentConfigMapper;
import com.cmig.future.csportal.module.pay.payment.service.IPaymentConfigService;
import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUser;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@Transactional
public class PaymentConfigServiceImpl extends BaseServiceImpl<PaymentConfig> implements IPaymentConfigService {

    @Autowired
   private PaymentConfigMapper paymentConfigMapper;
    /**
     * 添加PaymengConfig
     * @param dto
     */
    @Override
    public void addPaymentConfig(PaymentConfig dto) {
        paymentConfigMapper.insertSelective(dto);
    }

    /**
     * 根据id获取对象
     * @param dto
     * @return
     */
    @Override
    public PaymentConfig getPaymentById(PaymentConfig dto) {
        return paymentConfigMapper.selectByPrimaryKey(dto);
    }

    /**
     * 根据主键更新对象
     * @param dto
     */
    @Override
    public void updatePayment(PaymentConfig dto) {
        paymentConfigMapper.updateByPrimaryKeySelective(dto);
    }

    /**
     * 批量删除，也就是批量更新
     * @param map
     */
    @Override
    public void bathupdatePayment(Map<String, Object> map) {
        paymentConfigMapper.bathupdatePayment(map);
    }

    /**
     * 分页查询
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     */
    @Override
    public List<PaymentConfig> selectPayment(IRequest requestContext, PaymentConfig dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return  paymentConfigMapper.selectPayment(dto);
    }

    /**
     * 停用该账号状态
     * @param dto
     */
    @Override
    public void stopPayment(PaymentConfig dto) {
        paymentConfigMapper.stopPayment(dto);
    }

    /**
     * 开启该账号
     * @param dto
     */
    @Override
    public void startPayment(PaymentConfig dto) {
        paymentConfigMapper.startPayment(dto);
    }

    /**
     * 查询出默认账号
     * @param dto
     * @return
     */
    @Override
    public PaymentConfig selectPaymentIsDefault(PaymentConfig dto) {
        return paymentConfigMapper.selectPaymentIsDefault(dto);
    }

    /**
     * 更新默认账号
     * @param dto
     */
    @Override
    public void updatePaymentDefault(PaymentConfig dto) {
        paymentConfigMapper.updatePaymentDefault(dto);
    }

}