package com.cmig.future.csportal.module.properties.mgtuser.service.impl;

import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserCommunity;
import com.cmig.future.csportal.module.properties.mgtuser.mapper.MgtUserCommunityMapper;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserCommunityService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MgtUserCommunityServiceImpl extends BaseServiceImpl<MgtUserCommunity> implements IMgtUserCommunityService {

	@Autowired
	private MgtUserCommunityMapper mgtUserCommunityMapper;

	/**
	 * 查询用户和小区之间的映射关系
	 * @param mgtUserCommunity
	 * @return
     */
	@Override
	public List<MgtUserCommunity> findList(MgtUserCommunity mgtUserCommunity) {
		return mgtUserCommunityMapper.getAllUserCommunity(mgtUserCommunity);
	}
    /**
     * 保存小区和对象映射的关系
     */
	@Override
	public void save(MgtUserCommunity mgtUserCommunity) {

		mgtUserCommunityMapper.insertUserCommunity(mgtUserCommunity);
		//super.mapper.insertSelective(mgtUserCommunity);
	}
	/**
	 * 删除和小区之间的关系
	 */
	@Override
	public void deleteBySourceSystemId(MgtUserCommunity mgtUserCommunity) {
		mgtUserCommunityMapper.deleteBySourceSystemId(mgtUserCommunity);
		
	}

	/**
	 * 根据项目Id分组,查询列表
	 * @author bubu
	 * @param mgtUserCommunity
	 * @return
	 */
	@Override
	public List<MgtUserCommunity> findPublishCommunityList(IRequest request, MgtUserCommunity mgtUserCommunity, int page, int pagesize){
		PageHelper.startPage(page, pagesize);
		return mgtUserCommunityMapper.findPublishCommunityList(mgtUserCommunity);
	}

	/**
	 * 查询人员列表(分页)
	 * @author bubu
	 * @param mgtUserCommunity
	 * @return
	 */
	@Override
	public List<MgtUserCommunity> findPublishUserList(IRequest request, MgtUserCommunity mgtUserCommunity, int page, int pagesize){
		PageHelper.startPage(page, pagesize);
		return mgtUserCommunityMapper.findPublishUserList(mgtUserCommunity);
	}

	/**
	 * 查询人员列表
	 * @author bubu
	 * @param mgtUserCommunity
	 * @return
	 */
	@Override
	public List<MgtUserCommunity> findPublishUserList(MgtUserCommunity mgtUserCommunity){
		return mgtUserCommunityMapper.findPublishUserList(mgtUserCommunity);
	}

	/**
	 * 根据物管员工id查询关联的项目
	 * @param mgtUserCommunity
	 * @return
	 */
	@Override
	public List<MgtUserCommunity> findCommunityListByMgtUserId(IRequest request, MgtUserCommunity mgtUserCommunity, int page, int pagesize){
		PageHelper.startPage(page, pagesize);
		return mgtUserCommunityMapper.findCommunityListByMgtUserId(mgtUserCommunity);
	}

    @Override
    public List<MgtUserCommunity> queryCommunitiesNoAuth(MgtUserCommunity dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return mgtUserCommunityMapper.queryCommunitiesNoAuth(dto);
    }
	
}