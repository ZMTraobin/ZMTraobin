package com.cmig.future.csportal.module.villager.family.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable=true)
@Table(name = "xl_land_info")
public class LandInfo extends BaseExtDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 土地信息
     */
	@Id
    @GeneratedValue
    private Long id;
	
	private String familyCode;
	
	private String landArea;
	
	private String landPrice;
	
	private String mainCrop;
	
	public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

	public String getLandArea() {
		return landArea;
	}

	public void setLandArea(String landArea) {
		this.landArea = landArea;
	}

	public String getLandPrice() {
		return landPrice;
	}

	public void setLandPrice(String landPrice) {
		this.landPrice = landPrice;
	}

	public String getMainCrop() {
		return mainCrop;
	}

	public void setMainCrop(String mainCrop) {
		this.mainCrop = mainCrop;
	}
	
	public String getFamilyCode() {
		return familyCode;
	}

	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}
}
