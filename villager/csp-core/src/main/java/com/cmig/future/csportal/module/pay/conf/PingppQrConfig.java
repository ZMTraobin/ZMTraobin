package com.cmig.future.csportal.module.pay.conf;

import com.cmig.future.csportal.common.exception.DataWarnningException;
import com.pingplusplus.Pingpp;
import org.apache.commons.codec.binary.Base64;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.X509EncodedKeySpec;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 11:17 2017/4/17.
 * @Modified by zhangtao on 11:17 2017/4/17.
 */
public class PingppQrConfig {
    /**
     * Pingpp 管理平台对应的 API Key，api_key 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击管理平台右上角公司名称->开发信息-> Secret Key
     */

    //public final static String apiKey = "sk_test_ebHCyP4CWPOOGKi5yHTG0eLG";

	public final static String apiKey ="sk_live_nzfXDS8Gev5CybvnnD9GWPyD";

    /**
     * Pingpp 管理平台对应的应用 ID，app_id 获取方式：登录 [Dashboard](https://dashboard.pingxx.com)->点击你创建的应用->应用首页->应用 ID(App ID)
     */
    public final static String appId = "app_uXjnDKrX5GqHfrrz";

    /**
     * 设置请求签名密钥，密钥对需要你自己用 openssl 工具生成，如何生成可以参考帮助中心：https://help.pingxx.com/article/123161；
     * 生成密钥后，需要在代码中设置请求签名的私钥(rsa_private_key.pem)；
     * 然后登录 [Dashboard](https://dashboard.pingxx.com)->点击右上角公司名称->开发信息->商户公钥（用于商户身份验证）
     * 将你的公钥复制粘贴进去并且保存->先启用 Test 模式进行测试->测试通过后启用 Live 模式
     */

    // 你生成的私钥路径
    public static String privateKeyFilePath = "res/qr/csp_rsa_private_key.pem";

	// Ping++ 公钥,用户回调验签
	public static String pubKeyPath = "res/qr/pingpp_public_key.pem";


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

    public static void initPingxxAppid()throws DataWarnningException {
	    Pingpp.apiKey = apiKey;
	    Pingpp.appId = appId;
	    Pingpp.privateKey = getPKCS8PrivateKey();
	    Pingpp.DEBUG = true;
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
        String pubKeyString = getStringFromFile(PingppQrConfig.pubKeyPath);
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
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeyException
     * @throws SignatureException
     */
    public static boolean verifyData(String dataString, String signatureString, PublicKey publicKey)
            throws NoSuchAlgorithmException, InvalidKeyException, SignatureException, UnsupportedEncodingException {
        String signKey=new String(signatureString.getBytes(),"UTF-8");
        byte[] signatureBytes = Base64.decodeBase64(signKey);
        Signature signature = Signature.getInstance("SHA256withRSA");
        signature.initVerify(publicKey);
        signature.update(dataString.getBytes("UTF-8"));
        return signature.verify(signatureBytes);
    }

	/**
	 * 扫码商户私钥
	 * @return
	 */
	public static String getPKCS8PrivateKey() {
		return "-----BEGIN PRIVATE KEY-----\n" +
				"MIICXAIBAAKBgQDE8eoavyVRuOF/wQka9SYJE7hScn+jzbTrsAHyrXmT1PQuULqu\n" +
				"j53AYVCh7O2osux4S7MMHYfFJ9SZBi1Bjo73EkTYVLjsNQCx+CFNS7oAmA5c07cm\n" +
				"N9pe48VCpnx3SPKfvJM5U4x0y0BQHWc0iYmo9T4x0jSFlCxfPMz/cvU10QIDAQAB\n" +
				"AoGBALGxv9LQIiDhGfzFs6xyAhZwh39ENawavyFkFe3a3XYnzwU266jyhZf9dd4T\n" +
				"wr7SaQ+E5ZCJ2GVnsac77ntW39+uN9gGkaNaUl9Wg2guWMbJfE6EWSmmXyHePHrD\n" +
				"rE7rCKKlxPdy9CPwXu9o+w/cISxT7X4jjKyHr8onTVZm4SABAkEA/3eM9km9RPHH\n" +
				"j71IzllaBmVU83erNy3jgYhcL1rt4uEmgbGmK0/EXMpqtftW668VFwc0G6Hn0WD/\n" +
				"BfVlNRIX0QJBAMVbGzCa5Jx8O9XHFWEzwkNJCAL8/FX/eHtBfUhbLgIv8e5cd6ct\n" +
				"DU2MmI/2v415xVov5jaSRQHb5tghedBWvgECQEqyz1H4r9IoDF04UMCdmdhC6vsh\n" +
				"47uHvH1UvPfP071Cbnr/0gHaGiWzvk4mBv11PWp1XcQyrFEp1ogMgCJrfvECQCh0\n" +
				"ZnxuI9KX/MPzjURhkb6tJ8FFDoyqU44gfIfqeOHl5hfQSI6yQ3nUCinokJd1kKjO\n" +
				"DMi+7vpUk1tWcVvtjAECQGpC5302nVy+pSp/77JIIYdGUlJgoCSJbtcTvBXrG5c8\n" +
				"OGydi89c7p7l2+udAd8DRIy2ubgSFQw1uDi3DrDSzyo=\n" +
				"-----END PRIVATE KEY-----\n";
	}

}
