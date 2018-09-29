package com.cmig.future.csportal.module.job;

import com.alibaba.fastjson.JSON;
import com.cmig.future.csportal.module.operate.cms.dto.Article;
import com.cmig.future.csportal.module.operate.cms.mapper.ArticleMapper;
import com.cmig.future.csportal.module.search.SearchApiInvoking;
import com.hand.hap.job.AbstractJob;
import org.quartz.JobExecutionContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class AddNewsDataJob extends AbstractJob{

	@Autowired
	ArticleMapper articleMapper;
	
	@Override
	public void safeExecute(JobExecutionContext context) throws Exception {
		// 获取新闻数据
		List<Article> article=articleMapper.getSearchField();
		// 转成json
		String jsonstr = JSON.toJSONString(article);
		//添加新闻数据
	    SearchApiInvoking.apiInvoking(SearchApiInvoking.getNewsUrl(), jsonstr);
	}

}
