/**
 * Copyright &copy; 2012-2014 <a href="https://github.com/thinkgem/jeesite">JeeSite</a> All rights reserved.
 */
package com.cmig.future.csportal.module.operate.neighbor.dto;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.base.entity.Page;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Transient;
import javax.xml.bind.annotation.XmlTransient;
import java.util.Date;
import java.util.List;

/**
 * 每日美图Entity
 * @author jinghao.che@zymobi.com
 * @version 2016-05-11
 */
@ExtensionAttribute(disable = true)
public class NhTopicView extends BaseExtDTO {
	
	private static final long serialVersionUID = 1L;
	private String id;		// id
	private String topicContent;		// 话题内容
	private Date publishTime;		// 发布时间
	private String showTime;		// show_time
    private String topicType;		// topic_type
	private String publishId;		// 发布者ID
	private String publisher;		// publisher
	private String topicAddress;		// 地理位置
	private double longitude;		// 地理位置经度
	private double latitude;		// 地理位置纬度
	private String communityId;		// 话题小区ID
	private String communityName;		// community_name
	private String companyId;		// 物业公司ID
	private String companyName;		// 物业公司
	private String typeId;		// 分类ID
	private String typeName;		// 标签名称
	private Long commentNum;		// comment_num
	private Long praiseNum;		// praise_num
	private String userIcon;		// user_icon
	//===============辅助字段================
	private List<LjhNeighborTopicImage> medias;//多媒体集合
	private Page<NhTopicCommentView> comments;//话题评论集合
	private boolean praiseFlag;//true:已点赞,false:未点赞
	private String communityIds;		// 关注小区ID，以,分割
	private String[] ids;//关注小区ID数组
	//===============后台查询辅助字段=====================
	private Date startTime;		// 开始时间
	private Date endTime;		// 结束时间

	@Transient
	private String mobile;

	@Transient
	private String typeIds;		// 分类ID
	@Transient
	private List<String> typeList;		// 分类ID

	@Transient
	protected Page<NhTopicView> page;

	@JsonIgnore
    @XmlTransient
    public Page<NhTopicView> getPage() {
        if (page == null){
            page = new Page<NhTopicView>();
        }
        return page;
    }
    
    public Page<NhTopicView> setPage(Page<NhTopicView> page) {
        this.page = page;
        return page;
    }

	public List<String> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<String> typeList) {
		this.typeList = typeList;
	}

	public String getTypeIds() {
		return typeIds;
	}

	public void setTypeIds(String typeIds) {
		this.typeIds = typeIds;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NhTopicView() {
		super();
	}

	public NhTopicView(String id){
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getTopicContent() {
		return topicContent;
	}

	public void setTopicContent(String topicContent) {
		this.topicContent = topicContent;
	}
	
	public Date getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(Date publishTime) {
		this.publishTime = publishTime;
	}
	
	public String getShowTime() {
		return showTime;
	}

	public void setShowTime(String showTime) {
		this.showTime = showTime;
	}
	
	public String getPublishId() {
		return publishId;
	}

	public void setPublishId(String publishId) {
		this.publishId = publishId;
	}
	
	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}
	
	public String getTopicAddress() {
		return topicAddress;
	}

	public void setTopicAddress(String topicAddress) {
		this.topicAddress = topicAddress;
	}
	
	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
	
	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}
	
	public String getCommunityId() {
		return communityId;
	}

	public void setCommunityId(String communityId) {
		this.communityId = communityId;
	}
	
	public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}
	
	public String getTypeId() {
		return typeId;
	}

	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}
	
	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}
	
	public Long getCommentNum() {
		return commentNum;
	}

	public void setCommentNum(Long commentNum) {
		this.commentNum = commentNum;
	}
	
	public Long getPraiseNum() {
		return praiseNum;
	}

	public void setPraiseNum(Long praiseNum) {
		this.praiseNum = praiseNum;
	}
	
	public String getUserIcon() {
		return userIcon;
	}

	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

	public boolean isPraiseFlag() {
		return praiseFlag;
	}

	public void setPraiseFlag(boolean praiseFlag) {
		this.praiseFlag = praiseFlag;
	}

	public String getCommunityIds() {
		return communityIds;
	}

	public void setCommunityIds(String communityIds) {
		this.communityIds = communityIds;
	}

    public Page<NhTopicCommentView> getComments() {
        return comments;
    }

    public void setComments(Page<NhTopicCommentView> comments) {
        this.comments = comments;
    }

    public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

    public String getTopicType() {
        return topicType;
    }

    public void setTopicType(String topicType) {
        this.topicType = topicType;
    }

    public List<LjhNeighborTopicImage> getMedias() {
        return medias;
    }

    public void setMedias(List<LjhNeighborTopicImage> medias) {
        this.medias = medias;
    }


}