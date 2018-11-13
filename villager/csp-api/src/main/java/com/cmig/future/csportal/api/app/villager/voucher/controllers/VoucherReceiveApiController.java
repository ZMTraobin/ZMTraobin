package com.cmig.future.csportal.api.app.villager.voucher.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.exceptions.ServiceExceptionHelper;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.cmig.future.csportal.module.villager.integral.service.IIntegralDetailService;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherInfo;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;
import com.cmig.future.csportal.module.villager.voucher.service.IVoucherInfoService;
import com.cmig.future.csportal.module.villager.voucher.service.IVoucherReceiveService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;

/**
 * controller of voucherReceive
 */
@Controller
@ResponseBody
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VoucherReceiveApiController extends BaseExtendController{
	
	private final static Logger logger = LoggerFactory.getLogger(VoucherApiController.class);
	 @Autowired
	    private IAppUserService appUserService;
	    
	    @Autowired
		IAttentionCommunityUserService iAttentionCommunityUserService;
	    
	    @Autowired
	    private IVoucherReceiveService iVoucherReceiveService;
	    
	    @Autowired
	    private IIntegralDetailService integralDetailService;
	    
	    @Autowired
	    private IVoucherInfoService iVoucherInfoService;
	    
	/**
	 * 领取消费券
	 * @param voucherReceive
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/st/consume/voucher/receive", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp receive(
    		HttpServletRequest request, 
    		HttpServletResponse response) {
		IRequest iRequest = createRequestContext(request);
		//获取appUser信息
        String token = getParam(request, "token", "");
        //获取消费券id
        String voucherId = getParam(request, "voucherId", "");
        String voucherIds = getParam(request, "voucherIds", "");
        String[] idArray = null;
        if(voucherIds != null)
        	idArray = voucherIds.split(",");
        if(StringUtils.isEmpty(token)){
        	return RetAppUtil.tokenDisabled();
        }
        if(StringUtils.isEmpty(voucherId)&&StringUtils.isEmpty(voucherIds)){
        	throw ServiceExceptionHelper.argsNull();
        }
        AppUser appUser = appUserService.getByToken(token);
        iVoucherReceiveService.receiveVoucher(iRequest, appUser, idArray);
        return RetAppUtil.success("领取成功!");
        }
	/**
	 * 分类查询
	 * @param voucherReceive
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/st/consume/voucher/show", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp show(VoucherReceive voucherReceive,
    		HttpServletRequest request, 
    		HttpServletResponse response,
    		@RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		//获取appUser信息
        String token = getParam(request, "token", "");
        //消费券的状态(0使用，1未使用，2过期)
        String isused = getParam(request, "isused", "");
        if(StringUtils.isEmpty(token)){
        	return RetAppUtil.tokenDisabled();
        }
        if(StringUtils.isEmpty(isused)){
        	throw ServiceExceptionHelper.argsNull();
        }
        AppUser appUser = appUserService.getByToken(token);
        PageHelper.startPage(page, pageSize);
        List<VoucherInfo> listVoucher = iVoucherReceiveService.listKindVocherInfo(appUser, isused, page, pageSize);
        return RetAppUtil.success(listVoucher,"查询成功！");
        }
	    
	/**
	 * 点击兑换
	 * @param voucherReceive
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/st/consume/voucher/exchange", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp exchange(
    		HttpServletRequest request, 
    		HttpServletResponse response) {
		IRequest iRequest = createRequestContext(request);
		//获取appUser信息
        String token = getParam(request, "token", "");
        //获取消费券id
        String voucherId = getParam(request, "voucherId", "");
        if(StringUtils.isEmpty(token)){
        	return RetAppUtil.tokenDisabled();
        }
        AppUser appUser = appUserService.getByToken(token);
        if(StringUtils.isEmpty(voucherId)){
        	throw ServiceExceptionHelper.argsNull();
        }
        // 根据id获取积分数
        VoucherInfo vinfo = new VoucherInfo();
        vinfo.setId(Long.valueOf(voucherId));
        VoucherInfo vouInfo = iVoucherInfoService.selectByPrimaryKey(iRequest, vinfo);
        // 使用积分
        Map map = integralDetailService.reduceScores(null,vouInfo.getEqualsPrice(),appUser.getId());
        // 领取券
        String[] idArray = voucherId.split(",");
        iVoucherReceiveService.receiveVoucher(iRequest, appUser, idArray);
        return RetAppUtil.success("领取成功!");
        }
	
	/**
	 * 充值
	 * @param voucherReceive
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/st/consume/voucher/recharge", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp recharge(
    		HttpServletRequest request, 
    		HttpServletResponse response) {
		IRequest iRequest = createRequestContext(request);
		//获取appUser信息
        String token = getParam(request, "token", "");
        //获取消费券id
        String receiveId = getParam(request, "receiveId", "");
        if(StringUtils.isEmpty(token)){
        	return RetAppUtil.tokenDisabled();
        }
        AppUser appUser = appUserService.getByToken(token);
        if(StringUtils.isEmpty(receiveId)){
        	throw ServiceExceptionHelper.argsNull();
        }
        // 更新数据
        VoucherReceive receive = new VoucherReceive();
        receive.setId(Long.valueOf(receiveId));
        VoucherReceive receive1 = iVoucherReceiveService.selectByPrimaryKey(iRequest, receive);
        receive1.setIsused("3");
        receive1.setUseDate(new Date());
        iVoucherReceiveService.updateByPrimaryKey(iRequest, receive1);
        return RetAppUtil.success("充值成功!");
        }
}
