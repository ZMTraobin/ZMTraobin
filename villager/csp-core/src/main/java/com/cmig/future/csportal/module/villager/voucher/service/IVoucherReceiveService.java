package com.cmig.future.csportal.module.villager.voucher.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherInfo;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceiveExcel;

public interface IVoucherReceiveService extends IBaseService<VoucherReceive>, ProxySelf<IVoucherReceiveService>{
	
	int judgeChange(VoucherReceive voucherReceive);
	
	int judgeUseable(VoucherReceive voucherReceive);
   // int existVoucher(VoucherReceive voucherReceive);
	
	List<VoucherInfo> listKindVocherInfo(AppUser appUser,String isused,int page,int pageSize);
	List<VoucherReceive> getVoucherMessageById(VoucherReceive voucherReceive);
	
	void receiveVoucher(IRequest iRequest,AppUser appUser,String...idArray);

	List<VoucherReceiveExcel> selectByUseStatus(String useStatus);
}