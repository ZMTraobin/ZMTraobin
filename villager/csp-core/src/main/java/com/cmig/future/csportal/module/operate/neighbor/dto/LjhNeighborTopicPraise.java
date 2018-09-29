package com.cmig.future.csportal.module.operate.neighbor.dto;

import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.validator.constraints.Length;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_neighbor_topic_praise")
public class LjhNeighborTopicPraise extends BaseExtDTO {
    
    private static final long serialVersionUID = 1L;
    @Id
    private String id;     // 话题ID
    private String delFlag;     // 话题ID
    private String topicId;     // 话题ID
    private String userId;      // 点赞ID
    
    public String getDelFlag() {
        return delFlag;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Length(min=0, max=32, message="话题ID长度必须介于 0 和 32 之间")
    public String getTopicId() {
        return topicId;
    }

    public void setTopicId(String topicId) {
        this.topicId = topicId;
    }
    
    @Length(min=0, max=32, message="点赞ID长度必须介于 0 和 32 之间")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
    

}
