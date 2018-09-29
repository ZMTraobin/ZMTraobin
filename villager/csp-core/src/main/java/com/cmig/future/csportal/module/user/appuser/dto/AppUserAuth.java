package com.cmig.future.csportal.module.user.appuser.dto;

/**Auto Generated By Hap Code Generator**/
import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import com.hand.hap.system.dto.BaseDTO;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Id;
import javax.persistence.Table;
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_app_user_auth")
public class AppUserAuth extends BaseExtDTO {
     @Id
      private String authId;

	@NotEmpty
      private String appUserId;

     @NotEmpty
      private String identityType;

     @NotEmpty
      private String uuid;

      private String nickName;

      private String avatar;


     public void setAuthId(String authId){
         this.authId = authId;
     }

     public String getAuthId(){
         return authId;
     }

     public void setAppUserId(String appUserId){
         this.appUserId = appUserId;
     }

     public String getAppUserId(){
         return appUserId;
     }

     public void setIdentityType(String identityType){
         this.identityType = identityType;
     }

     public String getIdentityType(){
         return identityType;
     }

     public void setUuid(String uuid){
         this.uuid = uuid;
     }

     public String getUuid(){
         return uuid;
     }

     public void setNickName(String nickName){
         this.nickName = nickName;
     }

     public String getNickName(){
         return nickName;
     }

     public void setAvatar(String avatar){
         this.avatar = avatar;
     }

     public String getAvatar(){
         return avatar;
     }

     }
