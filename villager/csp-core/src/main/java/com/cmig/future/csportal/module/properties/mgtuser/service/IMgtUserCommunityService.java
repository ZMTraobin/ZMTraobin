package com.cmig.future.csportal.module.properties.mgtuser.service;

import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IMgtUserCommunityService extends IBaseService<MgtUserCommunity>, ProxySelf<IMgtUserCommunityService>{

	/**
	 * 查询所有的小区和用户对象
	 * @return
	 */
	public List<MgtUserCommunity> findList(MgtUserCommunity mgtUserCommunity);
	
	/**
	 * 保存小区和用户映射的对象
	 * @param mgtUserCommunity
	 */
	public void save(MgtUserCommunity mgtUserCommunity);
	
	/**
	 * 删除小区和对象之间的映射
	 * @param mgtUserCommunity
	 */
	public void deleteBySourceSystemId(MgtUserCommunity mgtUserCommunity);

	/**
	 * 根据项目Id分组,查询列表
	 * @author bubu
	 * @param mgtUserCommunity
	 * @return
	 */
	 public List<MgtUserCommunity> findPublishCommunityList(IRequest requestContext, MgtUserCommunity mgtUserCommunity, int page, int pagesize);

	/**
	 * 查询人员列表(分页)
	 * @author bubu
	 * @param mgtUserCommunity
	 * @return
	 */
	public List<MgtUserCommunity> findPublishUserList(IRequest requestContext, MgtUserCommunity mgtUserCommunity, int page, int pagesize);

	/**
	 * 查询人员列表
	 * @author bubu
	 * @param mgtUserCommunity
	 * @return
	 */
	public List<MgtUserCommunity> findPublishUserList(MgtUserCommunity mgtUserCommunity);

	/**
	 * 根据物管员工id查询关联的项目
	 * @param mgtUserCommunity
	 * @return
	 */
	List<MgtUserCommunity> findCommunityListByMgtUserId(IRequest request, MgtUserCommunity mgtUserCommunity, int page, int pagesize);

    /**
     * 查询未授权的项目列表
     * @param dto
     * @param page
     * @param pageSize
     * @return
     */
    List<MgtUserCommunity> queryCommunitiesNoAuth(MgtUserCommunity dto, int page, int pageSize);

}