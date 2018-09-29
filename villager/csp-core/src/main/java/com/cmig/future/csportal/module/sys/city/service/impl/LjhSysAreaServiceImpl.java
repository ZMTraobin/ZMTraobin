package com.cmig.future.csportal.module.sys.city.service.impl;


import com.cmig.future.csportal.module.sys.city.dto.LjhSysArea;
import com.cmig.future.csportal.module.sys.city.mapper.LjhSysAreaMapper;
import com.cmig.future.csportal.module.sys.city.service.ILjhSysAreaService;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class LjhSysAreaServiceImpl extends BaseServiceImpl<LjhSysArea> implements ILjhSysAreaService {

    @Autowired
    private LjhSysAreaMapper ljhSysAreaMapper;

    @Override
    public List<LjhSysArea> cityList() {
        return ljhSysAreaMapper.cityList();
    }

    @Override
    public LjhSysArea queryCityNameById(String id) {
        return ljhSysAreaMapper.queryCityNameById(id);
    }

    /**
     * 根据类型获取城市列表
     * @return
     */
    @Override
    public List<LjhSysArea> getListByType(LjhSysArea dto) {
        return ljhSysAreaMapper.getListByType(dto);
    }

	@Override
	public List<LjhSysArea> findCityWithCommunity(LjhSysArea data) {
		return ljhSysAreaMapper.findCityWithCommunity(data);
	}

	@Override
	public List<LjhSysArea> findAreaList(LjhSysArea data) {
		return ljhSysAreaMapper.findAreaList(data);
	}
}