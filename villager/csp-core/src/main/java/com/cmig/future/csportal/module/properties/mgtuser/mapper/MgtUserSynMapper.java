package com.cmig.future.csportal.module.properties.mgtuser.mapper;

import com.cmig.future.csportal.module.properties.mgtuser.dto.MgtUserSyn;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface MgtUserSynMapper extends Mapper<MgtUserSyn>{

    /**
     * 根据sourceId和sourceSystem查询
     * @param mgtUserSyn
     * @return
     */
    public List<MgtUserSyn> findList(MgtUserSyn mgtUserSyn);
    
    /**
     * 根据sourceSystem和sourceId 查询集合
     * @param mgtUserSyn
     * @return
     */
    public List<MgtUserSyn> checkSourceAndSystemId(MgtUserSyn mgtUserSyn);

    /**
     * 插入映射关系
     * @param mgtUserSyn
     */
    public void insertMgtUserSyn(MgtUserSyn mgtUserSyn);

}