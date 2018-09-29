package com.cmig.future.csportal.common.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 正则表达式验证
 * 
 * @author haitao.wang
 * 
 */
public final class RegExpValidatorUtils {

	//private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static boolean match(String regex, String str) {
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 6-20位数字和字母组合的密码
	 * @param str
	 * @return
	 */
	public static boolean IsPassword(String str) {
		if(StringUtils.isBlank(str)){
		       return false;
		}
		String regex = "^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
		return match(regex, str);
	}

	/**
	 * 6-20位数字或字母的密码
	 * @param str
	 * @return
	 */
	public static boolean IsPasswordMgt(String str) {
		if(StringUtils.isBlank(str)){
			return false;
		}
		String regex = "^[0-9]{6,20}$|^[A-Za-z]{6,20}$|^(?![0-9]+$)(?![a-zA-Z]+$)[0-9A-Za-z]{6,20}$";
		return match(regex, str);
	}
	
	/**
	 * 非0正整数
	 * @param str
	 * @return
	 */
	public static boolean IsIntNumber(String str) {
		if(StringUtils.isBlank(str)){
		       return false;
		}
		String regex = "^[0-9]*[1-9][0-9]*$";
		return match(regex, str);
	}

	/**
	 * 含0正整数
	 * @param str
	 * @return
	 */
	public static boolean isIntNumberAndZero(String str) {
		if(StringUtils.isBlank(str)){
			return false;
		}
		String regex = "^\\d+$";
		return match(regex, str);
	}
	
	/**
	* 此方法描述的是：数字验证
	* @author:zhangtao107@126.com
	* @param str
	* @return boolean
	*/
	public static boolean IsNumber(String str) {
		if(StringUtils.isBlank(str)){
		       return false;
		}
		String regex = "\\d+(\\.\\d+)?";
		return match(regex, str);
	}
	
	/**
	* 此方法描述的是：物业电话验证规则，8到20位数字
	* @author:zhangtao107@126.com
	* @param value
	* @return boolean
	*/
	public static boolean isWydh(String value){
	    // 邮箱验证规则
	    String regEx = "[0-9-]{8,20}";
	    // 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx);
	    // 忽略大小写的写法
	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(value);
	    // 字符串是否与正则表达式相匹配
	    return matcher.matches();
	}

	/**
	 * 
	* 此方法描述的是：验证卡号
	* @author:jiubing.gao@zymobi.com
	* @param value
	* @return boolean
	 */
	public static boolean isNum(String value){
	    // 卡号验证
	    String regEx = "[0-9]+";
	    // 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx);
	 
	    Matcher matcher = pattern.matcher(value);
	    // 字符串是否与正则表达式相匹配
	    return matcher.matches();
	}
	/**
	 * 
	* 此方法描述的是：排序验证
	* @author:jiubing.gao@zymobi.com
	* @param integer
	* @return boolean
	 */
/*	public static boolean isSort(Integer integer){
		// 排序验证
		String regEx = "[0-9]+";
		// 编译正则表达式
		Pattern pattern = Pattern.compile(regEx);
		
		Matcher matcher = pattern.matcher(integer);
		// 字符串是否与正则表达式相匹配
		return matcher.matches();
	}*/
	/**
	* 此方法描述的是：时间格式校验HH:mm
	* @author:zhangtao107@126.com
	* @param value
	* @return boolean
	*/
	public static boolean isWyfwsj(String value){
	    // 邮箱验证规则
	    String regEx = "(0\\d{1}|1\\d{1}|2[0-3]):([0-5]\\d{1})";
	    // 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx);
	    // 忽略大小写的写法
	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(value);
	    // 字符串是否与正则表达式相匹配
	    return matcher.matches();
	}
	
