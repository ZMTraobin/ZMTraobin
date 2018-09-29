package com.cmig.future.csportal.module.operate.appUpgrade.service.impl;

import java.util.List;

import com.cmig.future.csportal.common.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.operate.appUpgrade.dto.LjhAppDownload;
import com.cmig.future.csportal.module.operate.appUpgrade.mapper.LjhAppDownloadMapper;
import com.cmig.future.csportal.module.operate.appUpgrade.service.ILjhAppDownloadService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;

import net.sf.json.util.JSONUtils;

@Service
@Transactional
public class LjhAppDownloadServiceImpl extends BaseServiceImpl<LjhAppDownload> implements ILjhAppDownloadService {

    private final Logger logger = LoggerFactory.getLogger(LjhAppDownloadServiceImpl.class);

    @Autowired
    private LjhAppDownloadMapper ljhAppDownloadMapper;

    @Override
    public List<LjhAppDownload> selectApp(IRequest requestContext, LjhAppDownload dto, int page, int pageSize) {
        PageHelper.startPage(page, pageSize);
        return ljhAppDownloadMapper.selectApp(dto);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<LjhAppDownload> saveOrUpdate(IRequest requestCtx, List<LjhAppDownload> dto) {
        // 处理文章内容
        logger.info("dto对象是:{}", JSONUtils.valueToString(dto));
        if (logger.isDebugEnabled()) {
            logger.debug("dto对象是:{}", JSONUtils.valueToString(dto));
        }
        if (dto == null || dto.isEmpty()) {
            return dto;
        }
        for (LjhAppDownload app : dto) {
            String status = app.get__status();
            app.setDelFlag(app.DEL_FLAG_NORMAL);
            String versionFlag = app.getVersionFlag();
            if (DTOStatus.ADD.equals(status)) {
                String id = IdGen.uuid();
                app.setId(id);
                ljhAppDownloadMapper.insertSelective(app);
            } else if (DTOStatus.UPDATE.equals(status)) {
                ljhAppDownloadMapper.updateByPrimaryKeySelective(app);
            }
            String  id = dto.get(0).getId();
            if((Constants.YES).equals(versionFlag)){//是最新版本
                List<LjhAppDownload> list = ljhAppDownloadMapper.findListByNameAndType(app);
                for(LjhAppDownload ap : list){
                    if(!ap.getId().equals(id)){
                        ap.setVersionFlag(Constants.NO);
                        ljhAppDownloadMapper.updateByPrimaryKeySelective(ap);
                    }
                }
            }
        }
        return dto;
    }

    @Override
    public LjhAppDownload findLastVersionApp(LjhAppDownload appDownload) {
        return ljhAppDownloadMapper.findLastVersionApp(appDownload);
    }

    @Override
    public List<LjhAppDownload> findList(IRequest requestContext, LjhAppDownload dto, int page, int pageSize){
        PageHelper.startPage(page, pageSize);
        return ljhAppDownloadMapper.findList(dto);
    }

}