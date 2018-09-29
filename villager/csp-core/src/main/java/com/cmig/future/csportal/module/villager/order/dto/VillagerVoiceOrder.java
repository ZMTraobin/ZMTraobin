package com.cmig.future.csportal.module.villager.order.dto;

/**Auto Generated By Hap Code Generator**/

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.cmig.future.csportal.module.villager.good.dto.VillagerGood;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.util.Date;

@ExtensionAttribute(disable=true)
@Table(name = "csp_villager_voice_order")
public class VillagerVoiceOrder extends BaseExtDTO {
     @Id
     @GeneratedValue
      private Long id;

     @NotEmpty
      private String appUserId;
      private String communityId;

     private String mgtUserId;

     @NotEmpty
      private String orderNumber;

     @NotNull
      private Long orderAmount;

     @NotNull
      private Long discountAmount;

     @NotNull
      private Long integralAmount;

     @NotNull
      private Long payableAmount;

      private Long paidAmount;

      private Date timeExpire;

      private Date timePaid;

      private Date timeSend;

      private Date timeSettle;

      private Date timeReceived;

      private Date timeReceivedGood;

      private String goodId;

      private String goodName;

     @NotNull
      private Long goodNum;

     @NotEmpty
      private String clientIp;

     @NotEmpty
      private String payStatus;

      private String orderStatus;

      private String orderType;

      private String description;

      private String voiceUrl;

      private String voiceContent;


    @Transient
    private VillagerGood good;
    @Transient
    private String mobile;
    @Transient
    private String userName;
    @Transient
    private String receiverName;
    @Transient
    private String communityName;


     public void setId(Long id){
         this.id = id;
     }

     public Long getId(){
         return id;
     }

     public void setAppUserId(String appUserId){
         this.appUserId = appUserId;
     }

     public String getAppUserId(){
         return appUserId;
     }

     public void setOrderNumber(String orderNumber){
         this.orderNumber = orderNumber;
     }

     public String getOrderNumber(){
         return orderNumber;
     }

     public void setOrderAmount(Long orderAmount){
         this.orderAmount = orderAmount;
     }

     public Long getOrderAmount(){
         return orderAmount;
     }

     public void setDiscountAmount(Long discountAmount){
         this.discountAmount = discountAmount;
     }

     public Long getDiscountAmount(){
         return discountAmount;
     }

     public void setIntegralAmount(Long integralAmount){
         this.integralAmount = integralAmount;
     }

     public Long getIntegralAmount(){
         return integralAmount;
     }

     public void setPayableAmount(Long payableAmount){
         this.payableAmount = payableAmount;
     }

     public Long getPayableAmount(){
         return payableAmount;
     }

     public void setPaidAmount(Long paidAmount){
         this.paidAmount = paidAmount;
     }

     public Long getPaidAmount(){
         return paidAmount;
     }

     public void setTimeExpire(Date timeExpire){
         this.timeExpire = timeExpire;
     }

     public Date getTimeExpire(){
         return timeExpire;
     }

     public void setTimePaid(Date timePaid){
         this.timePaid = timePaid;
     }

     public Date getTimePaid(){
         return timePaid;
     }

     public void setTimeSend(Date timeSend){
         this.timeSend = timeSend;
     }

     public Date getTimeSend(){
         return timeSend;
     }

     public void setTimeSettle(Date timeSettle){
         this.timeSettle = timeSettle;
     }

     public Date getTimeSettle(){
         return timeSettle;
     }

     public void setGoodId(String goodId){
         this.goodId = goodId;
     }

     public String getGoodId(){
         return goodId;
     }

     public void setGoodName(String goodName){
         this.goodName = goodName;
     }

     public String getGoodName(){
         return goodName;
     }

     public void setGoodNum(Long goodNum){
         this.goodNum = goodNum;
     }

     public Long getGoodNum(){
         return goodNum;
     }

     public void setClientIp(String clientIp){
         this.clientIp = clientIp;
     }

     public String getClientIp(){
         return clientIp;
     }

     public void setPayStatus(String payStatus){
         this.payStatus = payStatus;
     }

     public String getPayStatus(){
         return payStatus;
     }

     public void setOrderStatus(String orderStatus){
         this.orderStatus = orderStatus;
     }

     public String getOrderStatus(){
         return orderStatus;
     }

     public void setOrderType(String orderType){
         this.orderType = orderType;
     }

     public String getOrderType(){
         return orderType;
     }

     public void setDescription(String description){
         this.description = description;
     }

     public String getDescription(){
         return description;
     }

     public void setVoiceUrl(String voiceUrl){
         this.voiceUrl = voiceUrl;
     }

     public String getVoiceUrl(){
         return voiceUrl;
     }

     public void setVoiceContent(String voiceContent){
         this.voiceContent = voiceContent;
     }

     public String getVoiceContent(){
         return voiceContent;
     }

    public VillagerGood getGood() {
        return good;
    }

    public void setGood(VillagerGood good) {
        this.good = good;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMgtUserId() {
        return mgtUserId;
    }

    public void setMgtUserId(String mgtUserId) {
        this.mgtUserId = mgtUserId;
    }

    public Date getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(Date timeReceived) {
        this.timeReceived = timeReceived;
    }

    public Date getTimeReceivedGood() {
        return timeReceivedGood;
    }

    public void setTimeReceivedGood(Date timeReceivedGood) {
        this.timeReceivedGood = timeReceivedGood;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }
}
