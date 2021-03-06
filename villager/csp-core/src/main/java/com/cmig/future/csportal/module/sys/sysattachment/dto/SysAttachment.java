package com.cmig.future.csportal.module.sys.sysattachment.dto;

/**Auto Generated By Hap Code Generator**/
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import com.cmig.future.csportal.module.base.entity.BaseExtDTO;
import com.hand.hap.mybatis.annotation.ExtensionAttribute;
import javax.persistence.Table;
import com.hand.hap.system.dto.BaseDTO;
import javax.validation.constraints.NotNull;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * 附件管理Entity
 * @author bubu
 * @version 2017年4月5日
 */
@ExtensionAttribute(disable=true)
@Table(name = "csp_ljh_sys_attachment")
public class SysAttachment extends BaseDTO {
     @Id
      private String id;

      private String type;

      private String objectClass;

      private String objectId;

     @NotEmpty
      private String fileName;

      private String filePath;

     @NotNull
      private Long fileSize;

      private String fileType;

      private String saveType;


     public void setId(String id){
         this.id = id;
     }

     public String getId(){
         return id;
     }

     public void setType(String type){
         this.type = type;
     }

     public String getType(){
         return type;
     }

     public void setObjectClass(String objectClass){
         this.objectClass = objectClass;
     }

     public String getObjectClass(){
         return objectClass;
     }

     public void setObjectId(String objectId){
         this.objectId = objectId;
     }

     public String getObjectId(){
         return objectId;
     }

     public void setFileName(String fileName){
         this.fileName = fileName;
     }

     public String getFileName(){
         return fileName;
     }

     public void setFilePath(String filePath){
         this.filePath = filePath;
     }

     public String getFilePath(){
         return filePath;
     }

     public void setFileSize(Long fileSize){
         this.fileSize = fileSize;
     }

     public Long getFileSize(){
         return fileSize;
     }

     public void setFileType(String fileType){
         this.fileType = fileType;
     }

     public String getFileType(){
         return fileType;
     }

     public void setSaveType(String saveType){
         this.saveType = saveType;
     }

     public String getSaveType(){
         return saveType;
     }

     }
