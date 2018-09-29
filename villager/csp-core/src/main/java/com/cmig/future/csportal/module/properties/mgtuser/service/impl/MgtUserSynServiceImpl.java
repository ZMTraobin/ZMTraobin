package com.cmig.future.csportal.module.properties.mgtuser.service.impl;

import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserSyn;
import com.cmig.future.csportal.module.properties.mgtuser.mapper.MgtUserSynMapper;
import com.cmig.future.csportal.module.properties.mgtuser.service.IMgtUserSynService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class MgtUserSynServiceImpl extends BaseServiceImpl<MgtUserSyn> implements IMgtUserSynService{

	@Autowired
	private MgtUserSynMapper mgtUserSynMapper;
	@Override
	public List<MgtUserSyn> findList(MgtUserSyn mgtUserSyn) {

		return mgtUserSynMapper.findList(mgtUserSyn);
	}

	@Override
	public void save(MgtUserSyn mgtUserSyn) {
		mgtUserSynMapper.insertMgtUserSyn(mgtUserSyn);
		//super.mapper.insertSelective(mgtUserSyn);
	}

	/**
	 * 根据系统id和系统名称判断查询集合
	 */
	@Override
	public List<MgtUserSyn> checkSourceAndSystemId(MgtUserSyn mgtUserSyn) {
		return mgtUserSynMapper.checkSourceAndSystemId(mgtUserSyn);
	}
}