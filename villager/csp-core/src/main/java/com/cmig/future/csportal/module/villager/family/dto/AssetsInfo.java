package com.cmig.future.csportal.module.villager.family.dto;


import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable=true)
@Table(name = "xl_assets_info")
public class AssetsInfo extends BaseExtDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
     * 资金信息
     */
	@Id
    @GeneratedValue
    private Long id;
	
	private String familyCode;
	
	private String depositBank;
	
	private String depositAmount;
	
	private String isLoan;
	
	private String loanAmount;
	
	private String loanBalance;
	
	private String loanRate;
	
	private String acceptableLoanRate;
	
	public void setId(Long id){
        this.id = id;
    }

    public Long getId(){
        return id;
    }

	public String getDepositBank() {
		return depositBank;
	}

	public void setDepositBank(String depositBank) {
		this.depositBank = depositBank;
	}

	public String getDepositAmount() {
		return depositAmount;
	}

	public void setDepositAmount(String depositAmount) {
		this.depositAmount = depositAmount;
	}

	public String getIsLoan() {
		return isLoan;
	}

	public void setIsLoan(String isLoan) {
		this.isLoan = isLoan;
	}

	public String getLoanAmount() {
		return loanAmount;
	}

	public void setLoanAmount(String loanAmount) {
		this.loanAmount = loanAmount;
	}

	public String getLoanBalance() {
		return loanBalance;
	}

	public void setLoanBalance(String loanBalance) {
		this.loanBalance = loanBalance;
	}

	public String getLoanRate() {
		return loanRate;
	}

	public void setLoanRate(String loanRate) {
		this.loanRate = loanRate;
	}

	public String getAcceptableLoanRate() {
		return acceptableLoanRate;
	}

	public void setAcceptableLoanRate(String acceptableLoanRate) {
		this.acceptableLoanRate = acceptableLoanRate;
	}
	
	public String getFamilyCode() {
		return familyCode;
	}

	public void setFamilyCode(String familyCode) {
		this.familyCode = familyCode;
	}

}
