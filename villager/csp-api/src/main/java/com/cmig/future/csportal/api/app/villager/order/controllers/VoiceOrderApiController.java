/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.order.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.utils.UserTokenUtils;
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.villager.order.dto.VillagerVoiceOrder;
import com.cmig.future.csportal.module.villager.order.service.IVillagerVoiceOrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * voice order controller
 */
@Controller
@ResponseBody
@RequestMapping(value = "${userPath}")
public class VoiceOrderApiController extends BaseExtendController {

    private final static Logger logger = LoggerFactory.getLogger(VoiceOrderApiController.class);

    @Autowired
    private IVillagerVoiceOrderService villagerVoiceOrderService;

    @Autowired
    private IAppUserService appUserService;

    /**
     * 下单
     * @param villagerVoiceOrder
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/voice/order/add", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp add(VillagerVoiceOrder villagerVoiceOrder, HttpServletRequest request, HttpServletResponse response) {

        if (StringUtils.isEmpty(villagerVoiceOrder)
                ||StringUtils.isEmpty(villagerVoiceOrder.getVoiceUrl())
                ||StringUtils.isEmpty(villagerVoiceOrder.getGoodId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        String token = getParam(request, "token", "");
        AppUser appUser = appUserService.getByToken(token);
        villagerVoiceOrder.setAppUserId(appUser.getId());
        villagerVoiceOrder.setClientIp(getRemoteid(request));

        villagerVoiceOrderService.add(villagerVoiceOrder);
        return RetAppUtil.success(villagerVoiceOrder,"下单成功!");
    }

    /**
     * 我的订单
     * @param villagerVoiceOrder
     * @param request
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/st/voice/order/list", produces = {"application/json"})
    @ResponseBody
    public RetApp list(VillagerVoiceOrder villagerVoiceOrder, HttpServletRequest request,
                       @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        String token = getParam(request, "token", "");
        AppUser appUser = appUserService.getByToken(token);
        villagerVoiceOrder.setAppUserId(appUser.getId());
        List<VillagerVoiceOrder> list=villagerVoiceOrderService.findAdminList(villagerVoiceOrder,page,pageSize);
        return RetAppUtil.success(list,"查询成功!");
    }

    /**
     * 确认收货
     * @param villagerVoiceOrder
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/st/voice/order/confirmReceipt", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp confirmReceipt(VillagerVoiceOrder villagerVoiceOrder, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(villagerVoiceOrder)||StringUtils.isEmpty(villagerVoiceOrder.getId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        String token = getParam(request, "token", "");
        String appUserId= UserTokenUtils.getUserIdByToken(token);
        villagerVoiceOrderService.confirmReceipt(appUserId,villagerVoiceOrder.getId());
        return RetAppUtil.success("确认收货成功!");
    }
}
