package com.cmig.future.csportal.module.sys.job.dto;

import com.hand.hap.core.annotation.Children;
import com.hand.hap.job.dto.JobData;

import javax.persistence.Id;
import java.util.List;

import javax.persistence.Transient;

public class JobDetailDto {
    @Id
    private String schedName;
    @Id
    private String jobName;
    @Id
    private String jobGroup;
    private String description;
    private String jobClassName;
    private String isDurable;
    private String isNonconcurrent;
    private String isUpdateData;
    private String requestsRecovery;
    @Children
    private List<JobData> jobDatas;

    @Transient
    private int page;
    @Transient
    private int pagesize;

    public JobDetailDto() {
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getPagesize() {
        return pagesize;
    }

    public void setPagesize(int pagesize) {
        this.pagesize = pagesize;
    }

    public String getSchedName() {
        return this.schedName;
    }

    public void setSchedName(String schedName) {
        this.schedName = schedName;
    }

    public String getJobName() {
        return this.jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getJobGroup() {
        return this.jobGroup;
    }

    public void setJobGroup(String jobGroup) {
        this.jobGroup = jobGroup;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description == null ? null : description.trim();
    }

    public String getJobClassName() {
        return this.jobClassName;
    }

    public void setJobClassName(String jobClassName) {
        this.jobClassName = jobClassName == null ? null : jobClassName.trim();
    }

    public String getIsDurable() {
        return this.isDurable;
    }

    public void setIsDurable(String isDurable) {
        this.isDurable = isDurable == null ? null : isDurable.trim();
    }

    public String getIsNonconcurrent() {
        return this.isNonconcurrent;
    }

    public void setIsNonconcurrent(String isNonconcurrent) {
        this.isNonconcurrent = isNonconcurrent == null ? null : isNonconcurrent.trim();
    }

    public String getIsUpdateData() {
        return this.isUpdateData;
    }

    public void setIsUpdateData(String isUpdateData) {
        this.isUpdateData = isUpdateData == null ? null : isUpdateData.trim();
    }

    public String getRequestsRecovery() {
        return this.requestsRecovery;
    }

    public void setRequestsRecovery(String requestsRecovery) {
        this.requestsRecovery = requestsRecovery == null ? null : requestsRecovery.trim();
    }

    public List<JobData> getJobDatas() {
        return this.jobDatas;
    }

    public void setJobDatas(List<JobData> jobDatas) {
        this.jobDatas = jobDatas;
    }
}

