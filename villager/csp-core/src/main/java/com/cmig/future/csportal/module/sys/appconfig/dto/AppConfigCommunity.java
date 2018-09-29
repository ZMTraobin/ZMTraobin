package com.cmig.future.csportal.module.sys.appconfig.dto;

/**Auto Generated By Hap Code Generator**/

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_app_config_community")
public class AppConfigCommunity extends BaseExtDTO {

	  @Id
      private String id;

      private String delFlag;

      private String functionCodes;

      private String communityId;

      private String configFlag;

      private Long createdBy;

      //小区名称
      @Transient
      private String communityName;
      //公司名称
      @Transient
      private String comPanyName;
      //开通的业务名称
      @Transient
      private String functionNames;
      @Transient
      private String companyId;

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
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

     public void setFunctionCodes(String functionCodes){
         this.functionCodes = functionCodes;
     }

     public String getFunctionCodes(){
         return functionCodes;
     }

     public void setCommunityId(String communityId){
         this.communityId = communityId;
     }

     public String getCommunityId(){
         return communityId;
     }

     public void setConfigFlag(String configFlag){
         this.configFlag = configFlag;
     }

     public String getConfigFlag(){
         return configFlag;
     }

     public void setCreatedBy(Long createdBy){
         this.createdBy = createdBy;
     }

     public Long getCreatedBy(){
         return createdBy;
     }

    public String getCommunityName() {
        return communityName;
    }

    public String getComPanyName() {
        return comPanyName;
    }

    public String getFunctionNames() {
        return functionNames;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setComPanyName(String comPanyName) {
        this.comPanyName = comPanyName;
    }

    public void setFunctionNames(String functionNames) {
        this.functionNames = functionNames;
    }
}
