package com.cmig.future.csportal.module.operate.cms.service.impl;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.oauth2.utils.OAuthUtils;
import com.cmig.future.csportal.common.utils.IdGen;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import com.cmig.future.csportal.module.operate.cms.dto.Article;
import com.cmig.future.csportal.module.operate.cms.dto.ArticleData;
import com.cmig.future.csportal.module.operate.cms.mapper.ArticleDataMapper;
import com.cmig.future.csportal.module.operate.cms.mapper.ArticleMapper;
import com.cmig.future.csportal.module.operate.cms.mapper.CategoryMapper;
import com.cmig.future.csportal.module.operate.cms.service.IArticleService;
import com.cmig.future.csportal.module.properties.community.dto.BaseCommunity;
import com.cmig.future.csportal.module.properties.community.service.IBaseCommunityService;
import com.cmig.future.csportal.module.properties.communityEvent.dto.LjhEventCommunity;
import com.cmig.future.csportal.module.properties.communityEvent.mapper.LjhEventCommunityMapper;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
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

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class ArticleServiceImpl extends BaseServiceImpl<Article> implements IArticleService {

    private final Logger logger = LoggerFactory.getLogger(CategoryServiceImpl.class);

    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleDataMapper articleDataMapper;
    @Autowired
    private LjhEventCommunityMapper ljhEventCommunityMapper;
    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private IBaseCommunityService baseCommunityService;


    @Override
    public Article getArticleById(IRequest requestContext, String id) {
        return articleMapper.getArticleById(requestContext, id);
    }

    @Override
    public List<Article> getArticleByCategory(IRequest requestContext, Article article, int page, int pagesize) {
        PageHelper.startPage(page, pagesize);
        return articleMapper.getArticleByCategory(article);
    }

    @Override
    @Transactional(propagation = Propagation.REQUIRED)
    public List<Article> saveOrUpdate(IRequest requestCtx, List<Article> articles) {
        // 处理文章内容
        logger.info("dto对象是:{}", JSONUtils.valueToString(articles));
        if (logger.isDebugEnabled()) {
            logger.debug("dto对象是:{}", JSONUtils.valueToString(articles));
        }
        if (articles == null || articles.isEmpty()) {
            return articles;
        }
        for (Article article : articles) {
            ArticleData articleData = new ArticleData();
            String status = article.get__status();
            articleData.setId(article.getId());
            articleData.setContent(article.getContent());
            if (DTOStatus.ADD.equals(status)) {
                String id = IdGen.uuid();
                long infoNumber = IdGen.timeStampId();
                article.setId(id);
                article.setInformationNumber(infoNumber);
                article.setHits(0l);
                articleData.setId(id);
                article.setStatus("DRAFT");
                articleMapper.insertSelective(article);
                articleDataMapper.insertSelective(articleData);
            } else if (DTOStatus.UPDATE.equals(status)) {
                articleMapper.updateByPrimaryKeySelective(article);
                articleDataMapper.updateByPrimaryKeySelective(articleData);
            }
        }
        return articles;
    }

    @Override
    public List<Article> getArticleListByCommunity(IRequest requestContext, String contentTypeStr, String communityId,int page,int pageSize) {
        String[] strArray = contentTypeStr.split(",");
        List<String> contentTypeList = Arrays.asList(strArray);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("contentTypeList", contentTypeList);
        params.put("communityId", communityId);
        PageHelper.startPage(page, pageSize);
        List<Article> list = articleMapper.getArticleListByCommunity(params);
        return list;
    }

	@Override
	public List<Article> getCardProtocol() {
		return articleMapper.getCardProtocol();
	}

    @Override
    @Transactional
    public String addInterface( Article article ) {
        if (StringUtils.isEmpty(article.getAppid())) {
	        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        if (StringUtils.isEmpty(article.getSourceSystemCommunityId())) {
	        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"sourceSystemCommunityId");
        }
        if (StringUtils.isEmpty(article.getTitle())) {
	        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"title");
        }
        if (StringUtils.isEmpty(article.getContent())) {
	        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"content");
        }
        if (StringUtils.isEmpty(article.getContentType())) {
	        throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"contentType");
        }
        String appid = article.getAppid();
        String sign = article.getSign();
        String sourceSystemCommunityId = article.getSourceSystemCommunityId();
        String title = article.getTitle();
        String content = article.getContent();
        String contentType = article.getContentType();

        //校验签名
        Map<String, String> map = new HashMap();
        map.put("appid", appid);
        map.put("sourceSystemCommunityId", sourceSystemCommunityId);
        map.put("contentType", contentType);
        map.put("content", content);
        map.put("title", title);
        OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
        if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
	        throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
        }
        //校验内容类型
        String categoryId = categoryMapper.getCategoryIdByType(contentType);
        if(StringUtils.isEmpty(categoryId)){
	        throw new ServiceException(RestApiExceptionEnums.ARGS_ERROR,"contentType");
        }
        //校验小区
        BaseCommunity communityQuery = new BaseCommunity();
        communityQuery.setSourceSystem(openAppInfo.getAppName());
        communityQuery.setSourceSystemId(sourceSystemCommunityId);
        communityQuery = baseCommunityService.getBySourceSystemId(communityQuery);
        if (communityQuery==null || StringUtils.isEmpty(communityQuery.getId())) {
            throw new DataWarnningException("该项目未同步");
        }
        String communityId = communityQuery.getId();

        article.setCategoryId(categoryId);
        article.setInformationNumber(IdGen.timeStampId());
        String id = IdGen.uuid();
        article.setId(id);
        article.setHits(0l);
        article.setStatus("DRAFT");
        //保存公告，将小区-公告关系保存
        articleMapper.insertSelective(article);
        ArticleData data = new ArticleData();
        data.setId(id);
        data.setContent(content);
        articleDataMapper.insert(data);
        LjhEventCommunity event = new LjhEventCommunity();
        event.setId(IdGen.uuid());
        event.setCommunityId(communityId);
        event.setEventId(id);
        event.setEventType("ARTICLE");
        ljhEventCommunityMapper.insert(event);
        return id;
    }

    @Override
    @Transactional
    public void updateInterface( Article article ) {
        if (StringUtils.isEmpty(article.getAppid())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        if (StringUtils.isEmpty(article.getArticleId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"articleId");
        }
        if (StringUtils.isEmpty(article.getTitle())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"title");
        }
        if (StringUtils.isEmpty(article.getContent())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"content");
        }
        String appid = article.getAppid();
        String sign = article.getSign();
        String articleId = article.getArticleId();
        String title = article.getTitle();
        String content = article.getContent();
        //校验签名
        Map<String, String> map = new HashMap();
        map.put("articleId", articleId);
        map.put("appid", appid);
        map.put("title", title);
        map.put("content", content);
        OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
        if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
            throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
        }
        //更新
        article.setId(articleId);
        articleMapper.updateByPrimaryKeySelective(article);
        ArticleData data = new ArticleData();
        data.setId(articleId);
        data.setContent(content);
        articleDataMapper.updateByPrimaryKeySelective(data);
    }

    @Override
    @Transactional
    public void deleteInterface( Article article ) {
        if (StringUtils.isEmpty(article.getAppid())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        if (StringUtils.isEmpty(article.getArticleId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"articleId");
        }
        String appid = article.getAppid();
        String sign = article.getSign();
        String articleId = article.getArticleId();
        Map<String, String> map = new HashMap();
        map.put("articleId", articleId);
        map.put("appid", appid);
        OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
        if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
            throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
        }
        //删除
        article.setDelFlag(BaseExtDTO.DEL_FLAG_DELETE);
        article.setId(articleId);
        articleMapper.updateByPrimaryKeySelective(article);
    }

    @Override
    @Transactional
    public void offlineInterface( Article article ) {
        if (StringUtils.isEmpty(article.getAppid())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        if (StringUtils.isEmpty(article.getArticleId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"articleId");
        }
        String appid = article.getAppid();
        String sign = article.getSign();
        String articleId = article.getArticleId();
        Map<String, String> map = new HashMap();
        map.put("articleId", articleId);
        map.put("appid", appid);
        OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
        if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
            throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
        }
        article.setStatus("DRAFT");
        article.setId(articleId);
        //更新
        articleMapper.updateByPrimaryKeySelective(article);

    }

    @Override
    @Transactional
    public void onlineInterface( Article article ) {
        if (StringUtils.isEmpty(article.getAppid())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        if (StringUtils.isEmpty(article.getArticleId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"articleId");
        }
        String appid = article.getAppid();
        String sign = article.getSign();
        String articleId = article.getArticleId();
        Map<String, String> map = new HashMap();
        map.put("articleId", articleId);
        map.put("appid", appid);
        OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
        if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
            throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
        }
        article.setStatus("PUBLISHED");
        article.setId(articleId);
        article.setPublishedDate(new Date());
        //更新
        articleMapper.updateByPrimaryKeySelective(article);

    }

    @Override
    public List<Article> getListInterface( Article article, Integer page, Integer pageSize ) {
        if (StringUtils.isEmpty(article.getAppid())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"appid");
        }
        if (StringUtils.isEmpty(article.getSourceSystemCommunityId())) {
            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"sourceSystemCommunityId");
        }
