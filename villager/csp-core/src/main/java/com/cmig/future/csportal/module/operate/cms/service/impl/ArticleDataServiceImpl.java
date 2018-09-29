package com.cmig.future.csportal.module.operate.cms.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.cmig.future.csportal.module.operate.cms.dto.ArticleData;
import com.cmig.future.csportal.module.operate.cms.service.IArticleDataService;
import com.hand.hap.system.service.impl.BaseServiceImpl;

@Service
@Transactional
public class ArticleDataServiceImpl extends BaseServiceImpl<ArticleData> implements IArticleDataService{

}