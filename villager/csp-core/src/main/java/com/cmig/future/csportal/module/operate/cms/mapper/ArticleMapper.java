package com.cmig.future.csportal.module.operate.cms.mapper;

import com.cmig.future.csportal.module.operate.cms.dto.Article;
import com.hand.hap.core.IRequest;
import com.hand.hap.mybatis.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface ArticleMapper extends Mapper<Article> {

    Article getArticleById(IRequest requestContext, @Param(value = "id") String id);

    List<Article> getArticleByCategory(Article article);

    List<Article> getArticleListByCommunity(Map<String, Object> params);

    Article getArticleAppById(IRequest requestContext, @Param(value = "articleId") String articleId);

    Article getByContentType(String contentType);
   
    // 获取搜索需求字段
    List<Article> getSearchField();


	/**
	 * 查询绑卡服务协议
	 * @return
	 */
	List<Article> getCardProtocol();

    List<Article> getListInterface( Article article );

    void updateHits(@Param(value = "articleId") String articleId);
}