package com.cmig.future.csportal.module.operate.integral.components;

import com.cmig.future.csportal.common.config.Global;
import com.cmig.future.csportal.module.base.enums.RestApiExceptionEnums;
import com.cmig.future.csportal.module.base.exceptions.ServiceException;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @Author:zhangtao
 * @Description:定义一个切面
 * @Date Created in 14:14 2017/7/13.
 * @Modified by zhangtao on 14:14 2017/7/13.
 */
@Component
@Aspect
public class IntegralAspect {

	private final Logger logger= LoggerFactory.getLogger(IntegralAspect.class);

	/**
	 * 配置前置通知,匹配com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation的所有方法执行做为切入点
	 * @param joinPoint
	 */
	@Before("execution(* com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation.*(..))")
	public void beforeIntegral(JoinPoint joinPoint){
		logger.debug("调用积分系统接口前置增强，判断积分系统配置是否开启");
		if(!Global.getBoolean("INTEGRAL.ISENABLE_OR_DISABLED",false)){
			throw new ServiceException(RestApiExceptionEnums.INTEGRAL_SYSTEM_UNABLE);
		}
	}

	/**
	 * 配置后置通知,匹配com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation的所有方法执行做为切入点
	 * @param joinPoint
	 */
/*	@After("execution(* com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation.*(..))")
	public void afterIntegral(JoinPoint joinPoint){
		logger.debug("调用积分系统接口后置增强");
	}*/

	/**
	 * 配置环绕通知,匹配com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation的所有方法执行做为切入点
	 * @param joinPoint
	 */
	/*@Around("execution(* com.cmig.future.csportal.module.operate.integral.components.IntegralRuleOperation.*(..))")
	public Object aroundIntegral(ProceedingJoinPoint joinPoint) throws Throwable {
		logger.debug("调用积分系统接口around 增强 begin ");
		// 获取目标方法原始的调用参数
		Object[] args = joinPoint.getArgs();
		Object object= joinPoint.proceed();
		logger.debug("调用积分系统接口around 增强 end ");
		return object;
	}*/
}
