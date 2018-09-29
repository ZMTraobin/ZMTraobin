package com.cmig.future.csportal.module.sys.city.mapper;

import com.cmig.future.csportal.module.sys.city.dto.LjhSysArea;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface LjhSysAreaMapper extends Mapper<LjhSysArea> {

    /**
     * 城市列表
     * @return
     */
    List<LjhSysArea> cityList();

    /**
     * 根据城市id查询城市名称
     * @return
     */
    LjhSysArea queryCityNameById(String id);

    /**
     * 根据类型获取城市列表
     * @return
     */
    public List<LjhSysArea>getListByType(LjhSysArea dto);

	/**
	 * 查询存在小区的城市列表
	 * @param data
	 * @return
	 */
	List<LjhSysArea> findCityWithCommunity(LjhSysArea data);

	List<LjhSysArea> findAreaList(LjhSysArea data);
}