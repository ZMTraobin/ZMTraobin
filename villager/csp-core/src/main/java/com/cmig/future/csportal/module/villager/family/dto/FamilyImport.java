package com.cmig.future.csportal.module.villager.family.dto;


import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import org.hibernate.validator.constraints.NotEmpty;


public class FamilyImport extends BaseExtDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * 家庭信息
	 */
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
    
    /**
     * 土地信息
     */
	private String landArea;
	
	private String landPrice;
	
	private String mainCrop;
	
	/**
     * 住房信息
     */
	private String houseArea;
	
	private Long houseFloors;
	
	private String homesteadArea;
	
	private String isCertificate;
	
	private String buildPrice;
	
	private Long buildYear;
	
	private String isPhotovoltaic;
	
	private String isInstallPho;

	private String installablePhoArea;
	
	/**
     * 资金信息
     */
	private String depositBank;
	
	private String depositAmount;
	
	private String isLoan;
	
	private String loanAmount;
	
	private String loanBalance;
	
	private String loanRate;
	
	private String acceptableLoanRate;
	
	
	public String getFamilyCode() {
		return familyCode;
	}

	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}

	@ExcelField(title = "姓名(必填)",  align = 2, sort = 1, required = true)
	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	@ExcelField(title = "性别", dictType="villager.family.sex",  align = 2, sort = 2, required = false)
	public String getUserSex() {
		return userSex;
	}

	public void setUserSex(String userSex) {
		this.userSex = userSex;
	}

	@ExcelField(title = "手机号(必填)",  align = 2, sort = 3, required = true)
	public Long getMobile() {
		return mobile;
	}

	public void setMobile(Long mobile) {
		this.mobile = mobile;
	}

	@ExcelField(title = "成员关系(必填)", dictType="villager.family.relation", align = 2, sort = 4, required = true)
	public String getFamilyRelation() {
		return familyRelation;
	}

	public void setFamilyRelation(String familyRelation) {
		this.familyRelation = familyRelation;
	}

	@ExcelField(title = "年龄",  align = 2, sort = 5, required = false)
	public Long getAge() {
		return age;
	}

	public void setAge(Long age) {
		this.age = age;
	}

	@ExcelField(title = "政治面貌",  align = 2, sort = 6, required = false)
	public String getPoliticalStatus() {
		return politicalStatus;
	}

	public void setPoliticalStatus(String politicalStatus) {
		this.politicalStatus = politicalStatus;
	}

	@ExcelField(title = "学历",  align = 2, sort = 7, required = false)
	public String getEducation() {
		return education;
	}

	public void setEducation(String education) {
		this.education = education;
	}

	@ExcelField(title = "身份证",  align = 2, sort = 8, required = false)
	public String getIdcard() {
		return idcard;
	}

	public void setIdcard(String idcard) {
		this.idcard = idcard;
	}

	@ExcelField(title = "行政村编码(必填)",  align = 2, sort = 9, required = true)
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

	@ExcelField(title = "人口数量",  align = 2, sort = 10, required = false)
	public Long getPopulation() {
		return population;
	}

	public void setPopulation(Long population) {
		this.population = population;
	}

	@ExcelField(title = "户主手机号(必填)",  align = 2, sort = 11, required = true)
	public Long getHouseholderMobile() {
		return householderMobile;
	}

	public void setHouseholderMobile(Long householderMobile) {
		this.householderMobile = householderMobile;
	}

	@ExcelField(title = "自有土地面积",  align = 2, sort = 12, required = false)
	public String getLandArea() {
		return landArea;
	}

	public void setLandArea(String landArea) {
		this.landArea = landArea;
	}

	@ExcelField(title = "土地流转价格",  align = 2, sort = 13, required = false)
	public String getLandPrice() {
		return landPrice;
	}

	public void setLandPrice(String landPrice) {
		this.landPrice = landPrice;
	}

	@ExcelField(title = "主要作物",  align = 2, sort = 14, required = false)
	public String getMainCrop() {
		return mainCrop;
	}

	public void setMainCrop(String mainCrop) {
		this.mainCrop = mainCrop;
	}

	@ExcelField(title = "住房面积",  align = 2, sort = 15, required = false)
	public String getHouseArea() {
		return houseArea;
	}

	public void setHouseArea(String houseArea) {
		this.houseArea = houseArea;
	}

	@ExcelField(title = "房屋层数",  align = 2, sort = 16, required = false)
	public Long getHouseFloors() {
		return houseFloors;
	}

	public void setHouseFloors(Long houseFloors) {
		this.houseFloors = houseFloors;
	}

	@ExcelField(title = "宅基地面积",  align = 2, sort = 17, required = false)
	public String getHomesteadArea() {
		return homesteadArea;
	}

	public void setHomesteadArea(String homesteadArea) {
		this.homesteadArea = homesteadArea;
	}

	@ExcelField(title = "是否有产权证", dictType="villager.family.yesno",  align = 2, sort = 18, required = false)
	public String getIsCertificate() {
		return isCertificate;
	}

	public void setIsCertificate(String isCertificate) {
		this.isCertificate = isCertificate;
	}

	@ExcelField(title = "建造成本",  align = 2, sort = 19, required = false)
	public String getBuildPrice() {
		return buildPrice;
	}

	public void setBuildPrice(String buildPrice) {
		this.buildPrice = buildPrice;
	}

	@ExcelField(title = "建造年份",  align = 2, sort = 20, required = false)
	public Long getBuildYear() {
		return buildYear;
	}

	public void setBuildYear(Long buildYear) {
		this.buildYear = buildYear;
	}

	@ExcelField(title = "是否有光伏", dictType="villager.family.yesno",  align = 2, sort = 21, required = false)
	public String getIsPhotovoltaic() {
		return isPhotovoltaic;
	}

	public void setIsPhotovoltaic(String isPhotovoltaic) {
		this.isPhotovoltaic = isPhotovoltaic;
	}

	@ExcelField(title = "是否愿意安装光伏", dictType="villager.family.yesno",  align = 2, sort = 22, required = false)
	public String getIsInstallPho() {
		return isInstallPho;
	}

	public void setIsInstallPho(String isInstallPho) {
		this.isInstallPho = isInstallPho;
	}

	@ExcelField(title = "可装光伏面积",  align = 2, sort = 23, required = false)
	public String getInstallablePhoArea() {
		return installablePhoArea;
	}

	public void setInstallablePhoArea(String installablePhoArea) {
		this.installablePhoArea = installablePhoArea;
	}

	@ExcelField(title = "存款银行",  align = 2, sort = 24, required = false)
	public String getDepositBank() {
		return depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	@ExcelField(title = "存款金额",  align = 2, sort = 25, required = false)
	public String getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}

	@ExcelField(title = "是否有贷款", dictType="villager.family.yesno",  align = 2, sort = 26, required = false)
	public String getIsLoan() {
		return isLoan;
	}

	public void setIsLoan(String isLoan) {
		this.isLoan = isLoan;
	}

	@ExcelField(title = "贷款金额",  align = 2, sort = 27, required = false)
	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	@ExcelField(title = "贷款余额",  align = 2, sort = 28, required = false)
	public String getLoanBalance() {
		return loanBalance;
	}

	public void setLoanBalance(String loanBalance) {
		this.loanBalance = loanBalance;
	}

	@ExcelField(title = "现有贷款利率",  align = 2, sort = 29, required = false)
	public String getLoanRate() {
		return loanRate;
	}

	public void setLoanRate(String loanRate) {
		this.loanRate = loanRate;
	}

	@ExcelField(title = "可接受贷款利率",  align = 2, sort = 30, required = false)
	public String getAcceptableLoanRate() {
		return acceptableLoanRate;
	}

	public void setAcceptableLoanRate(String acceptableLoanRate) {
		this.acceptableLoanRate = acceptableLoanRate;
	}

}
