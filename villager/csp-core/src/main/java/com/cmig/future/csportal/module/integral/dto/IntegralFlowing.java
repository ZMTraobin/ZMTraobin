package com.cmig.future.csportal.module.integral.dto;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;

import java.util.Date;

@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_integral_flowing")
public class IntegralFlowing extends BaseDTO {
     @Id
     @GeneratedValue
      private Long id;

      private String appId;

      private String serviceId;

      private String uid;

      private String phone;

      private Long purchaseAmount;

      private String type;

      private String outTradeNo;

      private String autoTradeNo;

      private String description;

      private String attach;

      private String timestamps;

      private Long credits;

      private String status;

      private String sign;

      private String integralParame;

      private String integralType;

      private String integralUrl;
      @Transient
      private String integralStatus;

      private Long createdBy;

      private Date createTime;


    public String getIntegralStatus() {
        return integralStatus;
    }

    public void setIntegralStatus(String integralStatus) {
        this.integralStatus = integralStatus;
    }

    public void setId(Long id){
         this.id = id;
     }

     public Long getId(){
         return id;
     }

     public void setAppId(String appId){
         this.appId = appId;
     }

     public String getAppId(){
         return appId;
     }

     public void setServiceId(String serviceId){
         this.serviceId = serviceId;
     }

     public String getServiceId(){
         return serviceId;
     }

     public void setUid(String uid){
         this.uid = uid;
     }

     public String getUid(){
         return uid;
     }

     public void setPhone(String phone){
         this.phone = phone;
     }

     public String getPhone(){
         return phone;
     }

     public void setPurchaseAmount(Long purchaseAmount){
         this.purchaseAmount = purchaseAmount;
     }

     public Long getPurchaseAmount(){
         return purchaseAmount;
     }

     public void setType(String type){
         this.type = type;
     }

     public String getType(){
         return type;
     }

     public void setOutTradeNo(String outTradeNo){
         this.outTradeNo = outTradeNo;
     }

     public String getOutTradeNo(){
         return outTradeNo;
     }

     public void setAutoTradeNo(String autoTradeNo){
         this.autoTradeNo = autoTradeNo;
     }

     public String getAutoTradeNo(){
         return autoTradeNo;
     }

     public void setDescription(String description){
         this.description = description;
     }

     public String getDescription(){
         return description;
     }

     public void setAttach(String attach){
         this.attach = attach;
     }

     public String getAttach(){
         return attach;
     }


     public void setTimestamps(String timestamps){
         this.timestamps = timestamps;
     }

     public String getTimestamps(){
         return timestamps;
     }

     public void setCredits(Long credits){
         this.credits = credits;
     }

     public Long getCredits(){
         return credits;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     public void setSign(String sign){
         this.sign = sign;
     }

     public String getSign(){
         return sign;
     }

    public String getIntegralParame() {
        return integralParame;
    }

    public void setIntegralParame(String integralParame) {
        this.integralParame = integralParame;
    }

    public String getIntegralType() {
        return integralType;
    }

    public void setIntegralType(String integralType) {
        this.integralType = integralType;
    }

    public String getIntegralUrl() {
        return integralUrl;
    }

    public void setIntegralUrl(String integralUrl) {
        this.integralUrl = integralUrl;
    }

    public void setCreatedBy(Long createdBy){
         this.createdBy = createdBy;
     }

    public Long getCreatedBy(){
         return createdBy;
     }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
