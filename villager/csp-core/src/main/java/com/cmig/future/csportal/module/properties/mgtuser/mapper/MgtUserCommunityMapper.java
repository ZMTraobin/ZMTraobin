package com.cmig.future.csportal.module.properties.mgtuser.mapper;

import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

public interface MgtUserCommunityMapper extends Mapper<MgtUserCommunity>{

	/**
	 * 删除小区和用户之间的映射关系
	 * @param mgtUserCommunity
	 */
	void deleteBySourceSystemId(MgtUserCommunity mgtUserCommunity);
	/**
	 * 查询用户和小区之间的映射关系
	 */
	public List<MgtUserCommunity> getAllUserCommunity(MgtUserCommunity mgtUserCommunity);
	/**
	 * 插入用户和小区之间的映射关系
	 * @param mgtUserCommunity
     */
	public void insertUserCommunity(MgtUserCommunity mgtUserCommunity);

	/**
	 * 根据项目Id分组,查询列表
	 * @author bubu
	 * @param mgtUserCommunity
	 * @return
     */
	List<MgtUserCommunity> findPublishCommunityList(MgtUserCommunity mgtUserCommunity);

	/**
	 * 查询人员列表
	 * @author bubu
	 * @param mgtUserCommunity
	 * @return
	 */
	List<MgtUserCommunity> findPublishUserList(MgtUserCommunity mgtUserCommunity);

	/**
	 * 根据物管员工id查询关联的项目
	 * @param mgtUserCommunity
	 * @return
     */
	List<MgtUserCommunity> findCommunityListByMgtUserId(MgtUserCommunity mgtUserCommunity);

    /**
     * 查询未授权的项目列表
     * @param dto
     * @return
     */
    List<MgtUserCommunity> queryCommunitiesNoAuth(MgtUserCommunity dto);
    
   
}