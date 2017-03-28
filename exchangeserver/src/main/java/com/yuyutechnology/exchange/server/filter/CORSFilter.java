package com.yuyutechnology.exchange.server.filter;
import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.filter.OncePerRequestFilter;
public class CORSFilter extends OncePerRequestFilter
{
	public static Logger logger = LogManager.getLogger(CORSFilter.class);
	@Override
	protected void doFilterInternal(HttpServletRequest request,
			HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException
	{
		logger.info("CORSFilter ========================{}", request.getRequestURI());
		response.setHeader("Access-Control-Allow-Origin", "*");
//		response.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
		response.setHeader("Access-Control-Allow-Headers", "accept, content-type");
		filterChain.doFilter(request, response);
	}
}
