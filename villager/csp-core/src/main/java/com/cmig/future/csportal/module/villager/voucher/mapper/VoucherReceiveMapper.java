package com.cmig.future.csportal.module.villager.voucher.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmig.future.csportal.module.villager.voucher.dto.VoucherInfo;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceiveExcel;

public interface VoucherReceiveMapper extends Mapper<VoucherReceive>{
	int judgeChange(VoucherReceive voucherReceive);
    List<VoucherInfo> listKindVocherReceiveInfo(@Param("voucher")VoucherReceive voucherReceive);
    int judgeUseable(VoucherReceive voucherReceive);
	int existVoucher(VoucherReceive voucherReceive);
	List<VoucherReceive> getVoucherMessageById(VoucherReceive voucherReceive);
	List<VoucherReceiveExcel> selectByUseStatus(String useStatus);
}