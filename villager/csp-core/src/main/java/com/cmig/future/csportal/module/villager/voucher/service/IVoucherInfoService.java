package com.cmig.future.csportal.module.villager.voucher.service;

import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherInfo;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;

public interface IVoucherInfoService extends IBaseService<VoucherInfo>, ProxySelf<IVoucherInfoService>{
	List<VoucherInfo> listUsableVoucher(AppUser appUser);
	
	
}