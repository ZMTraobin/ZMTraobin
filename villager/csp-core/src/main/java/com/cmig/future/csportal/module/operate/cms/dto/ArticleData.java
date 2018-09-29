package com.cmig.future.csportal.module.operate.cms.dto;

import javax.persistence.Id;
import javax.persistence.Table;

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_cms_article_data")
public class ArticleData extends BaseDTO {
    @Id
    //@GeneratedValue
    private String id;

    private String content;

    private String copyfrom;

    private String relation;

    private String allowComment;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setCopyfrom(String copyfrom) {
        this.copyfrom = copyfrom;
    }

    public String getCopyfrom() {
        return copyfrom;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getRelation() {
        return relation;
    }

    public void setAllowComment(String allowComment) {
        this.allowComment = allowComment;
    }

    public String getAllowComment() {
        return allowComment;
    }

}
