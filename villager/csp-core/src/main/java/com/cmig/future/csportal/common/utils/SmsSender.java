package com.cmig.future.csportal.common.utils;

import com.cmig.future.csportal.common.config.Global;
import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * 调用核心发短信接口
 */
public class SmsSender {

    protected static Logger logger = LoggerFactory.getLogger(SmsSender.class);
    public static final String RESULT_OK="ok";
    public static final String RESULT_FAIL="fail";
    private static String serviceUrl;
    private static String userCode;
    private static String userKey;

    static{
        serviceUrl = Global.getHspServerUrl("CORE_SMS_SERVER_URL");
        userCode =Global.getConfig("CORE_SMS_USERCODE");
        userKey =Global.getConfig("CORE_SMS_USERKEY");
    }

    public static void main(String[] args) {
        sendMsg("18210225831", "外网域名测试2");
    }

    /**
     * 发短信
     * @param phoneNo
     * @param smsContent
     * @return
     */
    public static boolean sendMsg(String phoneNo, String smsContent) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            Calendar cal = Calendar.getInstance();
            String reqTime = sdf.format(cal.getTime());
            String token = encrypt(userCode + reqTime + userKey);
            String authorization = Base64.encodeBase64String(new String(userCode + ":" + reqTime).getBytes("UTF-8"));

            String postParam =
                    "{" +
                            "	\"SmsMtSingleReq\":{\n" +
                            "		\"token\":\"" + token + "\",\n" +
                            "		\"phoneNo\":\"" + phoneNo + "\",\n" +
                            "		\"smsContent\":\"" + smsContent + "\"\n" +
                            "	}\n" +
                            "}";

            //logger.debug("短信服务器:{} ",serviceUrl);
            //logger.debug("params:{} ",postParam);

            URL restUrl = new URL(serviceUrl);
            // 创建HttpsURLConnection对象，并设置其SSLSocketFactory对象
            HttpURLConnection conn = (HttpURLConnection) restUrl.openConnection();

            //设置超时间为3秒
            conn.setConnectTimeout(30 * 1000);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");

            //防止屏蔽程序抓取而返回403错误
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept", "application/json");
	        conn.setRequestProperty("Authorization", "Basic "+authorization);

            conn.setRequestProperty("Content-Length", Integer.toString(postParam.getBytes().length));
            conn.connect();

            DataOutputStream outputStream = new DataOutputStream(conn.getOutputStream());
            outputStream.write(postParam.getBytes("UTF-8"));
            outputStream.flush();
            outputStream.close();

            if (HttpURLConnection.HTTP_OK == conn.getResponseCode()) {
                //得到输入流
                InputStream inputStream = conn.getInputStream();
                //获取自己数组
                byte[] data = readInputStream(inputStream);
                if (inputStream != null) {
                    inputStream.close();
                }
                String returnString = new String(data, "UTF-8");
                logger.debug("returnString:{}",returnString);

                JSONObject object = JSONObject.fromObject(returnString);
                String status =JSONObject.fromObject(object.getString("Resp")).getString("code").toString() ;
                if (status.equals("0000")) {
                    logger.debug("ok");
                    return true;
                } else {
                    logger.debug("fail");
                    return false;
                }
            }
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return false;
    }


    /**
     * 从输入流中获取字节数组
     *
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] readInputStream(InputStream inputStream) throws IOException {
        byte[] buffer = new byte[1024];
        int len = 0;
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        while ((len = inputStream.read(buffer)) != -1) {
            bos.write(buffer, 0, len);
        }
        bos.close();
        return bos.toByteArray();
    }


    private static MessageDigest alg;


    public static String encrypt(String key) {
        return Hex.encodeHexString(computeDigest(key.getBytes()));

    }

    public static byte[] computeDigest(byte[] b) {
        try {
            if (alg == null) {
                alg = MessageDigest.getInstance("MD5");
            }

            alg.reset();
            alg.update(b);
            byte[] hash = alg.digest(); // 得到摘要
            return hash;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
