/**
 * .
 */
package com.cmig.future.csportal.module.base.entity;

import com.cmig.future.csportal.common.config.Global;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hand.hap.mybatis.annotation.Condition;
import com.hand.hap.system.dto.BaseDTO;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.persistence.Transient;
import java.util.Date;

/**
 * 数据Entity类
 * @author ThinkGem
 * @version 2014-05-16
 */
public class BaseExtDTO extends BaseDTO {

	private static final long serialVersionUID = 1L;

    /**
     * 删除标记（0：正常；1：删除；2：审核；）
     */
    public static final String DEL_FLAG_NORMAL = "0";
    public static final String DEL_FLAG_DELETE = "1";
    public static final String DEL_FLAG_AUDIT = "2";

	/**
	 * 启用标记（0：正常；1：停用;）
	 */
	public static final String UNABLE_FLAG_NORMAL = "0";
	public static final String UNABLE_FLAG_STOP = "1";

	/**
	 * 启用标记（Y：正常；N：停用;）
	 */
	public static final String STATUS_NORMAL = "Y";
	public static final String STATUS_UNABLE = "N";

	@Transient
    @JsonIgnore
	protected String delFlag; 	// 删除标记（0：正常；1：删除；2：审核）
	@Transient
    @JsonIgnore
	protected String token;	//user token
	@Transient
    @JsonIgnore
    protected boolean readonly ;//是否只读

    /**
     * 文件服务器地址
     */
    @Transient
    private String fastdfsImageServer;

	public BaseExtDTO() {
		super();
		this.delFlag = DEL_FLAG_NORMAL;
	}

    @Column(updatable = false)
    @Condition(exclude = true)
    private Date creationDate;
	
	@JsonIgnore
	@Length(min=1, max=1)
	public String getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(String delFlag) {
		this.delFlag = delFlag;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

    public boolean getReadonly(){
        return this.readonly;
    }

    public void setReadonly(boolean readonly){
        this.readonly=readonly;
    }

    public String getFastdfsImageServer() {
        return fastdfsImageServer==null? Global.getImageServerPath():fastdfsImageServer;
    }

    public void setFastdfsImageServer(String fastdfsImageServer) {
        this.fastdfsImageServer = fastdfsImageServer;
    }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    @Override
    @JsonIgnore
    public Long getObjectVersionNumber() {
        return super.getObjectVersionNumber();
    }
}
