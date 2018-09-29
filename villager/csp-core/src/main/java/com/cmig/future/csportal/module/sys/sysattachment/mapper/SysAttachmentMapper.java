package com.cmig.future.csportal.module.sys.sysattachment.mapper;

import com.cmig.future.csportal.module.sys.sysattachment.dto.SysAttachment;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface SysAttachmentMapper extends Mapper<SysAttachment> {

    /**
     * 查询附件列表
     * @param dto
     * @return
     */
    List<SysAttachment> findList(SysAttachment dto);
}