package com.cmig.future.csportal.module.operate.appUpgrade.dto;

import javax.persistence.Id;
import javax.persistence.Table;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_app_download")
public class LjhAppDownload extends BaseExtDTO { 
    @Id
    // @GeneratedValue
    private String id;

    private String appName;

    private String appType;

    private String appApplicationSystem;

    private String deviceType;

    private String version;

    private String versionNumber;

    private String versionFlag;

    private String ismupdatel;

    private String url;

    private String appSize;

    private String appContent;

    private String delFlag;

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppType(String appType) {
        this.appType = appType;
    }

    public String getAppType() {
        return appType;
    }

    public void setAppApplicationSystem(String appApplicationSystem) {
        this.appApplicationSystem = appApplicationSystem;
    }

    public String getAppApplicationSystem() {
        return appApplicationSystem;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getVersion() {
        return version;
    }

    public void setVersionNumber(String versionNumber) {
        this.versionNumber = versionNumber;
    }

    public String getVersionNumber() {
        return versionNumber;
    }

    public void setVersionFlag(String versionFlag) {
        this.versionFlag = versionFlag;
    }

    public String getVersionFlag() {
        return versionFlag;
    }

    public void setIsmupdatel(String ismupdatel) {
        this.ismupdatel = ismupdatel;
    }

    public String getIsmupdatel() {
        return ismupdatel;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppContent(String appContent) {
        this.appContent = appContent;
    }

    public String getAppContent() {
        return appContent;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

}
