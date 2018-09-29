package com.cmig.future.csportal.module.properties.authorManager.dto;

import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;

@ExtensionAttribute(disable = true)
@Table(name = "csp_ljh_sys_user_community")
public class LjhSysUserCommunity extends BaseExtDTO {
    @Id
    // @GeneratedValue
    private String id;

    private Long sysUserId;

    private String communityId;

    private String delFlag;

    @Transient
    private String communityName;
    @Transient
    private String cityName;
    @Transient
    private String companyName;

    public String getCommunityName() {
        return communityName;
    }

    public void setCommunityName(String communityName) {
        this.communityName = communityName;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setSysUserId(Long sysUserId) {
        this.sysUserId = sysUserId;
    }

    public Long getSysUserId() {
        return sysUserId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

}
