package com.cmig.future.csportal.module.villager.voucher.service.impl;

import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.common.utils.Encodes;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherInfo;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceiveExcel;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherStatus;
import com.cmig.future.csportal.module.villager.voucher.mapper.VoucherInfoMapper;
import com.cmig.future.csportal.module.villager.voucher.mapper.VoucherReceiveMapper;
import com.cmig.future.csportal.module.villager.voucher.service.IVoucherReceiveService;
import com.github.pagehelper.PageHelper;

import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class VoucherReceiveServiceImpl extends BaseServiceImpl<VoucherReceive> implements IVoucherReceiveService{
	
	@Autowired
    private VoucherReceiveMapper voucherReceiveMapper;
	
	@Autowired
	private VoucherInfoMapper voucherInfoMapper;

	@Override
	public int judgeChange(VoucherReceive voucherReceive) {
		// TODO Auto-generated method stub
		return voucherReceiveMapper.judgeChange(voucherReceive);
	}

	@Override
	public List<VoucherInfo> listKindVocherInfo(AppUser appUser,String isused
			,int page,int pageSize) {
		    VoucherReceive voucherReceive = new VoucherReceive();
	        voucherReceive.setAppUserId(appUser.getId());
	        voucherReceive.setIsused(isused);
	        voucherReceive.setStatus(VoucherStatus.VALID_STATUS);
	        //用户已领取消费券的详情
	        PageHelper.startPage(page, pageSize);
	        List<VoucherInfo> listVoucher = voucherReceiveMapper.listKindVocherReceiveInfo(voucherReceive);
	        for (VoucherInfo v : listVoucher) {
	        	JSONObject jsonObject = new JSONObject();
	        	String voucher_ReceiveId = Encodes.encodeBase64(v.getVoucher_ReceiveId().toString()+","+new Date());
	        	jsonObject.put("voucherReceiveId", voucher_ReceiveId);
			    v.setBarCode(jsonObject.toJSONString());
			    v.setImage(Global.getFullImagePath(v.getImage()));
	        }
		return listVoucher;
	}

	@Override
	public void receiveVoucher(IRequest iRequest, AppUser appUser, String... idArray) {
		// TODO Auto-generated method stub
		for (String voucherId : idArray) {
			VoucherReceive voucherReceive = this.existVoucher(appUser, voucherId);
			voucherReceive.setReceiveWay("领取");
	        voucherReceive.setStatus(VoucherStatus.VALID_STATUS);
	        voucherReceive.setIsused(VoucherStatus.NOT_USED);
	        insertSelective(iRequest, voucherReceive);    
		}
		
	}
	VoucherReceive existVoucher(AppUser appUser,String voucherId) {
		VoucherReceive voucherReceive = new VoucherReceive();
		voucherReceive.setVoucherId(Long.parseLong(voucherId));
		voucherReceive.setAppUserId(appUser.getId());
		voucherReceive.setMobile(appUser.getMobile());
		voucherReceive.setStatus(VoucherStatus.VALID_STATUS);
        List<String> listDisableVoucher = voucherInfoMapper.listUserVocherInfo(voucherReceive);
        List<VoucherInfo> listVocher = voucherInfoMapper.listVocherInfo(voucherReceive,listDisableVoucher);
		if(!(listVocher.size() > 0)) 
	        throw new DataWarnningException("不符合领取条件！！！");
        voucherReceive.setLimitDate(DateUtils.addDays(new Date(), Integer.parseInt(String.valueOf(listVocher.get(0).getValidDays()))));
		return voucherReceive;
	}

	@Override
	public int judgeUseable(VoucherReceive voucherReceive) {
		// TODO Auto-generated method stub
		return voucherReceiveMapper.judgeUseable(voucherReceive);
	}

	@Override
	public List<VoucherReceive> getVoucherMessageById(VoucherReceive voucherReceive) {
		List<VoucherReceive> voucherReceives = voucherReceiveMapper.getVoucherMessageById(voucherReceive);
		
		return voucherReceives;
	}
	
	public static void main(String[] args) {
		JSONObject jsonObject = new JSONObject();
		String voucher_ReceiveId = Encodes.encodeBase64("64,"+new Date());
    	String voucherType = Encodes.encodeBase64("6");
    	jsonObject.put("voucher_ReceiveId", voucher_ReceiveId);
    	jsonObject.put("voucher_type", voucherType);
    	String ss = Encodes.decodeBase64String(voucher_ReceiveId);
    	String[] m = ss.split(",");
    	System.out.println(m[0]);
	}

	@Override
	public List<VoucherReceiveExcel> selectByUseStatus(String useStatus) {
		return voucherReceiveMapper.selectByUseStatus(useStatus);
	}
	
}