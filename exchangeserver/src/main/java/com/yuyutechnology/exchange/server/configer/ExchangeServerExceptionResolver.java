/**
 * 
 */
package com.yuyutechnology.exchange.server.configer;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.server.controller.response.BaseResponse;
import com.yuyutechnology.exchange.util.JsonBinder;

/**
 * @author silent.sun
 *
 */
public class ExchangeServerExceptionResolver implements HandlerExceptionResolver {
	private static Logger logger = LogManager.getLogger(ExchangeServerExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.error("request URI Exception:", ex);
		BaseResponse re = new BaseResponse();
		re.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
		re.setMessage(MessageConsts.RET_CODE_FAILUE + " : " + ex.toString());
		try {
			response.getOutputStream().print(JsonBinder.getInstance().toJson(re));
			response.getOutputStream().close();
		} catch (IOException e) {
		}
		return null;
	}
}
