package com.cmig.future.csportal.module.sys.city.service;


import com.cmig.future.csportal.module.sys.city.dto.LjhSysArea;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface ILjhSysAreaService extends IBaseService<LjhSysArea>, ProxySelf<ILjhSysAreaService> {

    /**
     * 城市列表
     * @return
     */
    public List<LjhSysArea> cityList();

    /**
     * 根据城市id查询城市名称
     * @return
     */
    public LjhSysArea queryCityNameById(String id);

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