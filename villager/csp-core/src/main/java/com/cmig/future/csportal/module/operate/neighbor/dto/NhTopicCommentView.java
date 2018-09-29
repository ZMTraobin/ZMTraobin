/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cmig.future.csportal.module.operate.neighbor.dto;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.base.entity.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import java.util.List;

/**
 * 每日美图评论Entity
 * @author jinghao.che@zymobi.com
 * @version 2016-05-11
 */
@ExtensionAttribute(disable = true)
public class NhTopicCommentView extends BaseExtDTO{
	
	private static final long serialVersionUID = 1L;
	private String topicId;		// 话题ID
	private String userId;		// 评论者ID
	private String userName;		// 姓名
	private String userIcon;		// user_icon
	private String replyId;		// 被回复者ID
	private String replyer;		// 姓名
	private String content;		// 评论内容
	private String id;

	@Transient
	private List<LjhNeighborTopicReply> replies;     //评论回复
	
	@Transient
	protected Page<NhTopicCommentView> page;


	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public List<LjhNeighborTopicReply> getReplies() {
		return replies;
	}

	public void setReplies(List<LjhNeighborTopicReply> replies) {
		this.replies = replies;
	}

	@JsonIgnore
    @XmlTransient
    public Page<NhTopicCommentView> getPage() {
        if (page == null){
            page = new Page<NhTopicCommentView>();
        }
        return page;
    }
    
    public Page<NhTopicCommentView> setPage(Page<NhTopicCommentView> page) {
        this.page = page;
        return page;
    }

    //	@JsonFormat(pattern = "yyyy-MM-dd HH:mm")
//	public Date getCreateTime() {
//		return creationTime;
//	}
	public NhTopicCommentView() {
		super();
	}

	public NhTopicCommentView(String id){
	}

	public String getTopicId() {
		return topicId;
	}

	public void setTopicId(String topicId) {
		this.topicId = topicId;
	}
	
	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}
	
	public String getReplyId() {
		return replyId;
	}

	public void setReplyId(String replyId) {
		this.replyId = replyId;
	}
	
	public String getReplyer() {
		return replyer;
	}

	public void setReplyer(String replyer) {
		this.replyer = replyer;
	}
	
	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
	
}