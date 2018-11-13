package com.cmig.future.csportal.api.admin.mgt.controllers;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.Encodes;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.exceptions.ServiceExceptionHelper;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.utils.AdminTokenUtils;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherStatus;
import com.cmig.future.csportal.module.villager.voucher.service.IVoucherReceiveService;
import com.hand.hap.core.IRequest;

@RestController
@RequestMapping(value = "${managementPath}/user")
public class MgtVoucherApiController extends BaseExtendController{
	@Autowired
    private IVoucherReceiveService iVoucherReceiveService;
	
	/**
	 * 兑换可用消费券
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/consume/voucher/change", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp change(
    		HttpServletRequest request, 
    		HttpServletResponse response) {
		IRequest iRequest = createRequestContext(request);
		//获取appUser信息
	    String token = getParam(request, "token", "");
        //消费券的状态
        String voucher_ReceiveId = getParam(request, "voucher_ReceiveId", "");
        String image = getParam(request, "image", "");
        if(StringUtils.isEmpty(token)){
        	return RetAppUtil.tokenDisabled();
        }
        if(StringUtils.isEmpty(voucher_ReceiveId)){
        	throw ServiceExceptionHelper.argsNull();
        }
        if(StringUtils.isEmpty(image)){
        	throw ServiceExceptionHelper.argsNull();
        }
	    String mgtUserId= AdminTokenUtils.getUserIdByToken(token);
        VoucherReceive voucherReceive = new VoucherReceive();
        voucherReceive.setId(Long.parseLong(voucher_ReceiveId));
        voucherReceive.setMgtUserId(mgtUserId);
        voucherReceive.setImage(image);
        voucherReceive.setIsused(VoucherStatus.USED);
        int msg = iVoucherReceiveService.judgeChange(voucherReceive);
        if(msg>0) 
        	throw new DataWarnningException("该券已被兑换！！");
        voucherReceive.setUseDate(new Date());
        //记录兑换信息
        iVoucherReceiveService.updateByPrimaryKeySelective(iRequest, voucherReceive);
        return RetAppUtil.success("兑换成功！");
        }
	
	@RequestMapping(value = "/consume/voucher/getVoucherMessageById", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp getVoucherMessageById(
    		HttpServletRequest request, 
    		HttpServletResponse response) {
		IRequest iRequest = createRequestContext(request);
		//获取appUser信息
	    String token = getParam(request, "token", "");
        //消费券的状态
        String voucher_ReceiveId = getParam(request, "voucher_ReceiveId", "");
        if(StringUtils.isEmpty(token)){
        	return RetAppUtil.tokenDisabled();
        }
        if(StringUtils.isEmpty(voucher_ReceiveId)){
        	throw ServiceExceptionHelper.argsNull();
        }
        String voucherReceiveId = Encodes.decodeBase64String(voucher_ReceiveId);
        String[] params = voucherReceiveId.split(",");
        VoucherReceive voucherReceive = new VoucherReceive();
        if(params.length>1) {
        	voucherReceive.setId(Long.parseLong(params[0]));        	
        }
	    String mgtUserId= AdminTokenUtils.getUserIdByToken(token);
        
        voucherReceive.setIsused(VoucherStatus.USED);
        //判断未过期，可用
        int msg = iVoucherReceiveService.judgeUseable(voucherReceive);
        if(!(msg>0)) 
        	throw new DataWarnningException("该券不可用！！");
        List<VoucherReceive> voucherReceives = iVoucherReceiveService.getVoucherMessageById(voucherReceive);
        if(voucherReceives!=null&& voucherReceives.size()>0) {
    		for (VoucherReceive voucherinfo : voucherReceives) {
    			if(voucherinfo.getUserIcon()==null) {
    				voucherinfo.setUserIcon(Global.getDefaultImagePath(request));
    		}else{
    			voucherinfo.setUserIcon(Global.getFullImagePath(voucherinfo.getUserIcon()));
    		}
    		}
        }
        return RetAppUtil.success(voucherReceives,"兑换券信息！");
	}
}
	
