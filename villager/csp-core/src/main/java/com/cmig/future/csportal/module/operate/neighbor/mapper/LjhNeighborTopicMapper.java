package com.cmig.future.csportal.module.operate.neighbor.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.cmig.future.csportal.module.operate.neighbor.dto.LjhNeighborTopic;

public interface LjhNeighborTopicMapper extends Mapper<LjhNeighborTopic>{

    List<LjhNeighborTopic> findList(LjhNeighborTopic dto);
    
    /**
     * 查询typeId是否在使用
     * @param typeId
     * @return
     */
     int findTag(@Param("typeId") String typeId);
     
     int delete(LjhNeighborTopic dto);

}