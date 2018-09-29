package com.cmig.future.csportal.module.operate.cms.controllers;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cmig.future.csportal.module.operate.cms.dto.Article;
import com.cmig.future.csportal.module.operate.cms.service.IArticleService;
import com.hand.hap.core.IRequest;
import com.hand.hap.system.controllers.BaseController;
import com.hand.hap.system.dto.ResponseData;

@Controller
public class ArticleController extends BaseController {

    @Autowired
    private IArticleService articleService;


    @RequestMapping(value = "/cms/article/query")
    @ResponseBody
    public ResponseData query(Article dto, @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                              @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(articleService.select(requestContext, dto, page, pageSize));
    }

    @RequestMapping(value = "/cms/article/submit")
    @ResponseBody
    public ResponseData update(HttpServletRequest request, @RequestBody List<Article> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(articleService.batchUpdate(requestCtx, dto));
    }
    
    @RequestMapping(value = "/cms/article/saveOrUpdate")
    @ResponseBody
    public ResponseData saveOrUpdate(HttpServletRequest request, @RequestBody List<Article> dto) {
        IRequest requestCtx = createRequestContext(request);
        return new ResponseData(articleService.saveOrUpdate(requestCtx, dto));
    }

    @RequestMapping(value = "/cms/article/remove")
    @ResponseBody
    public ResponseData delete(HttpServletRequest request, @RequestBody List<Article> dto) {
        articleService.batchDelete(dto);
        return new ResponseData();
    }

    @RequestMapping(value = "/cms/article/queryByCategory")
    @ResponseBody
    public ResponseData queryByCategory(Article article,
                                        @RequestParam(defaultValue = DEFAULT_PAGE) int page,
                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pagesize,
                                        HttpServletRequest request) {
        IRequest requestContext = createRequestContext(request);
        return new ResponseData(articleService.getArticleByCategory(requestContext, article, page, pagesize));
    }

    @RequestMapping(value = "/cms/article/queryById")
    @ResponseBody
    public ResponseData queryById(HttpServletRequest request, @RequestParam(required = true) String id) {
        IRequest requestContext = createRequestContext(request);
        List<Article> list = new ArrayList<Article>();
        Article article = articleService.getArticleById(requestContext, id);
        list.add(article);
        return new ResponseData(list);
    }
    
//    @RequestMapping(value = "/cms/article/updateArticleByFlag")
//    @ResponseBody
//    public ResponseData updateArticleByFlag(HttpServletRequest request, @RequestParam(required = true) String id) {
//        IRequest requestCtx = createRequestContext(request);
//        int result = articleService.updateArticleByFlag(requestCtx, id);
//        System.out.println("delete::" + result);
//        return new ResponseData();
//    }
    
    @RequestMapping(value = "/cms/article/updateArticle")
    @ResponseBody
    public ResponseData updateArticle(HttpServletRequest request, Article article) {
        IRequest requestCtx = createRequestContext(request);
        //article.setDelFlag(article.DEL_FLAG_DELETE);
        articleService.updateByPrimaryKeySelective(requestCtx, article);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/cms/article/publish")
    @ResponseBody
    public ResponseData publishArticle(HttpServletRequest request, Article article) {
        IRequest requestCtx = createRequestContext(request);
        articleService.publishArticle(requestCtx, article);
        return new ResponseData();
    }
    
    @RequestMapping(value = "/cms/article/unpublish")
    @ResponseBody
    public ResponseData unpublishArticle(HttpServletRequest request, Article article) {
        IRequest requestCtx = createRequestContext(request);
        articleService.unpublishArticle(requestCtx, article);
        return new ResponseData();
    }


}