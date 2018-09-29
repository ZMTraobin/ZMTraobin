package com.cmig.future.csportal.module.cooperate.service;

import com.cmig.future.csportal.module.cooperate.dto.Cooperate;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface ICooperateService extends IBaseService<Cooperate>, ProxySelf<ICooperateService>{
    /**
     * 商户新增
     * @param cooperate
     */
    public void save(Cooperate cooperate);

    /**
     * 根据appUserID查询该用户下的所有商户
     * @param cooperate
     * @return
     */
    List<Cooperate> selectCooperateByAppUserId(Cooperate cooperate);

    /**
     * 分页查询所有的商户
     * @param request
     * @param cooperate
     * @param pageNum
     * @param pageSize
     * @return
     */
    List<Cooperate>selectAllCooperate(IRequest request, Cooperate cooperate, int pageNum, int pageSize);

}