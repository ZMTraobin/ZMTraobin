package com.cmig.future.csportal.module.user.appuser.dto;

/**Auto Generated By Hap Code Generator**/
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_app_user")
public class AppUser extends BaseExtDTO {
     @Id
      private String id;

      //private Date createTime;

      private String userName;

      private String userType;

      private String relationFlag;

      private Date retTime;

      private Date lastTime;

      private String lastIp;

      private String passWord;

      private String mobile;

      private String userIcon;

      private String nickName;

      private String twoCode;

      private String homeAttribute;

      private String phone;

      private String email;

      private String selfIntroduction;

      private String associatedMobile1;

      private String associatedMobile2;

      private String associatedMobile3;

      private String signCollectionAgreementFlag;

      private String registrationInvitationCode;

      private Long integralBalance;

      private String sex;

      private String sourceSystem;

      private String sourceSystemId;

      private Date sourceSystemLastUpdateTime;

      private String country;

      private String province;

      private String city;

      private String location;

      private String mobileProvince;

      private String mobileCity;

      private String mobileSupplier;

      private String updateCorePasswordFlag;

    @Transient
    private String communityName;
    @Transient
    private String beginTime;
    @Transient
    private String endTime;

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setId(String id){
         this.id = id;
     }

     public String getId(){
         return id;
     }

  /*   public void setCreateTime(Date createTime){
         this.createTime = createTime;
     }

     public Date getCreateTime(){
         return createTime;
     }*/

     public void setUserName(String userName){
         this.userName = userName;
     }

     public String getUserName(){
         return userName;
     }

     public void setUserType(String userType){
         this.userType = userType;
     }

     public String getUserType(){
         return userType;
     }

     public void setRelationFlag(String relationFlag){
         this.relationFlag = relationFlag;
     }

     public String getRelationFlag(){
         return relationFlag;
     }

     public void setRetTime(Date retTime){
         this.retTime = retTime;
     }

     public Date getRetTime(){
         return retTime;
     }

     public void setLastTime(Date lastTime){
         this.lastTime = lastTime;
     }

     public Date getLastTime(){
         return lastTime;
     }

     public void setLastIp(String lastIp){
         this.lastIp = lastIp;
     }

     public String getLastIp(){
         return lastIp;
     }

     public void setPassWord(String passWord){
         this.passWord = passWord;
     }

     public String getPassWord(){
         return passWord;
     }

     public void setMobile(String mobile){
         this.mobile = mobile;
     }

     public String getMobile(){
         return mobile;
     }

     public void setUserIcon(String userIcon){
         this.userIcon = userIcon;
     }

     public String getUserIcon(){
         return userIcon;
     }

     public void setNickName(String nickName){
         this.nickName = nickName;
     }

     public String getNickName(){
         return nickName;
     }

     public void setTwoCode(String twoCode){
         this.twoCode = twoCode;
     }

     public String getTwoCode(){
         return twoCode;
     }

     public void setHomeAttribute(String homeAttribute){
         this.homeAttribute = homeAttribute;
     }

     public String getHomeAttribute(){
         return homeAttribute;
     }

     public void setPhone(String phone){
         this.phone = phone;
     }

     public String getPhone(){
         return phone;
     }

     public void setEmail(String email){
         this.email = email;
     }

     public String getEmail(){
         return email;
     }

     public void setAssociatedMobile1(String associatedMobile1){
         this.associatedMobile1 = associatedMobile1;
     }

     public String getAssociatedMobile1(){
         return associatedMobile1;
     }

     public void setAssociatedMobile2(String associatedMobile2){
         this.associatedMobile2 = associatedMobile2;
     }

     public String getAssociatedMobile2(){
         return associatedMobile2;
     }

     public void setAssociatedMobile3(String associatedMobile3){
         this.associatedMobile3 = associatedMobile3;
     }

     public String getAssociatedMobile3(){
         return associatedMobile3;
     }

     public void setSignCollectionAgreementFlag(String signCollectionAgreementFlag){
         this.signCollectionAgreementFlag = signCollectionAgreementFlag;
     }

     public String getSignCollectionAgreementFlag(){
         return signCollectionAgreementFlag;
     }

     public void setRegistrationInvitationCode(String registrationInvitationCode){
         this.registrationInvitationCode = registrationInvitationCode;
     }

     public String getRegistrationInvitationCode(){
         return registrationInvitationCode;
     }

     public void setIntegralBalance(Long integralBalance){
         this.integralBalance = integralBalance;
     }

     public Long getIntegralBalance(){
         return integralBalance;
     }

     public void setSex(String sex){
         this.sex = sex;
     }

     public String getSex(){
         return sex;
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

     public void setSourceSystemLastUpdateTime(Date sourceSystemLastUpdateTime){
         this.sourceSystemLastUpdateTime = sourceSystemLastUpdateTime;
     }

     public Date getSourceSystemLastUpdateTime(){
         return sourceSystemLastUpdateTime;
     }

     public void setCountry(String country){
         this.country = country;
     }

     public String getCountry(){
         return country;
     }

     public void setProvince(String province){
         this.province = province;
     }

     public String getProvince(){
         return province;
     }

     public void setCity(String city){
         this.city = city;
     }

     public String getCity(){
         return city;
     }

     public void setLocation(String location){
         this.location = location;
     }

     public String getLocation(){
         return location;
     }

     public void setMobileProvince(String mobileProvince){
         this.mobileProvince = mobileProvince;
     }

     public String getMobileProvince(){
         return mobileProvince;
     }

     public void setMobileCity(String mobileCity){
         this.mobileCity = mobileCity;
     }

     public String getMobileCity(){
         return mobileCity;
     }

     public void setMobileSupplier(String mobileSupplier){
         this.mobileSupplier = mobileSupplier;
     }

     public String getMobileSupplier(){
         return mobileSupplier;
     }

     public void setUpdateCorePasswordFlag(String updateCorePasswordFlag){
         this.updateCorePasswordFlag = updateCorePasswordFlag;
     }

     public String getUpdateCorePasswordFlag(){
         return updateCorePasswordFlag;
     }

    public String getSelfIntroduction() {
        return selfIntroduction;
    }

    public void setSelfIntroduction(String selfIntroduction) {
        this.selfIntroduction = selfIntroduction;
    }
}