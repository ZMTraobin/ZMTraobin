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
import com.cmig.future.csportal.module.user.appuser.dto.AppUser;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.villager.order.dto.VillagerOrder;
import com.cmig.future.csportal.module.villager.order.service.IVillagerOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * app用户Controller
 *
 * @author zhengshujun
 * @version 2015-12-01
 */
@RestController
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VillagerOrderApiController extends BaseExtendController {

    @Autowired
    private IVillagerOrderService villagerOrderService;

    @Autowired
    private IAppUserService appUserService;

    /**
     * 下单
     */
    @RequestMapping(value = "/st/villager/order/add", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp add(@ModelAttribute VillagerOrder data, HttpServletRequest request) {
        if (StringUtils.isEmpty(data)||StringUtils.isEmpty(data.getGoodId())||StringUtils.isEmpty(data.getGoodNum())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL);
        }
        String token = getParam(request, "token", "");
        AppUser appUser = appUserService.getByToken(token);
        data.setAppUserId(appUser.getId());
        data.setClientIp(getRemoteid(request));
        villagerOrderService.add(data);
        return RetAppUtil.success(data,"下单成功!");
    }


}