	/**
	* 此方法描述的是：4为数字邀请码
	* @author:zhangtao107@126.com
	* @param value
	* @return boolean
	*/
	public static boolean isRegistrationInvitationCode(String value){
	    // 邮箱验证规则
	    String regEx = "[0-9]{4}";
	    // 编译正则表达式
	    Pattern pattern = Pattern.compile(regEx);
	    // 忽略大小写的写法
	    // Pattern pat = Pattern.compile(regEx, Pattern.CASE_INSENSITIVE);
	    Matcher matcher = pattern.matcher(value);
	    // 字符串是否与正则表达式相匹配
	    return matcher.matches();
	}
	
	
	 /** 
     * 验证Email 
     * @param email email地址，格式：zhangsan@zuidaima.com，zhangsan@xxx.com.cn，xxx代表邮件服务商 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkEmail(String email) {   
        String regex = "(\\w|\\d|\\-|\\_|\\.)+@(\\w|\\d|\\-|\\_|\\.)+\\.[a-z]+(\\.[a-z]+)?";
        return Pattern.matches(regex, email);   
    }   
       
    /** 
     * 验证身份证号码 
     * @param idCard 居民身份证号码15位或18位，最后一位可能是数字或字母 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkIdCard(String idCard) {   
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";   
        return Pattern.matches(regex,idCard);   
    }   
       
    /** 
     * 验证手机号码，11位数字即可
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkMobile(String mobile) {   
        String regex = "[0-9]{11}";   
        return Pattern.matches(regex,mobile);   
    }   
       
    /** 
     * 验证固定电话号码 
     * @param phone 电话号码，格式：国家（地区）电话代码 + 区号（城市代码） + 电话号码，如：+8602085588447 
     * <p><b>国家（地区） 代码 ：</b>标识电话号码的国家（地区）的标准国家（地区）代码。它包含从 0 到 9 的一位或多位数字， 
     *  数字之后是空格分隔的国家（地区）代码。</p> 
     * <p><b>区号（城市代码）：</b>这可能包含一个或多个从 0 到 9 的数字，地区或城市代码放在圆括号—— 
     * 对不使用地区或城市代码的国家（地区），则省略该组件。</p> 
     * <p><b>电话号码：</b>这包含从 0 到 9 的一个或多个数字 </p> 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkPhone(String phone) {   
        String regex = "(\\+\\d+)?(\\d{3,4}\\-?)?\\d{7,8}$";   
        return Pattern.matches(regex, phone);   
    }   
       
    /** 
     * 验证整数（正整数和负整数） 
     * @param digit 一位或多位0-9之间的整数 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkDigit(String digit) {   
        String regex = "\\-?[1-9]\\d+";   
        return Pattern.matches(regex,digit);   
    }   
       
    /** 
     * 验证整数和浮点数（正负整数和正负浮点数） 
     * @param decimals 一位或多位0-9之间的浮点数，如：1.23，233.30 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkDecimals(String decimals) {   
        String regex = "\\-?[1-9]\\d+(\\.\\d+)?";   
        return Pattern.matches(regex,decimals);   
    }    
       
    /** 
     * 验证空白字符 
     * @param blankSpace 空白字符，包括：空格、\t、\n、\r、\f、\x0B 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkBlankSpace(String blankSpace) {   
        String regex = "\\s+";   
        return Pattern.matches(regex,blankSpace);   
    }   
       
    /** 
     * 验证中文 
     * @param chinese 中文字符 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkChinese(String chinese) {   
        String regex = "^[\u4E00-\u9FA5]+$";   
        return Pattern.matches(regex,chinese);   
    }   
       
    /** 
     * 验证日期（年月日） 
     * @param birthday 日期，格式：1992-09-03，或1992.09.03 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkBirthday(String birthday) {   
        String regex = "((?:19|20)\\d\\d)([-./])(0[1-9]|1[012])([-./])(0[1-9]|[12][0-9]|3[01])";
        return Pattern.matches(regex,birthday);   
    }

	/**
	 * 验证日期（年月）
	 * @param period 日期，格式：1992-09
	 * @return 验证成功返回true，验证失败返回false
	 */
	public static boolean checkPeriod(String period) {
		String regex = "((?:19|20)\\d\\d)-(0[1-9]|1[012])";
		return Pattern.matches(regex,period);
	}

	/**
     * 验证URL地址 
     * @param url 格式：http://blog.csdn.net:80/xyang81/article/details/7705960? 或 http://www.csdn.net:80 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkURL(String url) {   
        String regex = "(https?://(w{3}\\.)?)?\\w+\\.(\\w*-{0,1}\\w*)(\\.[a-zA-Z]+)*(:\\d{1,5})?(/\\w*)*(\\??(.+=.*)?(&.+=.*)?)?";   
        return Pattern.matches(regex, url);   
    }   
      
    /** 
     * <pre> 
     * 获取网址 URL 的一级域名 
     * http://www.zuidaima.com/share/1550463379442688.htm ->> zuidaima.com 
     * </pre> 
     *  
     * @param url 
     * @return 
     */  
    public static String getDomain(String url) {  
        Pattern p = Pattern.compile("(?<=http://|\\.)[^.]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);  
        // 获取完整的域名  
        // Pattern p=Pattern.compile("[^//]*?\\.(com|cn|net|org|biz|info|cc|tv)", Pattern.CASE_INSENSITIVE);  
        Matcher matcher = p.matcher(url);  
        matcher.find();  
        return matcher.group();  
    }  
    /** 
     * 匹配中国邮政编码 
     * @param postcode 邮政编码 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkPostcode(String postcode) {   
        String regex = "[1-9]\\d{5}";   
        return Pattern.matches(regex, postcode);   
    }   
       
    /** 
     * 匹配IP地址(简单匹配，格式，如：192.168.1.1，127.0.0.1，没有匹配IP段的大小) 
     * @param ipAddress IPv4标准地址 
     * @return 验证成功返回true，验证失败返回false 
     */   
    public static boolean checkIpAddress(String ipAddress) {   
        String regex = "[1-9](\\d{1,2})?\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))\\.(0|([1-9](\\d{1,2})?))";   
        return Pattern.matches(regex, ipAddress);   
    }

    /**
     * 该方法主要使用正则表达式来判断字符串中是否包含字母
     * @author fenggaopan 2015年7月21日 上午9:49:40
     * @param cardNum 待检验的原始卡号
     * @return 返回是否包含
     */
    public static boolean judgeContainsStr(String cardNum) {
        String regex=".*[a-zA-Z]+.*";
        Matcher m=Pattern.compile(regex).matcher(cardNum);
        return m.matches();
    }

	//金额验证
	public static boolean isNumber(String str)
	{
		String regex = "^(([1-9]{1}\\d*)|([0]{1}))(\\.(\\d){0,2})?$";
		Matcher m=Pattern.compile(regex).matcher(str);
		return m.matches();
	}
	//验证参数是否是true或者false
	public static boolean isBoolean(String str)
	{
	    String regex="true||false||TRUE||FALSE";
		Matcher m=Pattern.compile(regex).matcher(str);
		return m.matches();
	}


    public static void main(String[] args) {
		System.out.println(isBoolean("1"));
		/*System.out.println(isBoolean("abc"));
		System.out.println(IsPassword("11111111111aa"));*/
	}

}
