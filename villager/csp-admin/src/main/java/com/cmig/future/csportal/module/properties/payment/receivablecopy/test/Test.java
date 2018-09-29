package com.cmig.future.csportal.module.properties.payment.receivablecopy.test;

import com.cmig.future.csportal.common.utils.HttpUtil;
import com.cmig.future.csportal.common.utils.sign.CspSignUtil;
import com.cmig.future.csportal.module.properties.payment.receivablecopy.service.IMgtReceivableCopyService;
import com.cmig.future.csportal.module.sys.service.SpringContextHolder;
import com.cmig.future.csportal.module.user.appuser.service.IAppUserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ThinkPad on 2017/8/7.
 */

public class Test {
    //private  IAppUserService appUserService = null;
 // private static IMgtReceivableCopyService mgtReceivableCopyService = SpringContextHolder.getBean("mgtReceivableCopyService");
  private static final String APP_ID="934dfcc043f941cfa5587e6c11762bad";
  private static final String PROPERTY = "PROPERTY_FEE";
  private static final String BUILDING_TYPE="HOUSE";
  public static void main(String[] args){
          testAdd();
  }
  public static void testAdd(){
      try{
          System.out.println("测试方法开始");
        /*  ApplicationContext context = new
                  ClassPathXmlApplicationContext(new String[] {"/spring/application*.xml"});
          IMgtReceivableCopyService mgtReceivableCopyService = (IMgtReceivableCopyService)context.getBean("mgtReceivableCopyService");*/
          Map<String,String>map = new HashMap<String,String>();
          map.put("appid",APP_ID);
          map.put("sourceReceivableId","sdfsfdcccccc");
          map.put("sourceBuildCode","A08-2-1-11-02");
          map.put("expenditure",PROPERTY);
          map.put("periodName","2017-2");
          map.put("totalAmount",14+"");
          map.put("discountAmount",12+"");
          map.put("breakContractAmount",0+"");
          map.put("backUrl","http://localhost:8080/admin");
          map.put("description","物业缴费");
          map.put("buildingType",BUILDING_TYPE);
          String sign = CspSignUtil.generateSign(map,"efd5b7d3241c49589d4f2be10402c0ee");
          map.put("sign",sign);
          HttpUtil.get("http://127.0.0.1:8080/api/common/receivable/add",map);
          System.out.println("测试方法结束");
      }catch(Exception e){
          e.printStackTrace();
      }

  }



}
