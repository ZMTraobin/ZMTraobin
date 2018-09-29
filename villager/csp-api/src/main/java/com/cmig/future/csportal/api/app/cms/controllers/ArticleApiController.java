package com.cmig.future.csportal.api.app.cms.controllers;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.common.utils.DateUtils;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.operate.cms.dto.Article;
import com.cmig.future.csportal.module.operate.cms.service.IArticleService;
import com.github.pagehelper.Page;
import com.hand.hap.core.IRequest;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping(value = "${userPath}")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ArticleApiController extends BaseExtendController {

    @Autowired
    private IArticleService articleService;

    /**
     * app文章列表接口
     * 
     * @return
     */
    @RequestMapping(value = "/operate/cms/article/list", produces = { "application/json" }, method = RequestMethod.POST)
    @ResponseBody
    public String getArticleListByCommunity(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = true) String contentTypeStr, @RequestParam(required = false) String communityId,
            @RequestParam(defaultValue = DEFAULT_PAGE) int page,
            @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
        IRequest requestContext = createRequestContext(request);
	    RetApp result = getArticleList(contentTypeStr, communityId, page, pageSize, requestContext);
        return renderString(response, result);
    }

    @RequestMapping(value = "/operate/cms/article/detail", produces = {
            "application/json" }, method = RequestMethod.POST)
    @ResponseBody
    public RetApp getArticleById(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(required = true) String articleId) {
        IRequest requestContext = createRequestContext(request);
        RetApp result = new RetApp();
        Article article = articleService.getArticleAppById(requestContext, articleId);
        JSONObject jsonObject = new JSONObject();
	    jsonObject.put("hits", article.getHits());
	    jsonObject.put("title", article.getTitle());
        jsonObject.put("content", article.getContent());
        jsonObject.put("publishedDate", article.getPublishedDate() == null ? "": DateUtils.formatDate(article.getPublishedDate(),"yyyy-MM-dd"));
        result.setData(jsonObject);
        result.setStatus(OK);
        result.setMessage("查询成功!");
        return result;
    }

    /**
     * 根据内容类型得到文章：1.用户协议。2.关于我们
     * 
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/operate/cms/article/getByContentType", produces = {"application/json" }, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public RetApp getByContentType(HttpServletRequest request, HttpServletResponse response,@RequestParam(required = true) String contentType) {
	    try {
		    IRequest requestContext = createRequestContext(request);
		    Article protocol = articleService.getByContentType(requestContext,contentType);
		    JSONObject jsonObject = new JSONObject();
		    jsonObject.put("categoryName", protocol.getCategoryName());
		    jsonObject.put("title", protocol.getTitle());
		    jsonObject.put("content", protocol.getContent());
		    return RetAppUtil.success(jsonObject,"查询成功!");
	    }catch (ServiceException e){
		    return RetAppUtil.error(e);
	    }catch (Exception e){
		    e.printStackTrace();
		    return RetAppUtil.unKonwError();
	    }
    }

	/**
	 * 物管端公告列表接口
	 *
	 * @return
	 */
	@RequestMapping(value = "/operate/cms/article/mgt/list", produces = { "application/json" }, method = RequestMethod.POST)
	@ResponseBody
	public String getArticleMgtListByCommunity(HttpServletRequest request, HttpServletResponse response,
	                                        @RequestParam(required = true) String contentTypeStr, @RequestParam(required = true) String communityId,
	                                        @RequestParam(defaultValue = DEFAULT_PAGE) int page,
	                                        @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize) {
		IRequest requestContext = createRequestContext(request);
		RetApp result = getArticleList(contentTypeStr, communityId, page, pageSize, requestContext);
		return renderString(response, result);
	}

	private RetApp getArticleList(@RequestParam(required = true) String contentTypeStr, @RequestParam(required = false) String communityId, @RequestParam(defaultValue = DEFAULT_PAGE) int page, @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) int pageSize, IRequest requestContext) {
		RetApp result = new RetApp();
		List<Article> list = articleService.getArticleListByCommunity(requestContext, contentTypeStr, communityId,page,pageSize);
		List<JSONObject> objList = new ArrayList<JSONObject>();
		if (!list.isEmpty()) {
			for (Article article : list) {
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("articleId", article.getId());
				jsonObject.put("informationNumber", article.getInformationNumber());
				jsonObject.put("title", article.getTitle());
                jsonObject.put("cover", Global.getFullImagePath(article.getCover()));
                jsonObject.put("video", Global.getFullImagePath(article.getVideo()));
                jsonObject.put("link", article.getLink());
				jsonObject.put("hits", article.getHits());
				jsonObject.put("status", article.getStatus());
				jsonObject.put("categoryName", article.getCategoryName());
				jsonObject.put("parentCategoryName", article.getParentCategoryName());
				jsonObject.put("parentCategoryId", article.getParentCategoryId());
				jsonObject.put("categoryId", article.getCategoryId());
				jsonObject.put("contentType", article.getContentType());
				jsonObject.put("contentTypeDesc", article.getContentTypeDesc());
				jsonObject.put("tag", article.getTag());
				jsonObject.put("communityId", article.getCommunityId());
				jsonObject.put("keywords", article.getKeywords());
				jsonObject.put("description", article.getDescription());
				jsonObject.put("weight", article.getWeight());
				jsonObject.put("weightDate", article.getWeightDate() == null ? "": DateUtils.formatDate(article.getWeightDate(),"yyyy-MM-dd"));
				jsonObject.put("posid", article.getPosid());
				jsonObject.put("publishedDate", article.getPublishedDate() == null ? "": DateUtils.formatDate(article.getPublishedDate(),"yyyy-MM-dd"));
				objList.add(jsonObject);
			}
		}
		result.setData(objList);
		result.setStatus(OK);
		result.setMessage("查询成功!");
		result.setTotall((long) list.size());

		if (list != null&&list instanceof Page) {
			result.setTotall(Long.valueOf(((Page)list).getTotal()));
		}
		return result;
	}
}
