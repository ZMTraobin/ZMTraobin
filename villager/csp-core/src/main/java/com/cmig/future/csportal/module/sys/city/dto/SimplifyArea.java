package com.cmig.future.csportal.module.sys.city.dto;

import java.util.List;

/**
 * LjhSysArea
 * 城市
 * @author su
 *
 * 2018
 */
public class SimplifyArea {
	
      private String id;

      private String name;

      private String code;
      
      private String type;
      
      private List<SimplifyArea> children;

     public void setId(String id){
         this.id = id;
     }

     public String getId(){
         return id;
     }

     public void setName(String name){
         this.name = name;
     }

     public String getName(){
         return name;
     }

     public void setCode(String code){
         this.code = code;
     }

     public String getCode(){
         return code;
     }

	public List<SimplifyArea> getChildren() {
		return children;
	}

	public void setChildren(List<SimplifyArea> children) {
		this.children = children;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

     }
