package com.yuyutechnology.exchange.server.filter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.server.controller.response.BaseResponse;
import com.yuyutechnology.exchange.server.controller.response.EncryptResponse;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.session.SessionData;
import com.yuyutechnology.exchange.session.SessionDataHolder;
import com.yuyutechnology.exchange.session.SessionManager;
import com.yuyutechnology.exchange.startup.ServerContext;
import com.yuyutechnology.exchange.utils.AESCipher;
import com.yuyutechnology.exchange.utils.JsonBinder;
import com.yuyutechnology.exchange.utils.ResourceUtils;

/**
 * 登录拦截器
 * 
 * @author suzan.wu
 *
 */
public class LoginInterceptor extends HandlerInterceptorAdapter {

	private final Logger logger = LogManager.getLogger(this.getClass());

	SessionManager sessionManager;

	@PostConstruct
	private void init() {
		logger.info("=============initLoginInterceptor start===============");
		sessionManager = ServerContext.getBean(SessionManager.class);
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
        //logger.info("interceptor excute order:1.preHandle================");
		// 允许跨域访问
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Headers", "accept, content-type");
		String requestURI = request.getRequestURI();
		String sessionId = getToeknFromURI(request.getRequestURI());
		SessionData sessionData = sessionManager.get(sessionId);
		// 判断是否需要拦截或者是否登录
		if (sessionData!= null) {
			logger.info("request URI:" + requestURI + " session : " + sessionId + "==>session ok");
			sessionManager.refreshSessionDataExpireTime(sessionData);
			SessionDataHolder.setSessionData(sessionData);
			return true;
		} else {
			logger.info("request URI:" + requestURI + " session : " + sessionId + " " +MessageConsts.SESSION_TIMEOUT);
			BaseResponse re = new BaseResponse();
			re.setRetCode(RetCodeConsts.SESSION_TIMEOUT);
			re.setMessage(MessageConsts.SESSION_TIMEOUT);
			String json = JsonBinder.getInstance().toJson(re);
	        String key = ResourceUtils.getBundleValue4String("aes.key");
	        if (StringUtils.isNotBlank(key) && ((HandlerMethod)handler).getMethodAnnotation(ResponseEncryptBody.class) != null) {
	        	json = AESCipher.encryptAES(json,key);
	        	EncryptResponse encryptResponse = new EncryptResponse();
	        	encryptResponse.setContent(json);
	        	json = JsonBinder.getInstance().toJson(encryptResponse);
	        }
			response.getOutputStream().print(json);
			response.getOutputStream().close();
			return false;
		}
	}

	// 在业务处理器处理请求执行完成后,生成视图之前执行的动作
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		 logger.info("interceptor excute order:2.postHandle================");
	}

	/**
	 * 在DispatcherServlet完全处理完请求后被调用
	 * 
	 * 当有拦截器抛出异常时,会从当前拦截器往回执行所有的拦截器的afterCompletion()
	 */
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		 logger.info("interceptor excute order:3.afterCompletion================");
	}

}
