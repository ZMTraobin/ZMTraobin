package com.cmig.future.csportal.module.cooperate.mapper;

import com.cmig.future.csportal.module.cooperate.dto.Cooperate;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface CooperateMapper extends Mapper<Cooperate>{
    /**
     * 保存商户
     * @param cooperate
     */
    public void insertCooperate(Cooperate cooperate);

    /**
     * 根据appUserID查询出该用户的商户
     * @param cooperate
     * @return
     */
    public List<Cooperate> selectCooperateByAppUserId(Cooperate cooperate);

    /**
     * 查询所有的商户
     * @param cooperate
     * @return
     */
    public List<Cooperate>selectAllCooperate(Cooperate cooperate);

}