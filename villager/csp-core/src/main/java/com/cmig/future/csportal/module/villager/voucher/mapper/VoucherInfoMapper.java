package com.cmig.future.csportal.module.villager.voucher.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherInfo;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;

public interface VoucherInfoMapper extends Mapper<VoucherInfo>{
	//out
	List<String> listUserVocherInfo(VoucherReceive voucherReceiv);
	List<VoucherInfo> listVocherInfo(@Param("voucherRecive")VoucherReceive voucherRecive,@Param("disableVoucherIds")List<String> disableVoucherIds);
	

}