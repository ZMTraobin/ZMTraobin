/**
 * .
 */
package com.cmig.future.csportal.api.app.villager.order.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import com.cmig.future.csportal.module.villager.good.dto.VillagerGood;
import com.cmig.future.csportal.module.villager.good.service.IVillagerGoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * app用户Controller
 *
 * @author zhengshujun
 * @version 2015-12-01
 */
@RestController
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class VillagerGoodApiController extends BaseExtendController {

    @Autowired
    private IVillagerGoodService villagerGoodService;

    @Autowired
    private IAppUserService appUserService;

    /**
     * 商品清单
     */
    @RequestMapping(value = "/villager/good/list", produces = {"application/json"}, method = RequestMethod.POST)
    public RetApp list(VillagerGood data,@RequestParam(defaultValue = DEFAULT_PAGE) int page,@RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        List<VillagerGood> list= villagerGoodService.findList(data,page,pageSize);
        if(!StringUtils.isEmpty(list)){
            for(VillagerGood good:list){
                if(!StringUtils.isEmpty(good.getGoodImage())){
                    good.setGoodImage(Global.getFullImagePath(good.getGoodImage()));
                }
            }
        }
        return RetAppUtil.success(list,"查询成功!");
    }


}