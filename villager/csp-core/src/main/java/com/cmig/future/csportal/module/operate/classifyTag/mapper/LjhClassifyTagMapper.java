package com.cmig.future.csportal.module.operate.classifyTag.mapper;

import java.util.List;

import com.cmig.future.csportal.module.operate.classifyTag.dto.LjhClassifyTag;
import com.hand.hap.mybatis.common.Mapper;

public interface LjhClassifyTagMapper extends Mapper<LjhClassifyTag>{
    
    List<LjhClassifyTag> select(LjhClassifyTag ljhClassifyTag);

}