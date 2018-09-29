package com.cmig.future.csportal.module.sys.sysattachment.service;

import com.cmig.future.csportal.module.sys.sysattachment.dto.SysAttachment;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface ISysAttachmentService extends IBaseService<SysAttachment>, ProxySelf<ISysAttachmentService>{

    /**
     * 保存:添加or更新
     * @param requestCtx
     * @param dto
     * @return
     */
    public List<SysAttachment> saveOrUpdate(IRequest requestCtx, List<SysAttachment> dto);

    /**
     * 查询附件列表
     * @param dto
     * @return
     */
    public List<SysAttachment> findList(SysAttachment dto);

}