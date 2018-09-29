package com.cmig.future.csportal.module.properties.community.dto;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;
import java.util.Date;

/**
 * BaseCommunity
 * 项目
 * @author bubu
 *
 * 2017-3-21
 */
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_base_community")
public class BaseCommunity extends BaseExtDTO {
     @Id
      private String id;

      private String delFlag;

      private String areaId;

      private String cityName;

      private String area;

      private String useArea;

      private String securityUnit;

      private String greenArea;

      private Long floorNum;

      private String constructionUnit;

      private String cityId;

      private String partitionl;

      private String communityName;

      private String companyId;

      private String companyName;

      private String communityCode;

      private BigDecimal latitude;

      private BigDecimal longitude;

      private String phone;

      private String officeId;

      private String officeName;

      private String partitionlId;

      private String mobile;

      private String workingTime;

      private Date checkinTime;

      private String propertyManagementArea;

      private String address;

      private String twoCode;

      private String groupphone;

      private String printtemplatefamily;

      private String printtemplatepublic;

      private String sourceSystem;

      private String sourceSystemId;

      private String serverUrl;

      private String isRemoteAuthen;

      private String unableFlag; //启用标记（0：正常；1：停用;）

      @Transient
      private double distance; // 根据经纬度计算的距离

    @Transient
    private String isCurrentAttention; // 是否当前关注 0:否,1:是

    @Transient
    private String isAttention; // 是否被关注 N:否,Y:是

    private String residentManager; //是否支持用户管理

    public String getResidentManager() {
        return residentManager;
    }

    public void setResidentManager(String residentManager) {
        this.residentManager = residentManager;
    }

    public String getCommunityCode() {
        return communityCode;
    }

    public void setCommunityCode(String communityCode) {
        this.communityCode = communityCode;
    }

    public String getUnableFlag() {
        return unableFlag;
    }

    public void setUnableFlag(String unableFlag) {
        this.unableFlag = unableFlag;
    }

    public String getIsAttention() {
        return isAttention;
    }

    public String getIsCurrentAttention() {
        return isCurrentAttention;
    }

    public void setIsCurrentAttention(String isCurrentAttention) {
        this.isCurrentAttention = isCurrentAttention;
    }

    public void setIsAttention(String isAttention) {
        this.isAttention = isAttention;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setId(String id){
         this.id = id;
     }

     public String getId(){
         return id;
     }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setDelFlag(String delFlag){
         this.delFlag = delFlag;
     }

     public String getDelFlag(){
         return delFlag;
     }

     public void setAreaId(String areaId){
         this.areaId = areaId;
     }

     public String getAreaId(){
         return areaId;
     }

     public void setCityName(String cityName){
         this.cityName = cityName;
     }

     public String getCityName(){
         return cityName;
     }

     public void setArea(String area){
         this.area = area;
     }

     public String getArea(){
         return area;
     }

     public void setUseArea(String useArea){
         this.useArea = useArea;
     }

     public String getUseArea(){
         return useArea;
     }

     public void setSecurityUnit(String securityUnit){
         this.securityUnit = securityUnit;
     }

     public String getSecurityUnit(){
         return securityUnit;
     }

     public void setGreenArea(String greenArea){
         this.greenArea = greenArea;
     }

     public String getGreenArea(){
         return greenArea;
     }

     public void setFloorNum(Long floorNum){
         this.floorNum = floorNum;
     }

     public Long getFloorNum(){
         return floorNum;
     }

     public void setConstructionUnit(String constructionUnit){
         this.constructionUnit = constructionUnit;
     }

     public String getConstructionUnit(){
         return constructionUnit;
     }

     public void setCityId(String cityId){
         this.cityId = cityId;
     }

     public String getCityId(){
         return cityId;
     }

     public void setPartitionl(String partitionl){
         this.partitionl = partitionl;
     }

     public String getPartitionl(){
         return partitionl;
     }

     public void setCommunityName(String communityName){
         this.communityName = communityName;
     }

     public String getCommunityName(){
         return communityName;
     }

     public void setCompanyId(String companyId){
         this.companyId = companyId;
     }

     public String getCompanyId(){
         return companyId;
     }

     public void setLatitude(BigDecimal latitude){
         this.latitude = latitude;
     }

     public BigDecimal getLatitude(){
         return latitude;
     }

     public void setLongitude(BigDecimal longitude){
         this.longitude = longitude;
     }

     public BigDecimal getLongitude(){
         return longitude;
     }

     public void setPhone(String phone){
         this.phone = phone;
     }

     public String getPhone(){
         return phone;
     }

     public void setOfficeId(String officeId){
         this.officeId = officeId;
     }

     public String getOfficeId(){
         return officeId;
     }

    public String getOfficeName() {
        return officeName;
    }

    public void setOfficeName(String officeName) {
        this.officeName = officeName;
    }

    public void setPartitionlId(String partitionlId){
         this.partitionlId = partitionlId;
     }

     public String getPartitionlId(){
         return partitionlId;
     }

     public void setMobile(String mobile){
         this.mobile = mobile;
     }

     public String getMobile(){
         return mobile;
     }

     public void setWorkingTime(String workingTime){
         this.workingTime = workingTime;
     }

     public String getWorkingTime(){
         return workingTime;
     }

     public void setCheckinTime(Date checkinTime){
         this.checkinTime = checkinTime;
     }

     public Date getCheckinTime(){
         return checkinTime;
     }

     public void setPropertyManagementArea(String propertyManagementArea){
         this.propertyManagementArea = propertyManagementArea;
     }

     public String getPropertyManagementArea(){
         return propertyManagementArea;
     }

     public void setAddress(String address){
         this.address = address;
     }

     public String getAddress(){
         return address;
     }

     public void setTwoCode(String twoCode){
         this.twoCode = twoCode;
     }

     public String getTwoCode(){
         return twoCode;
     }

     public void setGroupphone(String groupphone){
         this.groupphone = groupphone;
     }

     public String getGroupphone(){
         return groupphone;
     }

     public void setPrinttemplatefamily(String printtemplatefamily){
         this.printtemplatefamily = printtemplatefamily;
     }

     public String getPrinttemplatefamily(){
         return printtemplatefamily;
     }

     public void setPrinttemplatepublic(String printtemplatepublic){
         this.printtemplatepublic = printtemplatepublic;
     }

     public String getPrinttemplatepublic(){
         return printtemplatepublic;
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

     public void setServerUrl(String serverUrl){
         this.serverUrl = serverUrl;
     }

     public String getServerUrl(){
         return serverUrl;
     }

     public void setIsRemoteAuthen(String isRemoteAuthen){
         this.isRemoteAuthen = isRemoteAuthen;
     }

     public String getIsRemoteAuthen(){
         return isRemoteAuthen;
     }

    @Transient
    private String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}
