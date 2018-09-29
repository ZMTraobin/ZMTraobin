package com.cmig.future.csportal.module.operate.cms.service;

import com.cmig.future.csportal.module.operate.cms.dto.Article;
import com.hand.hap.core.IRequest;
import com.hand.hap.core.ProxySelf;
import com.hand.hap.system.service.IBaseService;

import java.util.List;

public interface IArticleService extends IBaseService<Article>, ProxySelf<IArticleService>{

    /**
     * 根据文章ID查询文章详情,页面维护
     * @param requestContext
     * @param id
     * @return
     */
    Article getArticleById(IRequest requestContext, String id);

    /**
     * 根据栏目类型查询文章列表
     * @param requestContext
     * @param article
     * @param page
     * @param pagesize
     * @return
     */
    List<Article> getArticleByCategory(IRequest requestContext, Article article, int page, int pagesize);

    /**
     * 更新或保存文章
     * @param requestCtx
     * @param dto 文章list
     * @return
     */
    List<Article> saveOrUpdate(IRequest requestCtx, List<Article> dto);

    
    /**
     * 根据文章ID查询文章详情 App接口
     * @param requestContext
     * @param articleId
     * @return
     */
    Article getArticleAppById(IRequest requestContext, String articleId);

    /**
     * 发布文章
     * @param requestCtx
     * @param article
     * @return
     */
    Article publishArticle(IRequest requestCtx, Article article);
    
    /**
     * 下线文章
     * @param requestCtx
     * @param article
     * @return
     */
    Article unpublishArticle(IRequest requestCtx, Article article);

    Article getByContentType(IRequest requestContext, String contentType);

    /**
     * 根据内容类型和社区查询文章列表，App接口
     * @param requestContext
     * @param contentTypeStr
     * @param communityId
     * @return
     */
    List<Article> getArticleListByCommunity(IRequest requestContext, String contentTypeStr, String communityId,
                                            int page, int pageSize);

	/**
	 * 查询绑卡服务协议
	 * @return
	 */
	List<Article> getCardProtocol();

    /**
     * 新增公告接口service
     * @param article
     */
    String addInterface( Article article );

    void updateInterface( Article article );

    void deleteInterface( Article article );

    void offlineInterface( Article article );

    void onlineInterface( Article article );

    List<Article> getListInterface( Article article, Integer page, Integer pageSize );
}