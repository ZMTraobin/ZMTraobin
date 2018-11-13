package com.cmig.future.csportal.common.utils.cnnumber;

import org.apache.http.util.TextUtils;

/**
 * @description: 处理个位数
 * @author: zhangtao
 * @create: 2018-09-26 10:47
 **/
public class ChineseNumber1 {
    public final static String number0 = "零";
    public final static String number1 = "一|壹";
    public final static String number2 = "二|两|貳|贰";
    public final static String number3 = "叁|參|三";
    public final static String number4 = "肆|四";
    public final static String number5 = "五|伍";
    public final static String number6 = "陸|陆|六";
    public final static String number7 = "柒|七";
    public final static String number8 = "捌|八";
    public final static String number9 = "九|玖";

    private long number = 0;

    public ChineseNumber1() {

    }

    public ChineseNumber1(String data) {
        if (TextUtils.isEmpty(data)) {
            return;
        }
        if (!TextUtils.isEmpty(RexUtils.getFind(data, number0))) {
            number = 0;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number1))) {
            number = 1;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number2))) {
            number = 2;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number3))) {
            number = 3;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number4))) {
            number = 4;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number5))) {
            number = 5;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number6))) {
            number = 6;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number7))) {
            number = 7;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number8))) {
            number = 8;
        } else if (!TextUtils.isEmpty(RexUtils.getFind(data, number9))) {
            number = 9;
        }
    }

    public long getNumber() {
        return number;
    }
}
