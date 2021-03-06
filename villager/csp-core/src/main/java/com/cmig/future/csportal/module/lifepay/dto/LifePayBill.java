package com.cmig.future.csportal.module.lifepay.dto;

/**Auto Generated By Hap Code Generator**/

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.pay.order.dto.OrderForm;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "csp_life_pay_bill")
public class LifePayBill extends BaseExtDTO {
     @Id
     @GeneratedValue
      private Long id;

     @NotEmpty
      private String productType;

     @NotEmpty
      private String productId;

     @NotEmpty
      private String billId;

     @NotEmpty
      private String billOrgId;

     @NotEmpty
      private String billOrgName;

     @NotEmpty
      private String billNo;

     @NotEmpty
      private String billMonth;

     @NotNull
      private Long billAmt;

      private String billRecordTimes;

      private String barcode;

      private String doorCode;

      private String billAddr;

      private String billOwner;

     @NotNull
      private Long overdueFee;

      private String isInsurance;

      private String billStatus;

      private Date orderNotifyTime;

      private String orderNotifyStatus;

      private String thirdOrderNo;

	private String searchType;//

	@Transient
	private Long useIntegralNum;//抵扣积分数
	@Transient
	private String clientIp;    // 客户端ipv4地址
	@Transient
	private String paymentName;    // 支付人姓名
	@Transient
	private String backUrl;//服务器通知地址
	@Transient
	private String frontUrl;//页面通知地址
	@Transient
	private OrderForm orderForm;
	@Transient
	private int recursiveNum=0;//递归次数


     public void setId(Long id){
         this.id = id;
     }

     public Long getId(){
         return id;
     }

     public void setProductType(String productType){
         this.productType = productType;
     }

     public String getProductType(){
         return productType;
     }

     public void setProductId(String productId){
         this.productId = productId;
     }

     public String getProductId(){
         return productId;
     }

     public void setBillId(String billId){
         this.billId = billId;
     }

     public String getBillId(){
         return billId;
     }

     public void setBillOrgId(String billOrgId){
         this.billOrgId = billOrgId;
     }

     public String getBillOrgId(){
         return billOrgId;
     }

     public void setBillOrgName(String billOrgName){
         this.billOrgName = billOrgName;
     }

     public String getBillOrgName(){
         return billOrgName;
     }

     public void setBillNo(String billNo){
         this.billNo = billNo;
     }

     public String getBillNo(){
         return billNo;
     }

     public void setBillMonth(String billMonth){
         this.billMonth = billMonth;
     }

     public String getBillMonth(){
         return billMonth;
     }

     public void setBillAmt(Long billAmt){
         this.billAmt = billAmt;
     }

     public Long getBillAmt(){
         return billAmt;
     }

     public void setBillRecordTimes(String billRecordTimes){
         this.billRecordTimes = billRecordTimes;
     }

     public String getBillRecordTimes(){
         return billRecordTimes;
     }

     public void setBarcode(String barcode){
         this.barcode = barcode;
     }

     public String getBarcode(){
         return barcode;
     }

     public void setBillAddr(String billAddr){
         this.billAddr = billAddr;
     }

     public String getBillAddr(){
         return billAddr;
     }

     public void setBillOwner(String billOwner){
         this.billOwner = billOwner;
     }

     public String getBillOwner(){
         return billOwner;
     }

     public void setOverdueFee(Long overdueFee){
         this.overdueFee = overdueFee;
     }

     public Long getOverdueFee(){
         return overdueFee;
     }

     public void setIsInsurance(String isInsurance){
         this.isInsurance = isInsurance;
     }

     public String getIsInsurance(){
         return isInsurance;
     }

     public void setBillStatus(String billStatus){
         this.billStatus = billStatus;
     }

     public String getBillStatus(){
         return billStatus;
     }

     public void setOrderNotifyTime(Date orderNotifyTime){
         this.orderNotifyTime = orderNotifyTime;
     }

     public Date getOrderNotifyTime(){
         return orderNotifyTime;
     }

     public void setOrderNotifyStatus(String orderNotifyStatus){
         this.orderNotifyStatus = orderNotifyStatus;
     }

     public String getOrderNotifyStatus(){
         return orderNotifyStatus;
     }

	public Long getUseIntegralNum() {
		return useIntegralNum;
	}

	public void setUseIntegralNum(Long useIntegralNum) {
		this.useIntegralNum = useIntegralNum;
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

	public String getPaymentName() {
		return paymentName;
	}

	public void setPaymentName(String paymentName) {
		this.paymentName = paymentName;
	}

	public String getBackUrl() {
		return backUrl;
	}

	public void setBackUrl(String backUrl) {
		this.backUrl = backUrl;
	}

	public String getFrontUrl() {
		return frontUrl;
	}

	public void setFrontUrl(String frontUrl) {
		this.frontUrl = frontUrl;
	}

	public OrderForm getOrderForm() {
		return orderForm;
	}

	public void setOrderForm(OrderForm orderForm) {
		this.orderForm = orderForm;
	}

	public String getSearchType() {
		return searchType;
	}

	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}

	public String getDoorCode() {
		return doorCode;
	}

	public void setDoorCode(String doorCode) {
		this.doorCode = doorCode;
	}

	public String getThirdOrderNo() {
		return thirdOrderNo;
	}

	public void setThirdOrderNo(String thirdOrderNo) {
		this.thirdOrderNo = thirdOrderNo;
	}

	public int getRecursiveNum() {
		return recursiveNum;
	}

	public void setRecursiveNum(int recursiveNum) {
		this.recursiveNum = recursiveNum;
	}
}
