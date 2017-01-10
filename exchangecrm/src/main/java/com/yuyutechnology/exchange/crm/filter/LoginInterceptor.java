package com.yuyutechnology.exchange.crm.filter;

import java.util.HashSet;
import java.util.Set;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

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
	

	@PostConstruct
	private void init() {
		logger.info("=============initLoginInterceptor start===============");
		
		noMappingSet.add("/login");
		noMappingSet.add("/dologin");
		noMappingSet.add("/index");
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
	 * 定义登录页面
	 */
	public static final String LOGIN_PAGE = "/login";

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
		String adminName=(String) request.getSession().getAttribute("adminName");
		// 判断是否需要拦截或者是否登录
		if (validURL(requestURI) || adminName!=null) {
			logger.info("request URI:" + requestURI + " adminName: " + adminName);
			return true;
		} else {
			logger.info("LOGIN_PAGE================"+LOGIN_PAGE);
			response.sendRedirect(request.getContextPath() + LOGIN_PAGE);
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
