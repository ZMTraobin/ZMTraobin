package com.cmig.future.csportal.module.properties.base.house.dto;

/**Auto Generated By Hap Code Generator**/

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.math.BigDecimal;

@ExtensionAttribute(disable=true)
@Table(name = "csp_mgt_house")
public class MgtHouse extends BaseExtDTO {
     @Id
     @GeneratedValue
      private Long houseId;

     @NotEmpty
      private String houseCode;

      @NotEmpty
      private String communityId;

     @NotEmpty
      private String houseName;

      private String houseNickName;

      private String houseFullName;

      private String useType;

      private BigDecimal buildingArea;

      private BigDecimal paymentArea;

      private String decorationStatus;

      private String status;

      private String sourceHouseCode;
      @Transient
      private String communityName;
      @Transient
      private String companyName;

     @Transient
      private String structureParentFullName;

     @Transient
      private Long parentStructureId;

     @Transient
      private String sourceSystem;

    @Transient
    private Long mapId;

    public Long getMapId() {
        return mapId;
    }

    public void setMapId(Long mapId) {
        this.mapId = mapId;
    }

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Long getParentStructureId() {
        return parentStructureId;
    }

    public void setParentStructureId(Long parentStructureId) {
        this.parentStructureId = parentStructureId;
    }

    public String getStructureParentFullName() {
        return structureParentFullName;
    }

    public void setStructureParentFullName(String structureParentFullName) {
        this.structureParentFullName = structureParentFullName;
    }

    public void setHouseId(Long houseId){
         this.houseId = houseId;
     }

     public Long getHouseId(){
         return houseId;
     }


     public void setHouseCode(String houseCode){
         this.houseCode = houseCode;
     }

     public String getHouseCode(){
         return houseCode;
     }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public void setHouseName(String houseName){
         this.houseName = houseName;
     }

     public String getHouseName(){
         return houseName;
     }

     public void setHouseNickName(String houseNickName){
         this.houseNickName = houseNickName;
     }

     public String getHouseNickName(){
         return houseNickName;
     }

     public void setHouseFullName(String houseFullName){
         this.houseFullName = houseFullName;
     }

     public String getHouseFullName(){
         return houseFullName;
     }

     public void setUseType(String useType){
         this.useType = useType;
     }

     public String getUseType(){
         return useType;
     }

     public void setBuildingArea(BigDecimal buildingArea){
         this.buildingArea = buildingArea;
     }

     public BigDecimal getBuildingArea(){
         return buildingArea;
     }

     public void setPaymentArea(BigDecimal paymentArea){
         this.paymentArea = paymentArea;
     }

     public BigDecimal getPaymentArea(){
         return paymentArea;
     }

     public void setDecorationStatus(String decorationStatus){
         this.decorationStatus = decorationStatus;
     }

     public String getDecorationStatus(){
         return decorationStatus;
     }

     public void setStatus(String status){
         this.status = status;
     }

     public String getStatus(){
         return status;
     }

     public void setSourceHouseCode(String sourceHouseCode){
         this.sourceHouseCode = sourceHouseCode;
     }

     public String getSourceHouseCode(){
         return sourceHouseCode;
     }

     }