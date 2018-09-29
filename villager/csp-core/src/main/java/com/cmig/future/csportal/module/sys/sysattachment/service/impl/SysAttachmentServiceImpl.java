package com.cmig.future.csportal.module.sys.sysattachment.service.impl;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.sys.sysattachment.dto.SysAttachment;
import com.cmig.future.csportal.module.sys.sysattachment.mapper.SysAttachmentMapper;
import com.cmig.future.csportal.module.sys.sysattachment.service.ISysAttachmentService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SysAttachmentServiceImpl extends BaseServiceImpl<SysAttachment> implements ISysAttachmentService {

    private final Logger logger = LoggerFactory.getLogger(SysAttachmentServiceImpl.class);

    @Autowired
    private SysAttachmentMapper sysAttachmentMapper;

    /**
     * 保存:添加or更新
     * @param requestCtx
     * @param dto
     * @return
     */
    public List<SysAttachment> saveOrUpdate(IRequest requestCtx, List<SysAttachment> dto){
        logger.info("dto对象是:{}", JSONUtils.valueToString(dto));
        if (dto == null && StringUtils.isEmpty(dto)) {
            return dto;
        }
        for(SysAttachment sysAttachment : dto){
            String status = sysAttachment.get__status();
            sysAttachment.setCreatedBy(requestCtx.getUserId());
            String fileName = sysAttachment.getFileName();
            String fileType = "";
            if(fileName.indexOf(".") != -1){
                fileType = getFileExt(fileName);
            }
            sysAttachment.setFileType(fileType);
            String id = sysAttachment.getId();
            if (id == null || id == "") {
                sysAttachment.setId(IdGen.uuid());
                sysAttachment.setLastUpdatedBy(requestCtx.getUserId());
                sysAttachmentMapper.insertSelective(sysAttachment);
            } else{
                sysAttachmentMapper.updateByPrimaryKeySelective(sysAttachment);
            }
        }
        return dto;
    }
    /**
     * 查询附件列表
     * @param dto
     * @return
     */
    public List<SysAttachment> findList(SysAttachment dto){
        return sysAttachmentMapper.findList(dto);
    }

    /**
     * 获得文件的扩展名
     *
     * @param fileName
     * @return
     */
    private String getFileExt(String fileName) {
        String fileExt = "";
        int index = fileName.lastIndexOf(".");
        fileExt = fileName.substring(index+1, fileName.length());
        return fileExt;
    }
}