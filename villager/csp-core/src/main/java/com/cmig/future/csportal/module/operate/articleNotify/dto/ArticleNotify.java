package com.cmig.future.csportal.module.operate.articleNotify.dto;

import javax.persistence.Id;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 通知公告Entity
 * @author bubu
 * @version 2017年4月5日
 */
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_article_notify")
public class ArticleNotify extends BaseExtDTO {

    //通知公告发布状态:草稿/已发布
    //public static final String STATUS_DRAFT = "DRAFT";
    //public static final String STATUS_PUBLISHED = "PUBLISHED";

     @Id
      private String id;

      private String informationNumber;

     @NotEmpty
      private String title;

      private String content;

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

      private String status;

    @Transient
    private String createdByName; //创建者

    private Long lastUpdateBy;//更新人

    private Date lastUpdateDate;//更新时间

    public String getCreatedByName() {
        return createdByName;
    }

    public void setCreatedByName(String createdByName) {
        this.createdByName = createdByName;
    }

    public Long getLastUpdateBy() {
        return lastUpdateBy;
    }

    public void setLastUpdateBy(Long lastUpdateBy) {
        this.lastUpdateBy = lastUpdateBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public void setId(String id){
         this.id = id;
     }

     public String getId(){
         return id;
     }

     public void setInformationNumber(String informationNumber){
         this.informationNumber = informationNumber;
     }

     public String getInformationNumber(){
         return informationNumber;
     }

     public void setTitle(String title){
         this.title = title;
     }

     public String getTitle(){
         return title;
     }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setLink(String link){
         this.link = link;
     }

     public String getLink(){
         return link;
     }

     public void setColor(String color){
         this.color = color;
     }

     public String getColor(){
         return color;
     }

     public void setKeywords(String keywords){
         this.keywords = keywords;
     }

     public String getKeywords(){
         return keywords;
     }

     public void setDescription(String description){
         this.description = description;
     }

     public String getDescription(){
         return description;
     }

     public void setWeight(Long weight){
         this.weight = weight;
     }

     public Long getWeight(){
         return weight;
     }

     public void setWeightDate(Date weightDate){
         this.weightDate = weightDate;
     }

     public Date getWeightDate(){
         return weightDate;
     }

     public void setHits(Long hits){
         this.hits = hits;
     }

     public Long getHits(){
         return hits;
     }

     public void setPosid(String posid){
         this.posid = posid;
     }

     public String getPosid(){
         return posid;
     }

     public void setRemarks(String remarks){
         this.remarks = remarks;
     }

     public String getRemarks(){
         return remarks;
     }

     public void setDelFlag(String delFlag){
         this.delFlag = delFlag;
     }

     public String getDelFlag(){
         return delFlag;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     }
