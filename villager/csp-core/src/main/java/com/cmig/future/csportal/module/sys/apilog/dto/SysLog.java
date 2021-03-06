package com.cmig.future.csportal.module.sys.apilog.dto;

import com.alibaba.fastjson.annotation.JSONField;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Auto Generated By Hap Code Generator
 **/
@ExtensionAttribute(disable = false)
@Table(name = "csp_ljh_sys_log")
public class SysLog extends BaseDTO {
    @Id
    @GeneratedValue
    private String id;

    private String appUserId;

	@JSONField(format="yyyy-MM-dd HH:mm:ss")
	private Date createTime;

    private String remoteAddr;

    private String userAgent;

    private String requestUri;

    private String method;

    private String params;

    private String exception;

    private String appId;

    private String deviceType;

    private String deviceModel;

    private String remoteIp;

    private String systemVersion;

    private String macAddress;

    private String imei;

    private Long respTime;

    private String location;

    private String nettype;

    private String devoperators;

    private String equipmentModel;

    private String appVersion;


    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAppUserId(String appUserId) {
        this.appUserId = appUserId;
    }

    public String getAppUserId() {
        return appUserId;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setRemoteAddr(String remoteAddr) {
        this.remoteAddr = remoteAddr;
    }

    public String getRemoteAddr() {
        return remoteAddr;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setRequestUri(String requestUri) {
        this.requestUri = requestUri;
    }

    public String getRequestUri() {
        return requestUri;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getMethod() {
        return method;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getParams() {
        return params;
    }

    public void setException(String exception) {
        this.exception = exception;
    }

    public String getException() {
        return exception;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppId() {
        return appId;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceModel(String deviceModel) {
        this.deviceModel = deviceModel;
    }

    public String getDeviceModel() {
        return deviceModel;
    }

    public void setRemoteIp(String remoteIp) {
        this.remoteIp = remoteIp;
    }

    public String getRemoteIp() {
        return remoteIp;
    }

    public void setSystemVersion(String systemVersion) {
        this.systemVersion = systemVersion;
    }

    public String getSystemVersion() {
        return systemVersion;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setImei(String imei) {
        this.imei = imei;
    }

    public String getImei() {
        return imei;
    }

    public void setRespTime(Long respTime) {
        this.respTime = respTime;
    }

    public Long getRespTime() {
        return respTime;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public void setNettype(String nettype) {
        this.nettype = nettype;
    }

    public String getNettype() {
        return nettype;
    }

    public void setDevoperators(String devoperators) {
        this.devoperators = devoperators;
    }

    public String getDevoperators() {
        return devoperators;
    }

    public void setEquipmentModel(String equipmentModel) {
        this.equipmentModel = equipmentModel;
    }

    public String getEquipmentModel() {
        return equipmentModel;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getAppVersion() {
        return appVersion;
    }

	@Override
	public String toString() {
		return "SysLog{" +
				"id='" + id + '\'' +
				", appUserId='" + appUserId + '\'' +
				", createTime=" + createTime +
				", remoteAddr='" + remoteAddr + '\'' +
				", userAgent='" + userAgent + '\'' +
				", requestUri='" + requestUri + '\'' +
				", method='" + method + '\'' +
				", params='" + params + '\'' +
				", exception='" + exception + '\'' +
				", appId='" + appId + '\'' +
				", deviceType='" + deviceType + '\'' +
				", deviceModel='" + deviceModel + '\'' +
				", remoteIp='" + remoteIp + '\'' +
				", systemVersion='" + systemVersion + '\'' +
				", macAddress='" + macAddress + '\'' +
				", imei='" + imei + '\'' +
				", respTime=" + respTime +
				", location='" + location + '\'' +
				", nettype='" + nettype + '\'' +
				", devoperators='" + devoperators + '\'' +
				", equipmentModel='" + equipmentModel + '\'' +
				", appVersion='" + appVersion + '\'' +
				'}';
	}
}
