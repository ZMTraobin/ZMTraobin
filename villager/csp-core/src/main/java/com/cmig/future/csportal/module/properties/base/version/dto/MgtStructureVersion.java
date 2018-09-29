package com.cmig.future.csportal.module.properties.base.version.dto;

/**Auto Generated By Hap Code Generator**/

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@ExtensionAttribute(disable=true)
@Table(name = "csp_mgt_structure_version")
public class MgtStructureVersion extends BaseExtDTO {
     @Id
     @GeneratedValue
      private Long versionId;

     @NotEmpty
      private String communityId;

     @NotEmpty
      private String versionNo;

     @NotEmpty
      private String versionName;

     @NotEmpty
      private String status;

      private String isDefault;

    @Transient
     private String communityName;

     @Transient
      private String companyName;

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public void setVersionId(Long versionId){
         this.versionId = versionId;
     }

     public Long getVersionId(){
         return versionId;
     }

     public void setCommunityId(String communityId){
         this.communityId = communityId;
     }

     public String getCommunityId(){
         return communityId;
     }

     public void setVersionNo(String versionNo){
         this.versionNo = versionNo;
     }

     public String getVersionNo(){
         return versionNo;
     }

     public void setVersionName(String versionName){
         this.versionName = versionName;
     }

     public String getVersionName(){
         return versionName;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

	public String getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(String isDefault) {
		this.isDefault = isDefault;
	}
}