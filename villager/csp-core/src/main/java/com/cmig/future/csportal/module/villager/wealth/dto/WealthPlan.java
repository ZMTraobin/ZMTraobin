package com.cmig.future.csportal.module.villager.wealth.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "xl_wealth_plan")
public class WealthPlan extends BaseDTO {
     @Id
     @GeneratedValue
      private Long id;

      private String typeCode;

      private String muCode;

      private String userId;

      private String userName;

      private String typeName;

      private String muArea;

      private String itemName;

      private String totalIncome;

      private String netIncome;

      private String totalCost;

      private Date commitDate;

      private String flag;

      private String status;

      private String mgtUserId;


     public void setId(Long id){
         this.id = id;
     }

     public Long getId(){
         return id;
     }

     public void setTypeCode(String typeCode){
         this.typeCode = typeCode;
     }

     public String getTypeCode(){
         return typeCode;
     }

     public void setMuCode(String muCode){
         this.muCode = muCode;
     }

     public String getMuCode(){
         return muCode;
     }

     public void setUserId(String userId){
         this.userId = userId;
     }

     public String getUserId(){
         return userId;
     }

     public void setUserName(String userName){
         this.userName = userName;
     }

     public String getUserName(){
         return userName;
     }

     public void setTypeName(String typeName){
         this.typeName = typeName;
     }

     public String getTypeName(){
         return typeName;
     }

     public void setMuArea(String muArea){
         this.muArea = muArea;
     }

     public String getMuArea(){
         return muArea;
     }

     public void setItemName(String itemName){
         this.itemName = itemName;
     }

     public String getItemName(){
         return itemName;
     }

     public void setTotalIncome(String totalIncome){
         this.totalIncome = totalIncome;
     }

     public String getTotalIncome(){
         return totalIncome;
     }

     public void setNetIncome(String netIncome){
         this.netIncome = netIncome;
     }

     public String getNetIncome(){
         return netIncome;
     }

     public void setTotalCost(String totalCost){
         this.totalCost = totalCost;
     }

     public String getTotalCost(){
         return totalCost;
     }

     public void setCommitDate(Date commitDate){
         this.commitDate = commitDate;
     }

     public Date getCommitDate(){
         return commitDate;
     }

     public void setFlag(String flag){
         this.flag = flag;
     }

     public String getFlag(){
         return flag;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     public void setMgtUserId(String mgtUserId){
         this.mgtUserId = mgtUserId;
     }

     public String getMgtUserId(){
         return mgtUserId;
     }

     }