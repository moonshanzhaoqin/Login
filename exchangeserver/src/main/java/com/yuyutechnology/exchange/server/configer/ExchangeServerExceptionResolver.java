/**
 * 
 */
package com.yuyutechnology.exchange.server.configer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.yuyutechnology.exchange.MessageConsts;
import com.yuyutechnology.exchange.RetCodeConsts;
import com.yuyutechnology.exchange.server.controller.response.BaseResponse;
import com.yuyutechnology.exchange.server.controller.response.EncryptResponse;
import com.yuyutechnology.exchange.server.security.annotation.ResponseEncryptBody;
import com.yuyutechnology.exchange.util.AESCipher;
import com.yuyutechnology.exchange.util.JsonBinder;
import com.yuyutechnology.exchange.util.ResourceUtils;

/**
 * @author silent.sun
 *
 */
public class ExchangeServerExceptionResolver implements HandlerExceptionResolver {
	private static Logger logger = LogManager.getLogger(ExchangeServerExceptionResolver.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		//全局错误
		logger.error("request URI Exception:", ex);
		BaseResponse re = new BaseResponse();
		re.setRetCode(RetCodeConsts.RET_CODE_FAILUE);
		re.setMessage(MessageConsts.RET_CODE_FAILUE + " : " + ex.toString());
		try {
			String json = JsonBinder.getInstance().toJson(re);
			String key = ResourceUtils.getBundleValue4String("aes.key");
			if (StringUtils.isNotBlank(key) && ((HandlerMethod) handler).getMethodAnnotation(ResponseEncryptBody.class) != null) {
				json = AESCipher.encryptAES(json, key);
				EncryptResponse encryptResponse = new EncryptResponse();
				encryptResponse.setContent(json);
				json = JsonBinder.getInstance().toJson(encryptResponse);
			}
			response.getOutputStream().print(json);
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
