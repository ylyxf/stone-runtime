package org.siqisource.stone.interceptor;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Component
public class HandlerInterceptor extends HandlerInterceptorAdapter {

	Logger logger = LoggerFactory.getLogger(HandlerInterceptor.class);

	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		String requestId = MDC.get("requestId");
		if (requestId == null) {
			System.out.println(request.getRequestURI());

			requestId = String.valueOf(SecurityUtils.getSubject().getPrincipal()) + ":" + sdf.format(new Date()) + ":"
					+ UUID.randomUUID();
			MDC.put("requestId", requestId);
		}
		logger.info("handler request: {}", request.getRequestURL());
		return true;
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//从请求头中取得鉴权需求，并将鉴权结果放到响应头中

		MDC.clear();
	}
}
