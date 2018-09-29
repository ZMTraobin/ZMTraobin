package com.cmig.future.csportal.module.lifepay.dto;

import java.math.BigDecimal;
import java.util.List;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 15:20 2017/11/21.
 * @Modified by zhangtao on 15:20 2017/11/21.
 */
public class BillResponse {

	/**
	 * data : [{"barcode":"0830201706014000008100006003","billAddr":"","billAmt":60,"billId":"201711221000282993","billMonth":"201706","billOrgId":"888880001602900","billOrgName":"上海青浦煤气管理所","billRecordTimes":"30","billStatus":"00","isInsurance":"N","overdueFee":0}]
	 * code : 0
	 * msg : 请求成功
	 */

	private String code;
	private String msg;
	private List<BillInfo> data;

	BillResponse() {

	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public List<BillInfo> getData() {
		return data;
	}

	public void setData(List<BillInfo> data) {
		this.data = data;
	}


	public static class BillInfo {
		/**
		 * barcode : 0830201706014000008100006003
		 * billAddr :
		 * billAmt : 60
		 * billId : 201711221000282993
		 * billMonth : 201706
		 * billOrgId : 888880001602900
		 * billOrgName : 上海青浦煤气管理所
		 * billRecordTimes : 30
		 * billStatus : 00
		 * isInsurance : N
		 * overdueFee : 0
		 */

		private String barcode;
		private String billAddr;
		private BigDecimal billAmt;
		private String billId;
		private String billMonth;
		private String billOrgId;
		private String billOrgName;
		private String billRecordTimes;
		private String billStatus;
		private String isInsurance;
		private BigDecimal overdueFee;
		private String billNo;
		private String billOwner;

		public String getBarcode() {
			return barcode;
		}

		public void setBarcode(String barcode) {
			this.barcode = barcode;
		}

		public String getBillAddr() {
			return billAddr;
		}

		public void setBillAddr(String billAddr) {
			this.billAddr = billAddr;
		}

		public BigDecimal getBillAmt() {
			return billAmt;
		}

		public void setBillAmt(BigDecimal billAmt) {
			this.billAmt = billAmt;
		}

		public String getBillId() {
			return billId;
		}

		public void setBillId(String billId) {
			this.billId = billId;
		}

		public String getBillMonth() {
			return billMonth;
		}

		public void setBillMonth(String billMonth) {
			this.billMonth = billMonth;
		}

		public String getBillOrgId() {
			return billOrgId;
		}

		public void setBillOrgId(String billOrgId) {
			this.billOrgId = billOrgId;
		}

		public String getBillOrgName() {
			return billOrgName;
		}

		public void setBillOrgName(String billOrgName) {
			this.billOrgName = billOrgName;
		}

		public String getBillRecordTimes() {
			return billRecordTimes;
		}

		public void setBillRecordTimes(String billRecordTimes) {
			this.billRecordTimes = billRecordTimes;
		}

		public String getBillStatus() {
			return billStatus;
		}

		public void setBillStatus(String billStatus) {
			this.billStatus = billStatus;
		}

		public String getIsInsurance() {
			return isInsurance;
		}

		public void setIsInsurance(String isInsurance) {
			this.isInsurance = isInsurance;
		}

		public BigDecimal getOverdueFee() {
			return overdueFee;
		}

		public void setOverdueFee(BigDecimal overdueFee) {
			this.overdueFee = overdueFee;
		}

		public String getBillNo() {
			return billNo;
		}

		public void setBillNo(String billNo) {
			this.billNo = billNo;
		}

		public String getBillOwner() {
			return billOwner;
		}

		public void setBillOwner(String billOwner) {
			this.billOwner = billOwner;
		}
	}
}
