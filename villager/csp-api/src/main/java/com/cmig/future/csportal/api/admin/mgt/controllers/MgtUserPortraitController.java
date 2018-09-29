package com.cmig.future.csportal.api.admin.mgt.controllers;

import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.sys.utils.AdminTokenUtils;
import com.cmig.future.csportal.module.villager.order.dto.VillagerVoiceOrder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @description: 用户画像
 * @author: zhangtao
 * @create: 2018-09-21 19:07
 **/
@RestController
@RequestMapping(value = "${managementPath}/user")
public class MgtUserPortraitController extends BaseExtendController {

    @RequestMapping(value = "/portrait/list", produces = {"application/json"})
    public RetApp list(VillagerVoiceOrder villagerVoiceOrder, HttpServletRequest request,
                       @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                       @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        String token = getParam(request, "token", "");
        String mgtUserId= AdminTokenUtils.getUserIdByToken(token);
        return RetAppUtil.success(null,"查询成功!");
    }
}
