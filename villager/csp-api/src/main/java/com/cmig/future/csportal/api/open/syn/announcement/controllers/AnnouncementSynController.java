package com.cmig.future.csportal.api.open.syn.announcement.controllers;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.cmig.future.csportal.module.base.controllers.BaseExtendController;
import com.cmig.future.csportal.module.base.entity.RetApp;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.base.utils.RetAppUtil;
import com.cmig.future.csportal.module.operate.cms.dto.Article;
import com.cmig.future.csportal.module.operate.cms.service.IArticleService;
import com.cmig.future.csportal.module.sys.code.CodeUtil;
import com.github.pagehelper.Page;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:17 2017/9/27.
 * @Modified by zhangtao on 11:17 2017/9/27.
 */
@Controller
@ResponseBody
@RequestMapping(value = "${commonPath}/")
public class AnnouncementSynController extends BaseExtendController {

	private static Logger logger = LoggerFactory.getLogger(AnnouncementSynController.class);

	@Autowired
	private IArticleService articleService;

	/**
	 * 添加
	 *
	 * @param article
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syn/announcement/add", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp add( @RequestBody Article article, HttpServletRequest request, HttpServletResponse response) throws  Exception{
		String articleId = articleService.addInterface(article);
		JSONObject obj = new JSONObject();
		obj.put("articleId",articleId);
		return RetAppUtil.success(obj,"保存成功");
	}

	/**
	 * 删除
	 *
	 * @param article
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syn/announcement/delete", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp delete(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response) throws Exception{
		articleService.deleteInterface(article);
		return RetAppUtil.success("删除成功");
	}

	/**
	 * 更新
	 *
	 * @param article
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syn/announcement/update", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp update(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response) throws Exception {
		articleService.updateInterface(article);
		return RetAppUtil.success("更新成功");
	}

	/**
	 * 查询集合
	 *
	 * @param article
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syn/announcement/list", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp getList(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response,@RequestParam(defaultValue = DEFAULT_PAGE) Integer page,
						  @RequestParam(defaultValue = DEFAULT_PAGE_SIZE) Integer pageSize) throws Exception {
		List<Article> list = articleService.getListInterface(article, page, pageSize);
		JSONArray jsonArray=new JSONArray();
		for(Article notice : list){
			JSONObject jsonObject=new JSONObject();
			jsonObject.put("articleId",notice.getId());
			jsonObject.put("title",notice.getTitle());
			jsonObject.put("content",notice.getContent());
			String statusDesc = CodeUtil.getDictLabel(notice.getStatus(),"CSP.CMS.ARTICLE_STATUS","","");
			jsonObject.put("status",statusDesc);
			jsonObject.put("publishedDate",notice.getPublishedDate());
			jsonObject.put("lastUpdateDate",notice.getLastUpdateDate());
			jsonArray.add(jsonObject);
		}

		Long total=0l;
		if (list != null&&list instanceof Page) {
			total=Long.valueOf(((Page)list).getTotal());
		}
		return RetAppUtil.success(jsonArray,"查询成功",total);
	}

	/**
	 * 上线
	 *
	 * @param article
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syn/announcement/online", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp online(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response) throws Exception {
		articleService.onlineInterface(article);
		return RetAppUtil.success("上线成功");
	}

	/**
	 * 下线
	 *
	 * @param article
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/syn/announcement/offline", produces = {"application/json"}, method = RequestMethod.POST)
	public RetApp offline(@RequestBody Article article, HttpServletRequest request, HttpServletResponse response) throws Exception{
		articleService.offlineInterface(article);
		return RetAppUtil.success("下线成功");
	}

}
