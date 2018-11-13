package com.cmig.future.csportal.common.utils.cnnumber;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @description:
 * @author: zhangtao
 * @create: 2018-09-26 10:46
 **/
public class RexUtils {
    public static String and(String... rules) {
        StringBuffer sb = new StringBuffer();
        for (String rule : rules) {
            sb.append("(").append(rule).append(")");
        }
        return sb.toString();
    }

    public static String or(String... rules) {
        StringBuffer sb = new StringBuffer();
        for (String rule : rules) {
            if (sb.length() != 0) {
                sb.append("|");
            }
            sb.append("(").append(rule).append(")");
        }
        return sb.toString();
    }

    public static String more(String rule) {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(rule).append("]+");
        return sb.toString();
    }

    public static String nullOrMore(String rule) {
        StringBuffer sb = new StringBuffer();
        sb.append("[").append(rule).append("]*");
        return sb.toString();
    }

    public static String getFind(String data, String rule) {
        Matcher m = Pattern.compile(rule).matcher(data);
        StringBuffer sb = new StringBuffer();
        while (m.find()) {
            sb.append(String.valueOf(m.group()));
            return sb.toString();
        }
        return "";
    }
}
