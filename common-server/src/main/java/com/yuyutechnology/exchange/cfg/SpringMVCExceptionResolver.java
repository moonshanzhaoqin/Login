/**
 * 
 */
package com.yuyutechnology.exchange.cfg;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
/**
 * @author silent.sun
 *
 */
public class SpringMVCExceptionResolver implements HandlerExceptionResolver{
	private static Logger logger = LoggerFactory.getLogger(SpringMVCExceptionResolver.class);
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.error("spring mvc error", ex);
		return null;
	}

}
