package com.cmig.future.csportal.module.sys.openinfo.service.impl;

import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.cmig.future.csportal.module.sys.openinfo.mapper.OpenAppInfoMapper;
import com.cmig.future.csportal.module.sys.openinfo.service.IOpenAppInfoService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OpenAppInfoServiceImpl extends BaseServiceImpl<OpenAppInfo> implements IOpenAppInfoService{
    
	@Autowired
	private OpenAppInfoMapper openAppInfoMapper;
	
	
//	@Override
//	public OpenAppInfo get(String id) {
//		
//		return super.mapper.selectByPrimaryKey(id);
//		
//	}

	@Override
	public List<OpenAppInfo> findList(OpenAppInfo openAppInfo) {
		return super.mapper.select(openAppInfo);
	}

	/**
	 * 分页查询
	 * @param dto
	 * @param page
	 * @param pageSize
     * @return
     */
	@Override
	public List<OpenAppInfo> selectOppInfo(IRequest requestContext,OpenAppInfo dto, int page, int pageSize) {
		PageHelper.startPage(page,pageSize);
        return openAppInfoMapper.selectOppInfo(dto);
	}

	/**
	 * 根据id查询对象
	 * @param dto
	 * @return
     */
	@Override
	public OpenAppInfo getOpenInfoById(OpenAppInfo dto) {
		return openAppInfoMapper.getOpenInfoById(dto);
	}

	/**
	 * 添加商户接入
	 * @param dto
     */
	@Override
	public void save(OpenAppInfo dto) {
		super.mapper.insertSelective(dto);
	}

}