package com.cmig.future.csportal.module.properties.base.view.dto;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;

/**
 * Created by dell on 2017/6/8.
 */
public class MgtViewPojo extends BaseExtDTO {

    private Long  versionId;

    private String communityId;

    private String viewId;

    private Long parentStructureId;

    private String viewCode;

    private String name;

    private String nickName;

    private String fullName;

    private String pName;

    private String type;

    private String status;

    private String sourceCode;

    private String isBuilding;

    public String getIsBuilding() {
        return isBuilding;
    }

    public void setIsBuilding(String isBuilding) {
        this.isBuilding = isBuilding;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getViewId() {
        return viewId;
    }

    public void setViewId(String viewId) {
        this.viewId = viewId;
    }

    public Long getParentStructureId() {
        return parentStructureId;
    }

    public void setParentStructureId(Long parentStructureId) {
        this.parentStructureId = parentStructureId;
    }

    public String getViewCode() {
        return viewCode;
    }

    public void setViewCode(String viewCode) {
        this.viewCode = viewCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSourceCode() {
        return sourceCode;
    }

    public void setSourceCode(String sourceCode) {
        this.sourceCode = sourceCode;
    }
}
