package com.cmig.future.csportal.module.properties.mgtuser.dto;

/**Auto Generated By Hap Code Generator**/

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_mgt_user")
public class MgtUser extends BaseExtDTO {
      @Id
      private String id;

      private String station;

      private String companyId;

      private String companyName;

      private String officeId;
      
      private String communityName;

     @NotEmpty
      private String officeName;

     @NotEmpty
      private String loginName;

     @NotEmpty
      private String password;

      private String no;

     @NotEmpty
      private String name;

      private String email;

      private String phone;

      private String mobile;

      private String userType;

      private String photo;

      private String loginIp;

      private Date loginDate;

      private String loginFlag;

      private Long createdBy;

      private Date creationDate;

      private Long lastUpdatedBy;

      private Date lastUpdateDate;

      private String remarks;

      private String delFlag;

      private String sex;

      private Date birthTime;

      private String post;

	  private String identityType;

      private String idcard;

      private String age;

      private String communityId;

      private String employeeId;

      private String businessNews;

      private String sourceSystem;

      private String sourceSystemId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStation() {
		return station;
   	}

	public void setStation(String station) {
		this.station = station;
	}

	public void setCompanyId(String companyId){
         this.companyId = companyId;
     }

     public String getCompanyId(){
         return companyId;
     }

     public void setCompanyName(String companyName){
         this.companyName = companyName;
     }

     public String getCompanyName(){
         return companyName;
     }

     public void setOfficeId(String officeId){
         this.officeId = officeId;
     }

     public String getOfficeId(){
         return officeId;
     }

     public void setOfficeName(String officeName){
         this.officeName = officeName;
     }

     public String getOfficeName(){
         return officeName;
     }
     

     public String getCommunityName() {
		return communityName;
	}

	public void setCommunityName(String communityName) {
		this.communityName = communityName;
	}

     public void setLoginName(String loginName){
         this.loginName = loginName;
     }

     public String getLoginName(){
         return loginName;
     }

     public void setPassword(String password){
         this.password = password;
     }

     public String getPassword(){
         return password;
     }

     public void setNo(String no){
         this.no = no;
     }

     public String getNo(){
         return no;
     }

     public void setName(String name){
         this.name = name;
     }

     public String getName(){
         return name;
     }

     public void setEmail(String email){
         this.email = email;
     }

     public String getEmail(){
         return email;
     }

     public void setPhone(String phone){
         this.phone = phone;
     }

     public String getPhone(){
         return phone;
     }

     public void setMobile(String mobile){
         this.mobile = mobile;
     }

     public String getMobile(){
         return mobile;
     }

     public void setUserType(String userType){
         this.userType = userType;
     }

     public String getUserType(){
         return userType;
     }

     public void setPhoto(String photo){
         this.photo = photo;
     }

     public String getPhoto(){
         return photo;
     }

     public void setLoginIp(String loginIp){
         this.loginIp = loginIp;
     }

     public String getLoginIp(){
         return loginIp;
     }

     public void setLoginDate(Date loginDate){
         this.loginDate = loginDate;
     }

     public Date getLoginDate(){
         return loginDate;
     }

     public void setLoginFlag(String loginFlag){
         this.loginFlag = loginFlag;
     }

     public String getLoginFlag(){
         return loginFlag;
     }

     public void setRemarks(String remarks){
         this.remarks = remarks;
     }

     public String getRemarks(){
         return remarks;
     }

     public void setDelFlag(String delFlag){
         this.delFlag = delFlag;
     }

     public String getDelFlag(){
         return delFlag;
     }

     public void setSex(String sex){
         this.sex = sex;
     }

     public String getSex(){
         return sex;
     }

     public void setBirthTime(Date birthTime){
         this.birthTime = birthTime;
     }

     public Date getBirthTime(){
         return birthTime;
     }

     public void setPost(String post){
         this.post = post;
     }

     public String getPost(){
         return post;
     }

	public String getIdentityType() {
		return identityType;
	}

	public void setIdentityType(String identityType) {
		this.identityType = identityType;
	}

	public void setIdcard(String idcard){
         this.idcard = idcard;
     }

     public String getIdcard(){
         return idcard;
     }

     public void setAge(String age){
         this.age = age;
     }

     public String getAge(){
         return age;
     }


     public String getCommunityId() {
		return communityId;
	 }

	public void setCommunityId(String communityId) {
	 	this.communityId = communityId;
	 }

	public void setEmployeeId(String employeeId){
         this.employeeId = employeeId;
     }

     public String getEmployeeId(){
         return employeeId;
     }

     public void setBusinessNews(String businessNews){
         this.businessNews = businessNews;
     }

     public String getBusinessNews(){
         return businessNews;
     }

     public void setSourceSystem(String sourceSystem){
         this.sourceSystem = sourceSystem;
     }

     public String getSourceSystem(){
         return sourceSystem;
     }

     public void setSourceSystemId(String sourceSystemId){
         this.sourceSystemId = sourceSystemId;
     }

     public String getSourceSystemId(){
         return sourceSystemId;
     }


    @Transient
    private String oldPwd;
    @Transient
    private String newPwd;
    @Transient
    private String token;
    @Transient
    private String sourceToken;

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSourceToken() {
        return sourceToken;
    }

    public void setSourceToken(String sourceToken) {
        this.sourceToken = sourceToken;
    }

    public Long getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(Long createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Long getLastUpdatedBy() {
        return lastUpdatedBy;
    }

    public void setLastUpdatedBy(Long lastUpdatedBy) {
        this.lastUpdatedBy = lastUpdatedBy;
    }

    public Date getLastUpdateDate() {
        return lastUpdateDate;
    }

    public void setLastUpdateDate(Date lastUpdateDate) {
        this.lastUpdateDate = lastUpdateDate;
    }

}
