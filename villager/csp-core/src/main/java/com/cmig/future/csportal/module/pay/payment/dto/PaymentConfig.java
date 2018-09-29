package com.cmig.future.csportal.module.pay.payment.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.hand.hap.system.dto.BaseDTO;
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_payment_config")
public class PaymentConfig extends BaseExtDTO {
     @Id
     @GeneratedValue
      private Long id;

      private String pingAppName;

      private String pingAppId;

      private String testSecretKey;

      private String liveSecretKey;

      private String pingPublicKey;

      private String merchantPrivateKey;

      private String isenable;

      private String isDefault;

      private String isDel;

      @Transient
      private String payMentNO;
      @Transient
      private String payMentYES;


      private Long createdBy;


     public void setId(Long id){
         this.id = id;
     }

     public Long getId(){
         return id;
     }

     public void setPingAppName(String pingAppName){
         this.pingAppName = pingAppName;
     }

     public String getPingAppName(){
         return pingAppName;
     }

     public void setPingAppId(String pingAppId){
         this.pingAppId = pingAppId;
     }

     public String getPingAppId(){
         return pingAppId;
     }

     public void setTestSecretKey(String testSecretKey){
         this.testSecretKey = testSecretKey;
     }

     public String getTestSecretKey(){
         return testSecretKey;
     }

     public void setLiveSecretKey(String liveSecretKey){
         this.liveSecretKey = liveSecretKey;
     }

     public String getLiveSecretKey(){
         return liveSecretKey;
     }

     public void setPingPublicKey(String pingPublicKey){
         this.pingPublicKey = pingPublicKey;
     }

     public String getPingPublicKey(){
         return pingPublicKey;
     }

     public void setMerchantPrivateKey(String merchantPrivateKey){
         this.merchantPrivateKey = merchantPrivateKey;
     }

     public String getMerchantPrivateKey(){
         return merchantPrivateKey;
     }

     public void setIsenable(String isenable){
         this.isenable = isenable;
     }

     public String getIsenable(){
         return isenable;
     }

     public void setIsDefault(String isDefault){
         this.isDefault = isDefault;
     }

     public String getIsDefault(){
         return isDefault;
     }

     public void setCreatedBy(Long createdBy){
         this.createdBy = createdBy;
     }

     public Long getCreatedBy(){
         return createdBy;
     }

    public String getIsDel() {
        return isDel;
    }

    public void setIsDel(String isDel) {
        this.isDel = isDel;
    }

    public String getPayMentNO() {
        return payMentNO;
    }

    public String getPayMentYES() {
        return payMentYES;
    }

    public void setPayMentNO(String payMentNO) {
        this.payMentNO = payMentNO;
    }

    public void setPayMentYES(String payMentYES) {
        this.payMentYES = payMentYES;
    }


}
