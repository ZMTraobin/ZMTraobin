package com.cmig.future.csportal.module.pay.order.dto;

/**Auto Generated By Hap Code Generator**/
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.hand.hap.core.BaseConstants;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.Date;

@ExtensionAttribute(disable = true)
@Table(name = "csp_order_form_notify_mc")
public class OrderFormNotifyMc extends BaseExtDTO {
    @Id
    @GeneratedValue
    private Long id;

     @NotEmpty
      private Long orderId;

    @NotEmpty
    private String status;

    private Long times;

    private Date timeNotified;

    private Date timeNextNotify;

    @Transient
    private String backUrl;
    
    @Transient
    private String sourceSystem;
    
    @Transient
    private String sourceOrderNumber;

    @Transient
    private String orderNumber;

    @JsonFormat(pattern = BaseConstants.DATE_TIME_FORMAT)
    private Date creationDate;
    

    public String getSourceSystem() {
        return sourceSystem;
    }

    public void setSourceSystem(String sourceSystem) {
        this.sourceSystem = sourceSystem;
    }

    public String getSourceOrderNumber() {
        return sourceOrderNumber;
    }

    public void setSourceOrderNumber(String sourceOrderNumber) {
        this.sourceOrderNumber = sourceOrderNumber;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public String getBackUrl() {
        return backUrl;
    }

    public void setBackUrl(String backUrl) {
        this.backUrl = backUrl;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public void setStatus(String status){
         this.status = status;
     }

    public String getStatus() {
        return status;
    }

    public void setTimes(Long times) {
        this.times = times;
    }

    public Long getTimes() {
        return times;
    }

    public void setTimeNotified(Date timeNotified) {
        this.timeNotified = timeNotified;
    }

    public Date getTimeNotified() {
        return timeNotified;
    }

    public void setTimeNextNotify(Date timeNextNotify) {
        this.timeNextNotify = timeNextNotify;
    }

    public Date getTimeNextNotify() {
        return timeNextNotify;
    }

    public String getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }
}