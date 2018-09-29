package com.cmig.future.csportal.module.villager.goods.controllers;

import com.cmig.future.csportal.module.villager.good.dto.VillagerGood;
import com.cmig.future.csportal.module.villager.good.service.IVillagerGoodService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class VillagerGoodController extends BaseController {

    @Autowired
    private IVillagerGoodService service;


    @RequestMapping(value = "/csp/villager/good/query")
    @ResponseBody
    public ResponseData query(VillagerGood dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        return new ResponseData(service.findList(dto, page, pageSize));
    }

    @RequestMapping(value = "/csp/villager/good/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<VillagerGood> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(service.batchUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/csp/villager/good/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<VillagerGood> dto) {
        service.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/csp/villager/good/queryById")
    @ResponseBody
    public ResponseData queryById(HttpServletRequest request, Long id) {
        VillagerGood villagerGood= new VillagerGood();
        villagerGood.setId(id);
        IRequest requestContext = createRequestContext(request);
        List<VillagerGood> list = new ArrayList<>();
        VillagerGood good = service.selectByPrimaryKey(requestContext, villagerGood);
        list.add(good);
        return new ResponseData(list);
    }
}