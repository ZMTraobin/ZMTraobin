package com.cmig.future.csportal.module.operate.neighbor.dto;

/**
 * Auto Generated By Hap Code Generator
 **/

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_neighbor_topic_reply")
public class LjhNeighborTopicReply extends BaseExtDTO {
    @Id
    //@GeneratedValue
    private String id;

    private String delFlag;

    private String replyId;

    private String userId;

    private String content;

    private String commentId;

    @Transient
    private String replyFrom;
    @Transient
    private String replyFromIcon;
    @Transient
    private String replyTo;
    @Transient
    private String replyToIcon;
    @Transient
    private Date creationDate;

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getReplyFrom() {
        return replyFrom;
    }

    public void setReplyFrom(String replyFrom) {
        this.replyFrom = replyFrom;
    }

    public String getReplyFromIcon() {
        return replyFromIcon;
    }

    public void setReplyFromIcon(String replyFromIcon) {
        this.replyFromIcon = replyFromIcon;
    }

    public String getReplyTo() {
        return replyTo;
    }

    public void setReplyTo(String replyTo) {
        this.replyTo = replyTo;
    }

    public String getReplyToIcon() {
        return replyToIcon;
    }

    public void setReplyToIcon(String replyToIcon) {
        this.replyToIcon = replyToIcon;
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

    public void setReplyId(String replyId) {
        this.replyId = replyId;
    }

    public String getReplyId() {
        return replyId;
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

    public void setCommentId(String commentId) {
        this.commentId = commentId;
    }

    public String getCommentId() {
        return commentId;
    }

}
