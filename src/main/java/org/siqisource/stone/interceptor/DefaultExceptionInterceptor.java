package org.siqisource.stone.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.UnauthorizedException;
import org.siqisource.stone.communication.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Component
public class DefaultExceptionInterceptor implements HandlerExceptionResolver {

	public static final Logger logger = LoggerFactory.getLogger(DefaultExceptionInterceptor.class);

	/**
	 * 将异常包装为Json格式返回。
	 */
	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		ModelAndView mv = new ModelAndView();
		logger.error("ExceptionInterceptor Catch Error:" + request.getRequestURL(), ex);
		String requestId = MDC.get("requestId");
		MappingJackson2JsonView view = new MappingJackson2JsonView();
		mv.setView(view);
		mv.addObject("status", "failed");
		if (ex instanceof BusinessException) {
			mv.addObject("message", ex.getMessage());
		} else if (ex instanceof UnauthorizedException) {
			mv.addObject("message", "您访问了未授权的资源[" + requestId + "]");
		} else {
			mv.addObject("message", "系统发生了未识别的错误[" + requestId + "]");
		}
		response.setStatus(500);

		return mv;
	}

}
