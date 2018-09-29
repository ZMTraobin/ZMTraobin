package com.cmig.future.csportal.common.utils;

/**
 * class name: ExceptionConstants 
 * author: fri
 * date: 2017年3月29日
 * function: 异常返回码数据处理类
 */
public final class ExceptionConstants {

	private ExceptionConstants() {}
	
	public static final ResultEnums REQUEST_ERROR			= ResultEnums._0000000;
	public static final ResultEnums REQUEST_EXCEPTION		= ResultEnums._1000000;
	public static final ResultEnums DATA_IS_NULL             = ResultEnums._1000001;
	public static final ResultEnums DATA_IS_REQUIRED			= ResultEnums._1000002;
	public static final ResultEnums OBJ_IS_CHANGE_FAILED		= ResultEnums._1000003;
	public static final ResultEnums OBJ_IS_CHECK_FAILED		= ResultEnums._1000004;
	public static final ResultEnums TGT_IS_GET_FAILED		= ResultEnums._1000005;

	public enum ResultEnums {
        /*======数据异常1000开头=========*/
		_0000000("请求第三方接口失败"),
		_1000000("接口参数传递类型异常"),
		_1000001("查询数据异常"),
		_1000002("缺少必传参数"),
		_1000003("入参对象转换异常"),
		_1000004("入参对象校验异常"),
		_1000005("TGT失效");
        
		
        private String error; // 错误信息
		
		ResultEnums(String error) {
			this.error = error;
		}
		
		public String getError() {
			return error;
		}
		
		public void setError(String error) {
			this.error = error;
		}
	}
}
