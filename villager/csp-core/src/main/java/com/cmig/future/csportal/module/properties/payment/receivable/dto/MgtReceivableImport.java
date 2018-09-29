package com.cmig.future.csportal.module.properties.payment.receivable.dto;


import com.cmig.future.csportal.common.utils.excel.annotation.ExcelField;
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import org.hibernate.validator.constraints.NotEmpty;

import java.math.BigDecimal;

public class MgtReceivableImport extends BaseExtDTO {

    @NotEmpty
    private String sourceSystem;

    @NotEmpty
    private String sourceReceivableId;

    @NotEmpty
    private String sourceBuildCode;

    @NotEmpty
    private String expenditure;

    @NotEmpty
    private String periodName;

    @NotEmpty
    private Long totalAmount;

    private Long discountAmount;

    private Long breakContractAmount;

    private String backUrl;

    private String description;

    @NotEmpty
    private String buildingType;

    private Long receivableAmount;

    private Long buildingId;

	private BigDecimal paymentArea;

	public BigDecimal getPaymentArea() {
		return paymentArea;
	}

	public void setPaymentArea(BigDecimal paymentArea) {
		this.paymentArea = paymentArea;
	}

	@ExcelField(title = "应用标识(必填)", dictType="OPEN.SOURCE_SYSTEM", align = 2, sort = 1, required = true)
    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    //@ExcelField(title = "源系统应收ID(必填)", align = 2, sort = 2, required = true)
    public String getSourceReceivableId() {
        return sourceReceivableId;
    }

    public void setSourceReceivableId(String sourceReceivableId) {
        this.sourceReceivableId = sourceReceivableId;
    }

    @ExcelField(title = "建筑实体类型(必填)", dictType="MGT.BUILDING_TYPE",align = 2, sort = 3, required = true)
    public String getBuildingType() {
        return buildingType;
    }

    public void setBuildingType(String buildingType) {
        this.buildingType = buildingType;
    }

    @ExcelField(title = "源系统建筑编码(必填)", align = 2, sort = 4, required = true)
    public String getSourceBuildCode() {
        return sourceBuildCode;
    }

    public void setSourceBuildCode(String sourceBuildCode) {
        this.sourceBuildCode = sourceBuildCode;
    }

    @ExcelField(title = "费项(必填)", dictType="MGT.EXPENDITURE_TYPE", align = 2, sort = 5, required = true)
    public String getExpenditure() {
        return expenditure;
    }

    public void setExpenditure(String expenditure) {
        this.expenditure = expenditure;
    }

    @ExcelField(title = "期间(必填)", align = 2, sort = 6, required = true)
    public String getPeriodName() {
        return periodName;
    }

    public void setPeriodName(String periodName) {
        this.periodName = periodName;
    }

    @ExcelField(title = "总金额(分)(必填)", align = 2, sort = 7, required = true)
    public Long getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(Long totalAmount) {
        this.totalAmount = totalAmount;
    }

    @ExcelField(title = "折扣金额(分)", align = 2, sort = 8, required = false)
    public Long getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(Long discountAmount) {
        this.discountAmount = discountAmount;
    }

    @ExcelField(title = "违约金(分)", align = 2, sort = 9, required = false)
    public Long getBreakContractAmount() {
        return breakContractAmount;
    }

    public void setBreakContractAmount(Long breakContractAmount) {
        this.breakContractAmount = breakContractAmount;
    }

    //@ExcelField(title = "支付成功通知地址", align = 2, sort = 10, required = false)
    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    @ExcelField(title = "描述", align = 2, sort = 11, required = false)
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Long getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(Long receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public Long getBuildingId() {
        return buildingId;
    }

    public void setBuildingId(Long buildingId) {
        this.buildingId = buildingId;
    }
}
