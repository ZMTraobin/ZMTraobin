package com.cmig.future.csportal.module.villager.family.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable=true)
@Table(name = "xl_housing_info")
public class HousingInfo extends BaseExtDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 住房信息
     */
	@Id
    @GeneratedValue
    private Long id;
	
	private String familyCode;
	
	private String houseArea;
	
	private Long houseFloors;
	
	private String homesteadArea;
	
	private String isCertificate;
	
	private String buildPrice;
	
	private Long buildYear;
	
	private String isPhotovoltaic;
	
	private String isInstallPho;

	private String installablePhoArea;
	
	public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

	public String getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}

	public Long getHouseFloors() {
		return houseFloors;
	}

	public void setHouseFloors(Long houseFloors) {
		this.houseFloors = houseFloors;
	}

	public String getHomesteadArea() {
		return homesteadArea;
	}

	public void setHomesteadArea(String homesteadArea) {
		this.homesteadArea = homesteadArea;
	}

	public String getIsCertificate() {
		return isCertificate;
	}

	public void setIsCertificate(String isCertificate) {
		this.isCertificate = isCertificate;
	}

	public String getBuildPrice() {
		return buildPrice;
	}

	public void setBuildPrice(String buildPrice) {
		this.buildPrice = buildPrice;
	}

	public Long getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(Long buildYear) {
		this.buildYear = buildYear;
	}

	public String getIsPhotovoltaic() {
		return isPhotovoltaic;
	}

	public void setIsPhotovoltaic(String isPhotovoltaic) {
		this.isPhotovoltaic = isPhotovoltaic;
	}

	public String getIsInstallPho() {
		return isInstallPho;
	}

	public void setIsInstallPho(String isInstallPho) {
		this.isInstallPho = isInstallPho;
	}

	public String getInstallablePhoArea() {
		return installablePhoArea;
	}

	public void setInstallablePhoArea(String installablePhoArea) {
		this.installablePhoArea = installablePhoArea;
	}
	
	public String getFamilyCode() {
		return familyCode;
	}

	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}
}
