package com.cmig.future.csportal.module.pay.account.dto;
/**Auto Generated By Hap Code Generator**/
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@ExtensionAttribute(disable=true)
@Table(name = "csp_account_receivable")
public class AccountReceivable extends BaseExtDTO {
     @Id
     @GeneratedValue
      private Long id;

      private String companyId;

    @Transient
      private String companyName;

      private String communityId;

    @Transient
      private String communityName;

      private String merchantNumber;

    @Transient
      private String merchantName;

      private String expenditure;

      private String enableFlag;

    @Transient
    private String cityName;


     public void setId(Long id){
         this.id = id;
     }

     public Long getId(){
         return id;
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

     public void setCommunityId(String communityId){
         this.communityId = communityId;
     }

     public String getCommunityId(){
         return communityId;
     }

     public void setCommunityName(String communityName){
         this.communityName = communityName;
     }

     public String getCommunityName(){
         return communityName;
     }

     public void setMerchantNumber(String merchantNumber){
         this.merchantNumber = merchantNumber;
     }

     public String getMerchantNumber(){
         return merchantNumber;
     }

     public void setMerchantName(String merchantName){
         this.merchantName = merchantName;
     }

     public String getMerchantName(){
         return merchantName;
     }

     public void setExpenditure(String expenditure){
         this.expenditure = expenditure;
     }

     public String getExpenditure(){
         return expenditure;
     }

     public void setEnableFlag(String enableFlag){
         this.enableFlag = enableFlag;
     }

     public String getEnableFlag(){
         return enableFlag;
     }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }
}
