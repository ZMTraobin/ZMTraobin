package com.cmig.future.csportal.module.villager.family.dto;


import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@ExtensionAttribute(disable=true)
@Table(name = "xl_family_info")
public class FamilyInfo extends BaseExtDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 家庭信息
	 */
	@Id
    @GeneratedValue
    private Long id;
	
	private String familyCode;
	
    @NotEmpty
    private String userName;

    private String userSex;
    
	@NotEmpty
    private Long mobile;

    @NotEmpty
    private String familyRelation;

    private Long age;

    private String politicalStatus;

    private String education;

    private String idcard;
    
    @NotEmpty
    private String villageCode;

    private String villageId;

    private Long population;

    @NotEmpty
    private Long householderMobile;
    
    public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	public String getFamilyRelation() {
		return familyRelation;
	}

	public void setFamilyRelation(String familyRelation) {
		this.familyRelation = familyRelation;
	}

	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	public String getPoliticalStatus() {
		return politicalStatus;
	}

	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}

	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	public String getVillageCode() {
		return villageCode;
	}

	public void setVillageCode(String villageCode) {
		this.villageCode = villageCode;
	}

	public String getVillageId() {
		return villageId;
	}

	public void setVillageId(String villageId) {
		this.villageId = villageId;
	}

	public Long getPopulation() {
		return population;
	}

	public void setPopulation(Long population) {
		this.population = population;
	}

	public Long getHouseholderMobile() {
		return householderMobile;
	}

	public void setHouseholderMobile(Long householderMobile) {
		this.householderMobile = householderMobile;
	}

	public String getFamilyCode() {
		return familyCode;
	}

	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}

}
