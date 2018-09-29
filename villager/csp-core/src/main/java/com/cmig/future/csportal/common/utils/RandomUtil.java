package com.cmig.future.csportal.common.utils;

/**
 * Created by zhangtao107@126.com on 2016/12/15.
 */
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 *  @version:
 *        @function:        产生随机数字、随机字母、随机数字+字母。
 */
public class RandomUtil {

    /**
     * 随机产生几位数字：例：maxLength=3,则结果可能是 012
     */
    public static final int produceNumber(int maxLength){
        Random random = new Random();
        return random.nextInt(maxLength);
    }


    /**
     * 随机产生区间数字：例：minNumber=1,maxNumber=2,则结果可能是 1、2,包括首尾。
     */
    public static int produceRegionNumber(int minNumber,int maxNumber){
        return minNumber + produceNumber(maxNumber);
    }

    /**
     * 随机产生几位字符串：例：maxLength=3,则结果可能是 aAz
     * @param maxLength 传入数必须是正数。
     */
    public static String produceString(int maxLength){
        String source = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return doProduce(maxLength, source);
    }

    /**
     * 随机产生随机数字+字母：例：maxLength=3,则结果可能是 1Az
     * @param maxLength 传入数必须是正数。
     */
    public static String produceStringAndNumber(int maxLength){
        String source = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
        return doProduce(maxLength, source);
    }

    /**
     * 自定义随机产生结果
     */
    public static String produceResultByCustom(String customString,int maxLength){
        return doProduce(maxLength, customString);
    }

    /**
     * 生产结果
     */
    private static String doProduce(int maxLength, String source) {
        StringBuffer sb = new StringBuffer(100);
        for (int i = 0; i < maxLength; i++) {
            final int number =  produceNumber(source.length());
            sb.append(source.charAt(number));
        }
        return sb.toString();
    }


    //生成随机数字和字母,
    public static String getStringRandom(int length,int stringNum) {
        String val = "";
        Random random = new Random();
        //参数length，表示生成几位随机数
        int charNum=0;
        int numNum=0;
        for(int i = 0; i < length; i++) {
            String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";

            if(charNum>=stringNum){
                charOrNum="num";
            }else if(numNum>=(length-stringNum)){
                charOrNum="char";
            }
            //输出字母还是数字
            if( "char".equalsIgnoreCase(charOrNum) ) {
                //输出是大写字母还是小写字母
                int temp = random.nextInt(2) % 2 == 0 ? 65 : 97;
                val += (char)(random.nextInt(26) + temp);
                charNum++;
            } else if( "num".equalsIgnoreCase(charOrNum) ) {
                val += String.valueOf(random.nextInt(10));
                numNum++;
            }
        }
        return val;
    }


    public static void main(String[] args) {
        //for(int i=0;i<1000;i++) {
        //    String result=RandomUtil.getStringRandom(4, 1).toUpperCase();
        //    System.out.println(result+"   "+RegExpValidatorUtils.judgeContainsStr(result));
        //}


        Map map=new HashMap<>();
        for(int i=0;i<5;i++){
            map.put(i,RandomUtil.getStringRandom(4, 1).toUpperCase());
            System.out.print(i+" "+map.get(i).toString()+" ");
        }
        System.out.println();

        for(int i=0;i<2000;i++) {
            String result=map.get(new Random().nextInt(5)).toString();
            System.out.println(result+"   "+RegExpValidatorUtils.judgeContainsStr(result));
        }
    }
}
