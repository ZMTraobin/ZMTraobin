package com.cmig.future.csportal.module.operate.neighbor.dto;

import java.util.Date;
import java.util.List;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_neighbor_topic_comment")
public class LjhNeighborTopicComment extends BaseExtDTO {
    @Id
    private String id;

    private String delFlag;

    private String topicId;

    private String userId;

    private String content;

    private String replyId;

    @Transient
    private String userName;
    @Transient
    private String userIcon;
    @Transient
    private Date creationDate;

    @Transient
    private List<LjhNeighborTopicReply> replies;     //评论回复

    public List<LjhNeighborTopicReply> getReplies() {
        return replies;
    }

    public void setReplies(List<LjhNeighborTopicReply> replies) {
        this.replies = replies;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplyId() {
        return replyId;
    }

}
