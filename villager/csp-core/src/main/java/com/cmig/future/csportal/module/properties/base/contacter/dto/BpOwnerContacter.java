package com.cmig.future.csportal.module.properties.base.contacter.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.properties.base.customer.dto.BpHouseMap;
import com.cmig.future.csportal.module.properties.base.house.dto.MgtHouse;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

@ExtensionAttribute(disable=true)
@Table(name = "csp_bp_owner_contacter")
public class BpOwnerContacter extends BaseExtDTO {
     @Id
     @GeneratedValue
      private Long ownerContacterId;

     @NotEmpty
      private String type;

     @NotNull
      private Long bpOwnerId;

     @NotEmpty
      private String contacterName;

      private String contacterNickName;

      private String idType;

      private String idNo;

     @NotEmpty
      private String mobile;

      private String photo;

      @Transient
      private String typeDesc;
      @Transient
      private Long buildingId;
      @Transient
      private String houseName;
      @Transient
      private List<BpHouseMap> ownerHouse;
    @Transient
    private String ownerMobile;
    @Transient
    private String communityId;

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getOwnerMobile() {
        return ownerMobile;
    }

    public void setOwnerMobile(String ownerMobile) {
        this.ownerMobile = ownerMobile;
    }

    public List<BpHouseMap> getOwnerHouse() {
        return ownerHouse;
    }

    public void setOwnerHouse(List<BpHouseMap> ownerHouse) {
        this.ownerHouse = ownerHouse;
    }

    public String getTypeDesc() {
        return typeDesc;
    }

    public void setTypeDesc(String typeDesc) {
        this.typeDesc = typeDesc;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }

    public String getHouseName() {
        return houseName;
    }

    public void setHouseName(String houseName) {
        this.houseName = houseName;
    }

    public void setOwnerContacterId(Long ownerContacterId){
         this.ownerContacterId = ownerContacterId;
     }

     public Long getOwnerContacterId(){
         return ownerContacterId;
     }

     public void setType(String type){
         this.type = type;
     }

     public String getType(){
         return type;
     }

     public void setBpOwnerId(Long bpOwnerId){
         this.bpOwnerId = bpOwnerId;
     }

     public Long getBpOwnerId(){
         return bpOwnerId;
     }

     public void setContacterName(String contacterName){
         this.contacterName = contacterName;
     }

     public String getContacterName(){
         return contacterName;
     }

     public void setContacterNickName(String contacterNickName){
         this.contacterNickName = contacterNickName;
     }

     public String getContacterNickName(){
         return contacterNickName;
     }

     public void setIdType(String idType){
         this.idType = idType;
     }

     public String getIdType(){
         return idType;
     }

     public void setIdNo(String idNo){
         this.idNo = idNo;
     }

     public String getIdNo(){
         return idNo;
     }

     public void setMobile(String mobile){
         this.mobile = mobile;
     }

     public String getMobile(){
         return mobile;
     }

     public void setPhoto(String photo){
         this.photo = photo;
     }

     public String getPhoto(){
         return photo;
     }

     }
