package com.cmig.future.csportal.module.operate.cms.dto;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_cms_article")
public class Article extends BaseExtDTO {

    private static final long serialVersionUID = -6676213883242218397L;

    @Id
    // @GeneratedValue
    private String id;

    private String categoryId;

    @NotEmpty
    private String title;

    private String cover;
    private String video;

    private Date publishedDate;

    private String link;

    private String color;

    private String keywords;

    private String description;

    private Long weight;

    private Date weightDate;

    private Long hits;

    private String posid;

    private String remarks;

    private String delFlag;

    private Long informationNumber;

    private String status;

    @Transient
    private String categoryName;

    @Transient
    private String content;

    @Transient
    private String contentType;

    @Transient
    private String publishedByName;

    @Transient
    private Date creationDate;

    @Transient
    private Date lastUpdateDate;
    
    @Transient
    private String statusDesc;
    
    @Transient
    private String articleId;
    
    @Transient
    private String contentTypeDesc;
    
    private String tag;
    
    @Transient
    private String parentCategoryName;
    
    @Transient
    private String parentCategoryId;
    
    @Transient
    private String communityId;

    @Transient
    private String appid;
    @Transient
    private String sourceSystemCommunityId;
    @Transient
    private String sign;

    public String getSign() {
        return sign;
    }

    public void setSign( String sign ) {
        this.sign = sign;
    }

    public String getAppid() {
        return appid;
    }

    public void setAppid( String appid ) {
        this.appid = appid;
    }

    public String getSourceSystemCommunityId() {
        return sourceSystemCommunityId;
    }

    public void setSourceSystemCommunityId( String sourceSystemCommunityId ) {
        this.sourceSystemCommunityId = sourceSystemCommunityId;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }

    public String getParentCategoryName() {
        return parentCategoryName;
    }

    public void setParentCategoryName(String parentCategoryName) {
        this.parentCategoryName = parentCategoryName;
    }

     public String getParentCategoryId() {
        return parentCategoryId;
    }

    public void setParentCategoryId(String parentCategoryId) {
        this.parentCategoryId = parentCategoryId;
    }

    public String getContentTypeDesc() {
        return contentTypeDesc;
    }

    public void setContentTypeDesc(String contentTypeDesc) {
        this.contentTypeDesc = contentTypeDesc;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getArticleId() {
        return articleId;
    }

    public void setArticleId(String articleId) {
        this.articleId = articleId;
    }

    public String getStatusDesc() {
        return statusDesc;
    }

    public void setStatusDesc(String statusDesc) {
        this.statusDesc = statusDesc;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getPublishedByName() {
        return publishedByName;
    }

    public void setPublishedByName(String publishedByName) {
        this.publishedByName = publishedByName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getLink() {
        return link;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getKeywords() {
        return keywords;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setWeight(Long weight) {
        this.weight = weight;
    }

    public Long getWeight() {
        return weight;
    }

    public void setWeightDate(Date weightDate) {
        this.weightDate = weightDate;
    }

    public Date getWeightDate() {
        return weightDate;
    }

    public void setHits(Long hits) {
        this.hits = hits;
    }

    public Long getHits() {
        return hits;
    }

    public void setPosid(String posid) {
        this.posid = posid;
    }

    public String getPosid() {
        return posid;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public Long getInformationNumber() {
        return informationNumber;
    }

    public void setInformationNumber(Long informationNumber) {
        this.informationNumber = informationNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getVideo() {
        return video;
    }

    public void setVideo(String video) {
        this.video = video;
    }
}
