package com.yuyutechnology.exchange.server.filter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yuyutechnology.exchange.ServerConsts;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;
import com.yuyutechnology.exchange.session.SessionManager;
import com.yuyutechnology.exchange.startup.ServerContext;

/**
 * 登录拦截器
 * 
 * @author suzan.wu
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = Logger.getLogger(this.getClass());

	private static final Set<String> noMappingSet = new HashSet<String>();
	private static final Set<String> noMappingSetUrl = new HashSet<String>();
	
	SessionManager sessionManager;

	@PostConstruct
	private void init() {
		logger.info("=============initLoginInterceptor start===============");
		
		sessionManager = ServerContext.getBean(SessionManager.class);
		
		noMappingSet.add("/login");
		noMappingSet.add("/register");
		noMappingSet.add("/getRegistrationCode");
		noMappingSet.add("/getVerificationCode");
		noMappingSet.add("/testCode");
		noMappingSet.add("/forgetPassword");

		// swagger
		noMappingSetUrl.add("api-docs");
		noMappingSetUrl.add("swagger");
	}

	/**
	 * 判断是否需要拦截
	 * 
	 * @param method
	 * @return
	 */
	private boolean validURL(String method) {
		for (String string : noMappingSetUrl) {
			if (method.contains(string)) {
				return true;
			}
		}
		method = method.substring(method.lastIndexOf("/"));
		if (noMappingSet.contains(method)) {
			return true;
		} else {
			return false;
		}

	}

	/**
	 * 从URI中获取token
	 * 
	 * @param uri
	 * @return
	 */
	private String getToeknFromURI(String uri) {
		int index = StringUtils.indexOf(uri, "/token");
		if (index > -1) {
			String param = uri.substring(index);
			String token = StringUtils.replace(param, "/token/", "");
			if (token.indexOf("/") != -1) {
				return token.substring(0, token.indexOf("/"));
			}
			return token;
		} else {
			return null;
		}
	}

	/**
	 * Controller之前执行 在业务处理器处理请求之前被调用 如果返回false
	 * 从当前的拦截器往回执行所有拦截器的afterCompletion(),再退出拦截器链
	 * 
	 * 如果返回true 执行下一个拦截器,直到所有的拦截器都执行完毕 再执行被拦截的Controller 然后进入拦截器链,
	 * 从最后一个拦截器往回执行所有的postHandle() 接着再从最后一个拦截器往回执行所有的afterCompletion()
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// logger.info("interceptor excute order:1.preHandle================");

		// 允许跨域访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "accept, content-type");

		String requestURI = request.getRequestURI();
		String sessionId = getToeknFromURI(request.getRequestURI());
		logger.info("request URI:" + requestURI + " session : " + sessionId);

		SessionData sessionData = sessionManager.get(sessionId);

		// 判断是否需要拦截或者是否登录
		if (validURL(requestURI) || sessionData.getUserId() != null) {
			logger.info("===================success=================");
//			logger.info("===================Language=================" + request.getParameter("language"));
//			sessionData.setBrowserLanguage(request.getParameter("language"));
			return true;
		} else {
			response.setStatus(500);
			response.getOutputStream()
					.print("{\"retCode\": " + ServerConsts.SESSION_TIMEOUT + " , \"msg\" : \"session timeout\"}");
			response.getOutputStream().close();
			return false;
		}
	}

	// 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// logger.info("interceptor excute order:2.postHandle================");
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// logger.info("interceptor excute
		// order:3.afterCompletion================");
	}

}