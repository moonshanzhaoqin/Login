package com.yuyutechnology.exchange.server.filter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import com.yuyutechnology.exchange.server.controller.request.BaseRequest;
import com.yuyutechnology.exchange.util.JsonBinder;

public class SecurityInterceptor implements MethodInterceptor {

	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		System.out.println("SecurityInterceptor=================="+invocation.getMethod().getName());
		for (Object arg : invocation.getArguments()) {
			if (arg instanceof BaseRequest) {
				System.out.println("sign ========"+((BaseRequest)arg).getSign() + " /n "+JsonBinder.getInstance().toJson(arg));
//				if (StringUtils.isBlank(((BaseRequest)arg).getSign())) {
//					throw new ServerException("sign valid !!!!!!!!!!!!!!!!!!!!");
//				}
			}
		}
		return invocation.proceed();
	}
}
