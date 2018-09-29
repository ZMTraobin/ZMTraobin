package com.cmig.future.csportal.common.utils;

import java.text.DecimalFormat;

/**
 * 金额转换工具
 * @author wangc
 *
 */
public class AmountUtils {
    
    /** 
     * create by wangchao@2017/05/02
     * 格式化数字为千分位显示； 
     * @param 要格式化的数字； 
     * @return 
     */  
    public static String fmtMicrometer(String text)  
    {  
        DecimalFormat df = null;  
        if(text.indexOf(".") > 0)  
        {  
            if(text.length() - text.indexOf(".")-1 == 0)  
            {  
                df = new DecimalFormat("###,##0.");  
            }else if(text.length() - text.indexOf(".")-1 == 1)  
            {  
                df = new DecimalFormat("###,##0.0");  
            }else  
            {  
                df = new DecimalFormat("###,##0.00");  
            }  
        }else   
        {  
            df = new DecimalFormat("###,##0");  
        }  
        double number = 0.0;  
        try {  
             number = Double.parseDouble(text);  
        } catch (Exception e) {  
            number = 0.0;  
        }  
        return df.format(number);  
    }  

    public static void main(String[] args) {
        System.out.println(fmtMicrometer("1000000000"));
    }
}
