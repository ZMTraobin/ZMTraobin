package com.cmig.future.csportal.module.sys.logs.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import java.util.Date;
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_login_log")
public class LjhLoginLog extends BaseDTO {
     @Id
     @GeneratedValue
      private Long id;

      private String logType;

      private String userId;

      private Date creationDate;

      private String ip;

      private String token;


     public void setId(Long id){
         this.id = id;
     }

     public Long getId(){
         return id;
     }

     public void setLogType(String logType){
         this.logType = logType;
     }

     public String getLogType(){
         return logType;
     }

     public void setUserId(String userId){
         this.userId = userId;
     }

     public String getUserId(){
         return userId;
     }

    @Override
    public Date getCreationDate() {
        return creationDate;
    }

    @Override
    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setIp(String ip){
         this.ip = ip;
     }

     public String getIp(){
         return ip;
     }

     public void setToken(String token){
         this.token = token;
     }

     public String getToken(){
         return token;
     }

     }
