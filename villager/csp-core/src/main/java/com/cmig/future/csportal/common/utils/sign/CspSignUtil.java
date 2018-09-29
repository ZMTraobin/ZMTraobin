package com.cmig.future.csportal.common.utils.sign;


import com.cmig.future.csportal.common.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangtao107@126.com on 2016/8/19.
 */
public class CspSignUtil {

    private static Log log = LogFactory.getLog(CspSignUtil.class);
    // 字符编码格式
    public static String input_charset = "utf-8";


    /**
     * 比较签名
     *
     * @param paramsMap 请求接收方接收到的参数
     * @param secret    中民居家平台分配的用于计算签名的秘钥
     * @param sign      调用方使用签名生成规则生成的签名
     * @return
     */
    public static boolean checkSign(Map<String, String> paramsMap, String secret, String sign) {
        boolean checkFlag = false;

        //计算签名
        String newSign = generateSign(paramsMap, secret);
        if (StringUtils.isNotEmpty(newSign) && newSign.equals(sign)) {
            checkFlag = true;
        }
        return checkFlag;
    }

    /**
     * 根据请求接收方接收到的参数组paramsMap生成签名
     *
     * @param paramsMap 请求接收方接收到的参数
     * @param secret    中民居家平台分配的用于计算签名的秘钥
     * @return
     */
    public static String generateSign(Map<String, String> paramsMap, String secret) {
        //返回的签名变量
        String sign = null;
        try {

            //过滤空值、sign与sign_type参数
            Map<String, String> sParaNew = paraFilter(paramsMap);

            sParaNew.put("signedSecKey", secret);
            //获取待签名字符串
            String preSignStr = createLinkString(sParaNew);
            //log.debug("****************preSignStr " + preSignStr + "*****************");

            //计算签名
            sign = generateSign(preSignStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 根据请求接收方接收到的参数生成签名
     *
     * @param params 请求接收方接收到的参数
     * @return
     */
    protected static String generateSign(String params) {
        //返回的签名变量
        String sign = null;
        try {
            String toSign = (params == null ? "" : params);
            sign = getMd5Str(getContentBytes(toSign, input_charset));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 根据编码格式返回对应的字节数组
     *
     * @param content 字符串
     * @param charset 编码格式
     * @return
     * @throws UnsupportedEncodingException
     */
    protected static byte[] getContentBytes(String content, String charset) throws UnsupportedEncodingException {
        if (StringUtils.isEmpty(charset)) {
            return content.getBytes();
        }
        return content.getBytes(charset);
    }

    /**
     * 获取MD5字符串
     *
     * @param signbyte
     * @return
     * @throws NoSuchAlgorithmException
     */
    protected static String getMd5Str(byte[] signbyte) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        return Base64.getEncoder().encodeToString(md.digest(signbyte));
    }

    /**
     * 除去数组中的空值和签名参数
     *
     * @param sArray 签名参数组
     * @return 去掉空值与签名参数后的新签名参数组
     */
    protected static Map<String, String> paraFilter(Map<String, String> sArray) {
        Map<String, String> result = new HashMap<String, String>();
        if (sArray == null || sArray.size() <= 0) {
            return result;
        }

        for (String key : sArray.keySet()) {
            String value = sArray.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase("sign")
                    || key.equalsIgnoreCase("sign_type")) {
                continue;
            }
            result.put(key, value);
        }
        return result;
    }

    /**
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param paramsMap 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
    protected static String createLinkString(Map<String, String> paramsMap) {
        List<String> keys = new ArrayList<String>(paramsMap.keySet());
        Collections.sort(keys);
        String prestr = "";
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = paramsMap.get(key);

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                prestr = prestr + key + "=" + value;
            } else {
                prestr = prestr + key + "=" + value + "&";
            }
        }
        return prestr;
    }

    public static void main(String args[]){
        String appid="65a17951724c4194a67434704c9dbf92";
        String secret="77851c52ab3e4ebe860ec7a50224f604";//中民居家平台分配的秘钥
        //商户提交订单
        Map<String, String> map = new HashMap();
        map.put("appid", appid);
        map.put("openid", "72f6d91a22c7410887802a0e16ed5992");
        map.put("tradeNo", "10000026");
        map.put("orderAmount", "100");
        map.put("subject", "iphone7 玫瑰金");
        map.put("body", "iphone7 玫瑰金 64G 港版 保修三年");
        map.put("backUrl", "http://csportal.cm-dev.cn/user/pingpp/notify");
        map.put("frontUrl", "http://csportal.cm-dev.cn/user/pingpp/notify");
        map.put("orderType", "物业费");
        map.put("description", "订单描述");
        map.put("timeExpire", "");
	    map.put("clientIp", "127.0.0.1");
	    map.put("integralAmount", "100");


        String sign=generateSign(map,secret);
	    try {
		    log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }
        log.info(checkSign(map,secret,sign));

        //商户接收通知
        map = new HashMap();
        map.put("tradeNo", "15113314626455592013");
        map.put("paidAmount", "6000");
        map.put("channel","alipay");
        map.put("timePaid","1511331690141");
        map.put("transeq","123456789456787845675678");
        map.put("tradeStatus", "Y");
        sign = CspSignUtil.generateSign(map, secret);
        try {
            log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //商户查询订单支付状态
        map = new HashMap();
        map.put("appid", appid);
        map.put("tradeNo", "10000025");
        sign=generateSign(map,secret);
        try {
            log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //商户提交退款
        map = new HashMap();
        map.put("appid", appid);
        map.put("tradeNo", "1494902197172");
        map.put("amount", "");
        map.put("description", "测试退款");
        map.put("backUrl", "http://csportal.cm-dev.cn/user/order/refundNotifyDemo");
        sign=generateSign(map,secret);
        try {
            log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        //商户查询订单支付状态
        map = new HashMap();
        map.put("appid", appid);
        map.put("refundOrderNo", "1493782028637");
        sign=generateSign(map,secret);
        try {
            log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }


	    map = new HashMap();
	    map.put("token","6266ae9c3f5aca959a1151803912e6d4");
	    map.put("appid", appid);
	    map.put("origiOrderId", "10000023");
	    map.put("orderAmount", "1001");
	    map.put("cardId", "4");
	    map.put("merchantId","300010000001");
	    sign=generateSign(map,secret);
	    try {
		    log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }

	    map = new HashMap();
	    map.put("appid", appid);
	    map.put("origOrderNo", "10000023");
	    map.put("merchantId","300010000001");
	    sign=generateSign(map,secret);
	    try {
		    log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }

	    map = new HashMap();
	    map.put("appid", appid);
	    map.put("origiOrderId", "1497597594898o4ue2gc");
	    map.put("merchantId","300010000001");
	    map.put("refundAmt", "1001");
	    map.put("openid","a1d3ee52c89e4867ba687c133f11c3ac");
	    sign=generateSign(map,secret);
	    try {
		    log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }

	    map = new HashMap();
	    map.put("appid", appid);
	    map.put("integralCode", "R00002");
	    map.put("openid","33ed0a256c254f73aeff11be0c9bfcc2");
	    map.put("orderAmount", "12345");
	    sign=generateSign(map,secret);
	    try {
		    log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }

	    map = new HashMap();
	    map.put("appid", appid);
	    map.put("tradeNo", "1500370813973");
	    sign=generateSign(map,secret);
	    try {
		    log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }

	    map = new HashMap();
	    map.put("appid", appid);
	    map.put("mobile", "18210225831");
	    map.put("content", "张涛测试push消息prd");
	    sign=generateSign(map,secret);
	    try {
		    log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }


	    map = new HashMap();
	    map.put("openid","d06bfa07f3144720a64aa1c71a8e37ac");
	    map.put("appid", "934dfcc043f941cfa5587e6c11762bad");
	    map.put("origiOrderId", "54654564000000001");
	    map.put("orderAmount", "1");
	    map.put("cardId", "6227001217450011115");
	    map.put("merchantId","300010000001");
	    sign=generateSign(map,"64d7285467ee44e2989e1a28c4327c6e");
	    try {
		    log.info(URLEncoder.encode(sign, CspSignUtil.input_charset));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }

	    map = new HashMap();
	    map.put("appid", "146c74602a3c422b8e5aaf41d5eb73db");
	    map.put("sourceSystemCommunityId", "002Z00044otymyqTWs8eH2");
	    map.put("contentType", "MGT_COMMUNITY_NOTICE");
	    map.put("content", "xxx");
	    map.put("title", "test_title");
	    sign=generateSign(map,"f0ff6faaa34a4893b583b0568f61ca35");
	    log.info(sign);


	    String authorization = null;
	    try {
		    authorization = org.apache.commons.codec.binary.Base64.encodeBase64String(new String("public@cm-inv.com" + ":" + "ljlife@2015").getBytes("UTF-8"));
	    } catch (UnsupportedEncodingException e) {
		    e.printStackTrace();
	    }
	    log.info("Basic "+authorization);


	    map = new HashMap();
	    map.put("appid", "146c74602a3c422b8e5aaf41d5eb73db");
	    map.put("startDate","2016-12-31");
	    map.put("endDate","2016-12-31");
	    sign=generateSign(map,"efd5b7d3241c49589d4f2be10402c0ee");
	    log.info(sign);
    }
}
