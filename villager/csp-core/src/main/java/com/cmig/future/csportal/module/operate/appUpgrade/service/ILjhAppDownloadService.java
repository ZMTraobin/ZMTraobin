package com.cmig.future.csportal.module.operate.appUpgrade.service;

import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

import com.cmig.future.csportal.module.operate.appUpgrade.dto.LjhAppDownload;

public interface ILjhAppDownloadService extends IBaseService<LjhAppDownload>, ProxySelf<ILjhAppDownloadService>{

    List<LjhAppDownload> selectApp(IRequest requestContext, LjhAppDownload dto, int page, int pageSize);

    List<LjhAppDownload> saveOrUpdate(IRequest requestCtx, List<LjhAppDownload> dto);

    LjhAppDownload findLastVersionApp(LjhAppDownload appDownload);

    List<LjhAppDownload> findList(IRequest requestCtx, LjhAppDownload appDownload, int page, int pageSize);
}