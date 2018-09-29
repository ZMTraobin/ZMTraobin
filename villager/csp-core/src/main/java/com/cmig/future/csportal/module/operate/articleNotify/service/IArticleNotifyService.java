package com.cmig.future.csportal.module.operate.articleNotify.service;

import com.cmig.future.csportal.module.operate.articleNotify.dto.ArticleNotify;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IArticleNotifyService extends IBaseService<ArticleNotify>, ProxySelf<IArticleNotifyService>{

    /**
     * 通知公告分页列表
     * @param requestContext
     * @param articleNotify
     * @param page
     * @param pagesize
     * @return
     */
    public List<ArticleNotify> selectArticleNotify(IRequest requestContext, ArticleNotify articleNotify, int page, int pagesize);

    /**
     * 保存:添加or更新
     * @param requestCtx
     * @param dto
     * @return
     */
    public List<ArticleNotify> saveOrUpdate(IRequest requestCtx, List<ArticleNotify> dto);

    /**
     * 根据id查询通知公告
     * @param id
     * @return
     */
    public ArticleNotify queryArticleNotifyById(String id);

    /**
     * 根据id删除通知公告
     * @param articleNotify
     * @return
     */
    public int deleteArticleNotifyById(ArticleNotify articleNotify);
}