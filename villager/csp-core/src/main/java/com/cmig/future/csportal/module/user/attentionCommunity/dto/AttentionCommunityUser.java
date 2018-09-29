package com.cmig.future.csportal.module.user.attentionCommunity.dto;

/**Auto Generated By Hap Code Generator**/
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;


/**
 * AttentionCommunityUser
 *
 * @author bubu
 *
 * 2017-3-21
 */
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_attention_community_user")
public class AttentionCommunityUser extends BaseExtDTO {
     @Id
      private String id;

      private String delFlag;

      private String communityId;

      private String userId;

      private String isAttention;

    @Transient
      private String companyName;//物业公司名称
    @Transient
      private String communityName; // 小区名称
    @Transient
      private String communityAddress; // 小区地址
    @Transient
      private String sourceSystem;//源系统代码
    @Transient
      private String sourceSystemId;//源系统小区id

    @Transient
    private String serverUrl;//源系统小区所在服务器域名

    @Transient
    private String residentManager;//是否支持用户管理

	@Transient
	private String isRemoteAuthen;//是否物管云小区

    private Date lastUpdateDate;//更新时间

    public String getResidentManager() {
        return residentManager;
    }

    public void setResidentManager(String residentManager) {
        this.residentManager = residentManager;
    }

    @Override
    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    @Override
    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCommunityAddress() {
        return communityAddress;
    }

    public void setCommunityAddress(String communityAddress) {
        this.communityAddress = communityAddress;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceSystemId() {
        return sourceSystemId;
    }

    public void setSourceSystemId(String sourceSystemId) {
        this.sourceSystemId = sourceSystemId;
    }

     public void setId(String id){
         this.id = id;
     }

     public String getId(){
         return id;
     }

     public void setDelFlag(String delFlag){
         this.delFlag = delFlag;
     }

     public String getDelFlag(){
         return delFlag;
     }

     public void setCommunityId(String communityId){
         this.communityId = communityId;
     }

     public String getCommunityId(){
         return communityId;
     }

     public void setUserId(String userId){
         this.userId = userId;
     }

     public String getUserId(){
         return userId;
     }

     public void setIsAttention(String isAttention){
         this.isAttention = isAttention;
     }

     public String getIsAttention(){
         return isAttention;
     }

    public String getServerUrl() {
        return serverUrl;
    }

    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }

	public String getIsRemoteAuthen() {
		return isRemoteAuthen;
	}

	public void setIsRemoteAuthen(String isRemoteAuthen) {
		this.isRemoteAuthen = isRemoteAuthen;
	}
}
