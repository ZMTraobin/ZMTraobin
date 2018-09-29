package com.cmig.future.csportal.module.operate.articleNotify.mapper;

import com.cmig.future.csportal.module.operate.articleNotify.dto.ArticleNotify;
import com.hand.hap.mybatis.common.Mapper;

import java.util.List;

public interface ArticleNotifyMapper extends Mapper<ArticleNotify>{

    /**
     * 通知公告列表
     * @param articleNotify
     * @return
     */
    List<ArticleNotify> selectArticleNotify(ArticleNotify articleNotify);

    /**
     * 根据id查询通知公告
     * @param articleNotify
     * @return
     */
    ArticleNotify queryArticleNotifyById(ArticleNotify articleNotify);

    /**
     * 根据id删除通知公告
     * @param articleNotify
     * @return
     */
    int deleteArticleNotifyById(ArticleNotify articleNotify);
}