//        if (StringUtils.isEmpty(article.getContentType())) {
//            throw new ServiceException(RestApiExceptionEnums.ARGS_NULL,"contentType");
//        }
        String appid = article.getAppid();
        String sourceSystemCommunityId = article.getSourceSystemCommunityId();
        String contentType = article.getContentType();
        String sign = article.getSign();
        Map<String, String> map = new HashMap();
        map.put("appid", appid);
        map.put("sourceSystemCommunityId", sourceSystemCommunityId);
        map.put("page", page.toString());
        map.put("pageSize", pageSize.toString());
        if (!StringUtils.isEmpty(article.getContentType())) {
            map.put("contentType", contentType);
        }
        OpenAppInfo openAppInfo = OAuthUtils.getOpenAppInfo(appid);
        if (!CspSignUtil.checkSign(map, openAppInfo.getAppSecret(), sign)) {
            throw new ServiceException(RestApiExceptionEnums.SIGN_ERROR);
        }

        BaseCommunity communityQuery = new BaseCommunity();
        communityQuery.setSourceSystem(openAppInfo.getAppName());
        communityQuery.setSourceSystemId(sourceSystemCommunityId);
        communityQuery = baseCommunityService.getBySourceSystemId(communityQuery);
        if (communityQuery==null || StringUtils.isEmpty(communityQuery.getId())) {
            throw new DataWarnningException("该项目未同步");
        }
        String communityId = communityQuery.getId();
        article.setCommunityId(communityId);
        PageHelper.startPage(page,pageSize);
       return articleMapper.getListInterface(article);
    }

    @Override
    public Article getArticleAppById(IRequest requestContext, String articleId) {
        articleMapper.updateHits(articleId);
        Article article = articleMapper.getArticleAppById(requestContext, articleId);
        return article;
    }

    @Override
    public Article publishArticle(IRequest requestCtx, Article article) {
        article.setStatus("PUBLISHED");
        article.setPublishedDate(new Date());
        return self().updateByPrimaryKeySelective(requestCtx,article);
    }
    
    @Override
    public Article unpublishArticle(IRequest requestCtx, Article article) {
        article.setStatus("DRAFT");
        return self().updateByPrimaryKeySelective(requestCtx,article);
    }

    @Override
    public Article getByContentType(IRequest requestContext, String contentType) {
        return articleMapper.getByContentType(contentType);
    }


}