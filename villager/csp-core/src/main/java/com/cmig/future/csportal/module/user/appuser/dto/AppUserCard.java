package com.cmig.future.csportal.module.user.appuser.dto;

/**Auto Generated By Hap Code Generator**/

import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@ExtensionAttribute(disable=true)
@Table(name = "csp_app_user_card")
public class AppUserCard extends BaseDTO {
     @Id
     @GeneratedValue
      private Long cardId;

     @NotEmpty
      private String appUserId;

     @NotEmpty
      private String idNo;

     @NotEmpty
      private String idType;

     @NotEmpty
      private String cardNo;

     @NotEmpty
      private String cardType;

     @NotEmpty
      private String name;

     @NotEmpty
      private String bankMobile;

      private String bankCode;

     @NotEmpty
      private String status;

	//序号
	private Integer orderSq;
	//是否默认值
	private String defaultFlag;

    public void setCardId(Long cardId){
         this.cardId = cardId;
     }

     public Long getCardId(){
         return cardId;
     }

     public void setAppUserId(String appUserId){
         this.appUserId = appUserId;
     }

     public String getAppUserId(){
         return appUserId;
     }

     public void setIdNo(String idNo){
         this.idNo = idNo;
     }

     public String getIdNo(){
         return idNo;
     }

     public void setIdType(String idType){
         this.idType = idType;
     }

     public String getIdType(){
         return idType;
     }

     public void setCardNo(String cardNo){
         this.cardNo = cardNo;
     }

     public String getCardNo(){
         return cardNo;
     }

     public void setCardType(String cardType){
         this.cardType = cardType;
     }

     public String getCardType(){
         return cardType;
     }

     public void setName(String name){
         this.name = name;
     }

     public String getName(){
         return name;
     }

     public void setBankMobile(String bankMobile){
         this.bankMobile = bankMobile;
     }

     public String getBankMobile(){
         return bankMobile;
     }

     public void setBankCode(String bankCode){
         this.bankCode = bankCode;
     }

     public String getBankCode(){
         return bankCode;
     }

     public void setStatus(String status){
         this.status = status;
     }

    public String getStatus() {
        return status;
    }

	public Integer getOrderSq() {
		return orderSq;
	}

	public void setOrderSq(Integer orderSq) {
		this.orderSq = orderSq;
	}

	public String getDefaultFlag() {
		return defaultFlag;
	}

	public void setDefaultFlag(String defaultFlag) {
		this.defaultFlag = defaultFlag;
	}
}
