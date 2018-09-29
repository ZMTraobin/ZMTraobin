package com.cmig.future.csportal.module.operate.neighbor.dto;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_neighbor_topic_image")
public class LjhNeighborTopicImage extends BaseExtDTO {
    @Id
    private String id;

    private String delFlag;

    private String topicId;

    private String url;

    @Transient
    private String breviaryUrl; // 缩略图片路径

    public String getBreviaryUrl() {
        return breviaryUrl;
    }

    public void setBreviaryUrl(String breviaryUrl) {
        this.breviaryUrl = breviaryUrl;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }

    public String getTopicId() {
        return topicId;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

}
