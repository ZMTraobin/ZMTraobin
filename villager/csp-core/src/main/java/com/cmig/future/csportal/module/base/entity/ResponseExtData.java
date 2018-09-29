package com.cmig.future.csportal.module.base.entity;

import com.hand.hap.system.dto.ResponseData;

import java.util.List;

/**
 * Created by zhangtao107@126.com on 2017/3/27.
 */
public class ResponseExtData extends ResponseData {

    private Object dto;

    public Object getDto() {
        return dto;
    }

    public void setDto(Object dto) {
        this.dto = dto;
    }

    public ResponseExtData(Object obj){
        super();
        if(obj instanceof List){

        }else{
            this.setRows(null);
            this.setTotal(new Long("1"));
            this.dto=obj;
        }
    }
}
