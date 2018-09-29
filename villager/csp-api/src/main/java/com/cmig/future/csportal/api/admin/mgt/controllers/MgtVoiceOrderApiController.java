/**
 * .
 */
package com.cmig.future.csportal.api.admin.mgt.controllers;

import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.pay.conf.OrderFormHelper;
import com.cmig.future.csportal.module.sys.utils.AdminTokenUtils;
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
@RequestMapping(value = "${managementPath}/user")
public class MgtVoiceOrderApiController extends BaseExtendController {

    private final static Logger logger = LoggerFactory.getLogger(MgtVoiceOrderApiController.class);

    @Autowired
    private IVillagerVoiceOrderService villagerVoiceOrderService;

    @Autowired
    private IAppUserService appUserService;


    /**
     * 待接单列表
     * @param villagerVoiceOrder
     * @param request
     * @param page
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/voice/order/list", produces = {"application/json"})
    @ResponseBody
    public RetApp list(VillagerVoiceOrder villagerVoiceOrder, HttpServletRequest request,
                       @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        String token = getParam(request, "token", "");
        String mgtUserId= AdminTokenUtils.getUserIdByToken(token);
        villagerVoiceOrder.setMgtUserId(mgtUserId);
        villagerVoiceOrder.setOrderStatus(OrderFormHelper.ORDER_STATUS_WAITING_RECEIVE);
        List<VillagerVoiceOrder> list=villagerVoiceOrderService.findAdminList(villagerVoiceOrder,page,pageSize);
        return RetAppUtil.success(list,"查询成功!");
    }


    /**
     * 管理端 接单
     * @param villagerVoiceOrder
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/voice/order/received", produces = {"application/json"}, method = RequestMethod.POST)
    @ResponseBody
    public RetApp received(VillagerVoiceOrder villagerVoiceOrder, HttpServletRequest request, HttpServletResponse response) {
        if (StringUtils.isEmpty(villagerVoiceOrder)||StringUtils.isEmpty(villagerVoiceOrder.getId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        String token = getParam(request, "token", "");
        String mgtUserId= AdminTokenUtils.getUserIdByToken(token);
        villagerVoiceOrderService.received(mgtUserId,villagerVoiceOrder.getId());
        return RetAppUtil.success("接单成功!");
    }

}
