package com.cmig.future.csportal.module.operate.appUpgrade.mapper;

import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

import com.cmig.future.csportal.module.operate.appUpgrade.dto.LjhAppDownload;

public interface LjhAppDownloadMapper extends Mapper<LjhAppDownload>{

    List<LjhAppDownload> selectApp(LjhAppDownload dto);

    LjhAppDownload findLastVersionApp(LjhAppDownload appDownload);

    List<LjhAppDownload> findList(LjhAppDownload appDownload);

    List<LjhAppDownload> findListByNameAndType(LjhAppDownload appDownload);
}