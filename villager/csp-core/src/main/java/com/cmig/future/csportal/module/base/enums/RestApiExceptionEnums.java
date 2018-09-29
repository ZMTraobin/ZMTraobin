package com.cmig.future.csportal.module.base.enums;

/**
 * @Author:zhangtao
 * @Description:
 * @Date Created in 9:23 2017/7/5.
 * @Modified by zhangtao on 9:23 2017/7/5.
 */
public enum RestApiExceptionEnums implements ExceptionEnum {

	SUCCESS(1,"处理成功"),
	UNKNOW_EXCEPTION(0,"响应异常，请稍后再试！"),
	UNLOGIN_EXCEPTION(-1,"未登录或登录超时，请登录后再操作"),
	SIGN_ERROR(10000,"验签失败，签名不正确"),
	ARGS_NULL(10001, "缺少参数"),
	ARGS_ERROR(10002, "参数错误"),
	INTEGRAL_SYSTEM_UNABLE(10003,"积分系统暂未开通，尽请期待"),
	GET_USER_BY_TOKEN_ERROR(10004,"系统繁忙，用户未获取"),
	INTEGRAL_DEDUCTION_FAIL(10005,"积分扣减失败"),
	INTEGRAL_DJ_FAIL(10006,"积分冻结失败"),
	OWNER_BUILD_REPEAT(10008,"数据重复"),
	INTEGRAL_JD_FAIL(10007,"积分解冻失败"),
	NULL_DEFAULT_COMMUNITY(10009,"未配置默认小区，请联系系统管理员"),
	UUID_LOGIN_FAIL(10010,"登录失败，该UUID未绑定帐号"),
	MOBILE_WITH_OUT_REGISTER(10011,"绑定失败，该手机号未注册"),
	GET_WX_CODE_TO_ACCESS_TOKEN_ERROR(10012,"通过code换取网页授权access_token失败"),
	GET_WX_USER_INFO_ERROR(10013,"获取微信用户信息失败"),
	GET_WX_API_ACCESS_TOKEN_ERROR(10014,"获取公众号全局唯一接口调用凭据失败"),
	GET_OWNER_ID(10015, "获取业主id失败"),
	MOBILE_FORMAT_ERROR(10016, "手机号码错误"),
	ID_FORMAT_ERROR(10017, "身份证格式错误"),
	CONTACTER_MOBILE_REPEAT(10018, "关系人手机号重复"),
	THIRD_CHANNEL_OAUTH_ERROR(10019,"第三方渠道登录授权错误"),
	LIFE_PAY_BILL_NULL(10020,"账单不存在"),
	CAS_TGT_IS_EXPIRED(10021,"TGT已过期"),
	INCORRECT_CREDENTIALS(10022,"用户名密码不匹配"),
	ENABLED_WORK_WX(10023,"该公司暂未开启微信企业号"),
	IMAGE_CODE_VALIDATION_ERROR(10024,"图片验证码不正确"),
	EXCEEDED_LIMIT_TIMES_SMS_VALIDATE_CODE_PRE_24H(10025,"超过24小时内短信验证码最大使用次数，请稍后再试"),
    LOGIN_FAIL(10026,"登录失败，用户名或密码错误"),
    OAUTH_FAIL(10027,"认证失败，身份证和姓名不匹配！"),
    KEYWORD_FAIL(10028,"语音匹配失败，请重试！"),;

	public Integer code;
	public String message;

	RestApiExceptionEnums(Integer code, String message){
		this.code = code;
		this.message = message;
	}

	@Override
	public Integer getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

}
