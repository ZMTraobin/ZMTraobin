package com.cmig.future.csportal.module.operate.cms.dto;


import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.validator.constraints.NotEmpty;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_cms_category")
public class Category extends BaseExtDTO {
    
    private static final long serialVersionUID = 6281238783091986159L;

    @Id
    //@GeneratedValue(strategy=GenerationType.AUTO)
    private String id;

    private String parentId;

    @NotEmpty
    private String name;
    
    @NotEmpty
    private String contentType;

    private String imageUrl;
    
    private String description;

    private String delFlag;
    
    @Transient
    private String parentName;
    
    //是否展开，前台树展开使用
    @Transient
    private boolean expanded = true;
    
    
    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public boolean isExpanded() {
        return expanded;
    }

    public void setExpanded(boolean expanded) {
        this.expanded = expanded;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

}
