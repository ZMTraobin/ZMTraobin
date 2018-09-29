package com.cmig.future.csportal.module.operate.articleNotify.service.impl;

import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.operate.articleNotify.dto.ArticleNotify;
import com.cmig.future.csportal.module.operate.articleNotify.mapper.ArticleNotifyMapper;
import com.cmig.future.csportal.module.operate.articleNotify.service.IArticleNotifyService;
import com.github.pagehelper.PageHelper;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.dto.DTOStatus;
import com.hand.hap.system.service.impl.BaseServiceImpl;
import net.sf.json.util.JSONUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ArticleNotifyServiceImpl extends BaseServiceImpl<ArticleNotify> implements IArticleNotifyService {

    private final Logger logger = LoggerFactory.getLogger(ArticleNotifyServiceImpl.class);

    @Autowired
    private ArticleNotifyMapper articleNotifyMapper;

    @Override
    public List<ArticleNotify> selectArticleNotify(IRequest requestContext, ArticleNotify articleNotify, int page, int pagesize){
        PageHelper.startPage(page, pagesize);
        return articleNotifyMapper.selectArticleNotify(articleNotify);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<ArticleNotify> saveOrUpdate(IRequest requestCtx, List<ArticleNotify> articleNotifies) {
        logger.info("dto对象是:{}", JSONUtils.valueToString(articleNotifies));
        if (articleNotifies == null || articleNotifies.isEmpty()) {
            return articleNotifies;
        }
        for(ArticleNotify articleNotify : articleNotifies){
            String status = articleNotify.get__status();
            articleNotify.setDelFlag(BaseExtDTO.DEL_FLAG_NORMAL);
            articleNotify.setCreatedBy(requestCtx.getUserId());
            if (DTOStatus.ADD.equals(status)) {
                articleNotify.setStatus("DRAFT");
                articleNotify.setId(IdGen.uuid());
                articleNotify.setLastUpdatedBy(requestCtx.getUserId());
                articleNotifyMapper.insertSelective(articleNotify);
            } else{
                articleNotifyMapper.updateByPrimaryKeySelective(articleNotify);
            }
        }
        return articleNotifies;
    }

    @Override
    public ArticleNotify queryArticleNotifyById(String id) {
        ArticleNotify articleNotify = new ArticleNotify();
        articleNotify.setId(id);
        return articleNotifyMapper.queryArticleNotifyById(articleNotify);
    }

    @Override
    public int deleteArticleNotifyById(ArticleNotify articleNotify){
        return articleNotifyMapper.deleteArticleNotifyById(articleNotify);
    }
}