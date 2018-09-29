package com.cmig.future.csportal.module.pay.conf;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.cmig.future.csportal.common.utils.StringUtils;
import com.cmig.future.csportal.module.sys.openinfo.OpenAppUtils;
import com.cmig.future.csportal.module.sys.openinfo.dto.OpenAppInfo;
import com.pingplusplus.Pingpp;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:17 2017/4/17.
 * @Modified by zhangtao on 11:17 2017/4/17.
 */
public class PingppConfig { 
    /**
     * Pingpp 管理平台对应的 API Key，api_key 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击管理平台右上角公司名称->开发信息-> Secret Key
     */
    //public final static String apiKey = "sk_test_OaLCGSXPGu9Sn94K88inP48O";

    public final static String apiKey = "sk_live_bj5uTGrbHyH4HOuv1Kzz5Ky9";

    /**
     * Pingpp 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
    public final static String appId = "app_O0mTiDWrjj50enbr";

    /**
     * 设置请求签名密钥，密钥对需要你自己用 openssl 工具生成，如何生成可以参考帮助中心：https://help.pingxx.com/article/123161；
     * 生成密钥后，需要在代码中设置请求签名的私钥(rsa_private_key.pem)；
     * 然后登录 [Dashboard](https://dashboard.pingxx.com)->点击右上角公司名称->开发信息->商户公钥（用于商户身份验证）
     * 将你的公钥复制粘贴进去并且保存->先启用 Test 模式进行测试->测试通过后启用 Live 模式
     */

    // 你生成的私钥路径
    public static String privateKeyFilePath = "res/csp_rsa_private_key.pem";

    // Ping++ 公钥,用户回调验签
    public static String pubKeyPath = "res/pingpp_public_key.pem";


    public static String currency="cny";

    static {
        String path=getClassPath();
        privateKeyFilePath=path+ File.separator+privateKeyFilePath;
        pubKeyPath=path+ File.separator+pubKeyPath;
    }
    public static String getClassPath() {
        String path = new Object() {
            public String getPath() {
                return this.getClass().getClassLoader().getResource("").getFile();
            }
        }.getPath();
        return path;
    }

    public static void initPingxxAppid(String appId)throws DataWarnningException {
        OpenAppInfo openAppInfo= OpenAppUtils.getOpenAppInfo(appId);
        if(StringUtils.isEmpty(openAppInfo.getPaymentConfigId())){
            throw new DataWarnningException("收款帐号配置错误");
        }

        Pingpp.appId=openAppInfo.getPaymentConfigId();
	    Pingpp.apiKey=apiKey;
    }


    /**
     * 读取文件, 部署 web 程序的时候, 签名和验签内容需要从 request 中获得
     * @param filePath
     * @return
     * @throws Exception
     */
    public static String getStringFromFile(String filePath) throws Exception {
        FileInputStream in = new FileInputStream(filePath);
        InputStreamReader inReader = new InputStreamReader(in, "UTF-8");
        BufferedReader bf = new BufferedReader(inReader);
        StringBuilder sb = new StringBuilder();
        String line;
        do {
            line = bf.readLine();
            if (line != null) {
                if (sb.length() != 0) {
                    sb.append("\n");
                }
                sb.append(line);
            }
        } while (line != null);

        return sb.toString();
    }

    /**
     * 获得公钥
     * @return
     * @throws Exception
     */
    public static PublicKey getPubKey() throws Exception {
        String pubKeyString = getStringFromFile(PingppConfig.pubKeyPath);
        pubKeyString = pubKeyString.replaceAll("(-+BEGIN PUBLIC KEY-+\\r?\\n|-+END PUBLIC KEY-+\\r?\\n?)", "");
        byte[] keyBytes = Base64.decodeBase64(pubKeyString);

        // generate public key
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PublicKey publicKey = keyFactory.generatePublic(spec);
        return publicKey;
    }

    /**
     * 验证签名
     * @param dataString
     * @param signatureString
     * @param publicKey
     * @return
     */
    public static boolean verifyData(String dataString, String signatureString, PublicKey publicKey){
	    try {
		    String signKey = new String(signatureString.getBytes(),"UTF-8");
		    byte[] signatureBytes = Base64.decodeBase64(signKey);
		    Signature signature = Signature.getInstance("SHA256withRSA");
		    signature.initVerify(publicKey);
		    signature.update(dataString.getBytes("UTF-8"));
		    return signature.verify(signatureBytes);
	    } catch (Exception e) {
		    e.printStackTrace();
	    }
		return false;
    }

}
