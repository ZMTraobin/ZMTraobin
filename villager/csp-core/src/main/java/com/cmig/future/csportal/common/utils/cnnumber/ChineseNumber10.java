package com.cmig.future.csportal.common.utils.cnnumber;

import org.apache.http.util.TextUtils;

/**
 * @description: 处理十位数
 * @author: zhangtao
 * @create: 2018-09-26 10:47
 **/
public class ChineseNumber10 {
    public final static String number10 = "十|拾";
    private final long unit = 10;

    private final String rex = RexUtils.and(RexUtils.nullOrMore(RexUtils.or("", ChineseNumber1.number0, ChineseNumber1.number1, ChineseNumber1.number2, ChineseNumber1.number3,
            ChineseNumber1.number4, ChineseNumber1.number5, ChineseNumber1.number6, ChineseNumber1.number7, ChineseNumber1.number8, ChineseNumber1.number9)),
            number10);

    private ChineseNumber1 number = new ChineseNumber1();

    private String mData = "";

    public ChineseNumber10(String data) {
        mData = RexUtils.getFind(data, rex);
        if (!TextUtils.isEmpty(mData)) {
            String numberData = String.valueOf(mData.subSequence(0, mData.length() - 1));
            if (TextUtils.isEmpty(numberData)) {
                numberData = "一";
            }
            number = new ChineseNumber1(numberData);
        }
    }

    public ChineseNumber10() {
        number = new ChineseNumber1();
    }

    public long getNumber() {
        return number.getNumber() * unit;
    }

    public String getData() {
        return mData;
    }
}
