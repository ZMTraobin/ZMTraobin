package com.cmig.future.csportal.module.villager.voucher.service.impl;

import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherInfo;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherStatus;
import com.cmig.future.csportal.module.villager.voucher.mapper.VoucherInfoMapper;
import com.cmig.future.csportal.module.villager.voucher.service.IVoucherInfoService;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoucherInfoServiceImpl extends BaseServiceImpl<VoucherInfo> implements IVoucherInfoService{
	
	@Autowired
	private VoucherInfoMapper voucherInfoMapper;
	
	@Override
	public List<VoucherInfo> listUsableVoucher(AppUser appUser) {
		 //查询所有的未领取的消费券
        VoucherReceive voucherReceive = new VoucherReceive();
        voucherReceive.setAppUserId(appUser.getId());
        voucherReceive.setStatus(VoucherStatus.VALID_STATUS);
        List<String> listDisableVoucher = voucherInfoMapper.listUserVocherInfo(voucherReceive);
        List<VoucherInfo> listVocher = voucherInfoMapper.listVocherInfo(voucherReceive,listDisableVoucher);
        for (VoucherInfo info : listVocher) {
			info.setImage(Global.getFullImagePath(info.getImage()));
		}
		return listVocher;
	}
	
}