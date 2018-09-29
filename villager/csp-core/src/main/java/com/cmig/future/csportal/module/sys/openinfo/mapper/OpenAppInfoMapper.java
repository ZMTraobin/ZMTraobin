package com.cmig.future.csportal.module.sys.openinfo.mapper;


import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface OpenAppInfoMapper extends Mapper<OpenAppInfo>{

    /**
     * 分页查询
     */

    public List<OpenAppInfo> selectOppInfo(OpenAppInfo dto);

    /**
     *根据id查询对象
     */

    public OpenAppInfo getOpenInfoById(OpenAppInfo dto);

    /**
     * 添加商户接入
     * @param dto
     */
    public void save(OpenAppInfo dto);




}