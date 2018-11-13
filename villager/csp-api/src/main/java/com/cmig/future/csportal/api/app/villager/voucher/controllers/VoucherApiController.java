package com.cmig.future.csportal.api.app.villager.voucher.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.user.attentionCommunity.service.IAttentionCommunityUserService;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherInfo;
import com.cmig.future.csportal.module.villager.voucher.dto.VoucherReceive;
import com.cmig.future.csportal.module.villager.voucher.service.IVoucherInfoService;

/**
 * controller of voucher
 */
@Controller
@ResponseBody
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VoucherApiController extends BaseExtendController {
	
    private final static Logger logger = LoggerFactory.getLogger(VoucherApiController.class);

    @Autowired
    private IAppUserService appUserService;
    
    @Autowired
	IAttentionCommunityUserService iAttentionCommunityUserService;
    
    @Autowired
    private IVoucherInfoService iVoucherInfoService;
  

   /**
    * 符合领取条件但未领取的消费券
    * @param request
    * @param response
    * @return
    */
	@RequestMapping(value = "/st/consume/voucher/list", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp list(HttpServletRequest request, 
    		HttpServletResponse response) {
		//获取appUser信息
        String token = getParam(request, "token", "");
        if(StringUtils.isEmpty(token)){
        	return RetAppUtil.tokenDisabled();
        }
        AppUser appUser = appUserService.getByToken(token);
        
        List<VoucherInfo> listVocher = iVoucherInfoService.listUsableVoucher(appUser);
       
        return RetAppUtil.success(listVocher,"可领取消费券!");
    }

}
