package com.cmig.future.csportal.module.properties.authorManager.service;

import java.util.List;

import com.cmig.future.csportal.module.properties.authorManager.dto.LjhSysUserCommunity;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

public interface ILjhSysUserCommunityService
        extends IBaseService<LjhSysUserCommunity>, ProxySelf<ILjhSysUserCommunityService> {

    /**
     * 保存授权关系
     * @param request
     * @param ljhSysUserCommunities
     * @return
     */
    List<LjhSysUserCommunity> saveUserCommunity(IRequest request,
                                                List<LjhSysUserCommunity> ljhSysUserCommunities);

    /**
     * 根据userId查询
     * @param requestContext
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<LjhSysUserCommunity> selectByUser(IRequest requestContext, LjhSysUserCommunity dto, int page, int pageSize);

    /**
     * 根据userId查询列表
     * @param dto
     * @return
     */
    List<LjhSysUserCommunity> findListByUserId(LjhSysUserCommunity dto);

    /**
     * 查询未授权的所有小区
     * @param requestContext
     * @param page
     * @param pageSize
     * @return
     */
    List<LjhSysUserCommunity> queryCommunitiesNoAuth(IRequest requestContext, LjhSysUserCommunity dto, int page, int pageSize);

}