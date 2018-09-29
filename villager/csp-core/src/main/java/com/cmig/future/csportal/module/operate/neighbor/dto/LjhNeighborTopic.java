package com.cmig.future.csportal.module.operate.neighbor.dto;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_neighbor_topic")
public class LjhNeighborTopic extends BaseExtDTO {
    
    private static final long serialVersionUID = 1L;
    @Id
    //@GeneratedValue
    private String id;
    private String delFlag;
    private String topicContent;        // 话题内容
    private Date publishTime;       // 发布时间
    private String publishId;       // 发布者ID
    private String communityId;     // 话题小区ID
    private String typeId;      // 标签ID
    private String topicType;   //话题类别
    private String topicAddress;        // 地理位置
    private String longitude;       // 地理位置经度
    private String latitude;        // 地理位置纬度
    
    
    //=====================辅助字段======================
    @Transient
    private String urls;        // 话题图片路径（路径以，隔开）
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public LjhNeighborTopic() {
        super();
    }

    public LjhNeighborTopic(String id){
    }

    @Length(min=0, max=5000, message="话题内容长度必须介于 0 和 5000 之间")
    public String getTopicContent() {
        return topicContent;
    }

    public void setTopicContent(String topicContent) {
        this.topicContent = topicContent;
    }
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    public Date getPublishTime() {
        return publishTime;
    }

    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
    }
    
    @Length(min=0, max=32, message="发布者ID长度必须介于 0 和 32 之间")
    public String getPublishId() {
        return publishId;
    }

    public void setPublishId(String publishId) {
        this.publishId = publishId;
    }
    
    @Length(min=0, max=32, message="话题小区ID长度必须介于 0 和 32 之间")
    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }
    
    @Length(min=0, max=32, message="分类ID长度必须介于 0 和 32 之间")
    public String getTypeId() {
        return typeId;
    }

    public void setTypeId(String typeId) {
        this.typeId = typeId;
    }
    
    @Length(min=0, max=200, message="地理位置长度必须介于 0 和 200 之间")
    public String getTopicAddress() {
        return topicAddress;
    }

    public void setTopicAddress(String topicAddress) {
        this.topicAddress = topicAddress;
    }
    
    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
    
    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getUrls() {
        return urls;
    }

    public void setUrls(String urls) {
        this.urls = urls;
    }

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }
}
