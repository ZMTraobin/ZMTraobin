package com.cmig.future.csportal.module.properties.base.customer.dto;

import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.Date;

@ExtensionAttribute(disable = true)
public class BpOwnerImport extends BaseDTO {
    private Long bpOwnerId;

    // 基本信息表
    private Long bpId;

    private String bpCode;

    @NotEmpty
    private String bpName;

    private String bpNickName;

    @NotEmpty
    private String idType;

    @NotEmpty
    private String idNo;

    private String photo;

    private String mobile;

    private String phone;

    private String email;

    private String gender;

    private Date birthday;

    private Long age;

    // 房屋信息
    @NotEmpty
    private String structureCode;
    @NotEmpty
    private String structureName;
    //房屋关系
    private String effectiveStartDate;
    private String effectiveEndDate;

	private Long buildingId;

	public Long getBuildingId() {
		return buildingId;
	}

	public void setBuildingId(Long buildingId) {
		this.buildingId = buildingId;
	}

	public void setBpOwnerId(Long bpOwnerId) {
        this.bpOwnerId = bpOwnerId;
    }

    public Long getBpOwnerId() {
        return bpOwnerId;
    }

    public void setBpId(Long bpId) {
        this.bpId = bpId;
    }

    public Long getBpId() {
        return bpId;
    }

    public String getBpCode() {
        return bpCode;
    }

    public void setBpCode(String bpCode) {
        this.bpCode = bpCode;
    }
    
    @ExcelField(title = "业主名称(必填)", align = 2, sort = 20, required = true)
    public String getBpName() {
        return bpName;
    }

    public void setBpName(String bpName) {
        this.bpName = bpName;
    }

    public String getBpNickName() {
        return bpNickName;
    }

    public void setBpNickName(String bpNickName) {
        this.bpNickName = bpNickName;
    }

    @ExcelField(title = "证件类型(必填)", dictType="CUSTOMER.CARD_TYPE",align = 2, sort = 30, required = true)
    public String getIdType() {
        return idType;
    }
    
    public void setIdType(String idType) {
        this.idType = idType;
    }

    @ExcelField(title = "证件号码(必填)", align = 2, sort = 40, required = true)
    public String getIdNo() {
        return idNo;
    }

    public void setIdNo(String idNo) {
        this.idNo = idNo;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    @ExcelField(title = "手机号(必填)", align = 2, sort = 50, required = true)
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @ExcelField(title = "性别(必填)", dictType="HR.EMPLOYEE_GENDER",align = 2, sort = 60, required = true)
    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public Long getAge() {
        return age;
    }

    public void setAge(Long age) {
        this.age = age;
    }
    
    @ExcelField(title = "建筑代码(必填)", align = 2, sort = 1, required = true)
    public String getStructureCode() {
        return structureCode;
    }

    public void setStructureCode(String structureCode) {
        this.structureCode = structureCode;
    }
    
    @ExcelField(title = "建筑名称(必填)", align = 2, sort = 10, required = true)
    public String getStructureName() {
        return structureName;
    }

    public void setStructureName(String structureName) {
        this.structureName = structureName;
    }

    @ExcelField(title = "服务开始日期(yyyymmdd)(必填)", align = 2, sort = 70, required = true)
    public String getEffectiveStartDate() {
        return effectiveStartDate;
    }

    public void setEffectiveStartDate(String effectiveStartDate) {
        this.effectiveStartDate = effectiveStartDate;
    }

    @ExcelField(title = "服务截止日期(yyyymmdd)(必填)", align = 2, sort = 80, required = true)
    public String getEffectiveEndDate() {
        return effectiveEndDate;
    }

    public void setEffectiveEndDate(String effectiveEndDate) {
        this.effectiveEndDate = effectiveEndDate;
    }
    

}
