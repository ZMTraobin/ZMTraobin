package com.cmig.future.csportal.module.integral.dto;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_integral_rule")
public class IntegralRule extends BaseDTO {
     @Id
     @GeneratedValue
      private Long ruleId;

     @NotEmpty
      private String ruleCode;

     @NotEmpty
      private String actionType;

     @NotEmpty
      private String eventCode;

     @NotNull
      private Long num;

     @NotEmpty
      private String cappingFactor;

     @NotNull
      private Long cappingTimes;

     @NotEmpty
      private String billingCycle;

     @NotEmpty
      private String status;

      private Date effectiveStartDate;

      private Date effectiveEndDate;

      private String remark;


     public void setRuleId(Long ruleId){
         this.ruleId = ruleId;
     }

     public Long getRuleId(){
         return ruleId;
     }

     public void setRuleCode(String ruleCode){
         this.ruleCode = ruleCode;
     }

     public String getRuleCode(){
         return ruleCode;
     }

     public void setActionType(String actionType){
         this.actionType = actionType;
     }

     public String getActionType(){
         return actionType;
     }

     public void setEventCode(String eventCode){
         this.eventCode = eventCode;
     }

     public String getEventCode(){
         return eventCode;
     }

     public void setNum(Long num){
         this.num = num;
     }

     public Long getNum(){
         return num;
     }

     public void setCappingFactor(String cappingFactor){
         this.cappingFactor = cappingFactor;
     }

     public String getCappingFactor(){
         return cappingFactor;
     }

     public void setCappingTimes(Long cappingTimes){
         this.cappingTimes = cappingTimes;
     }

     public Long getCappingTimes(){
         return cappingTimes;
     }

     public void setBillingCycle(String billingCycle){
         this.billingCycle = billingCycle;
     }

     public String getBillingCycle(){
         return billingCycle;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     public void setEffectiveStartDate(Date effectiveStartDate){
         this.effectiveStartDate = effectiveStartDate;
     }

     public Date getEffectiveStartDate(){
         return effectiveStartDate;
     }

     public void setEffectiveEndDate(Date effectiveEndDate){
         this.effectiveEndDate = effectiveEndDate;
     }

     public Date getEffectiveEndDate(){
         return effectiveEndDate;
     }

     public void setRemark(String remark){
         this.remark = remark;
     }

     public String getRemark(){
         return remark;
     }

     }
