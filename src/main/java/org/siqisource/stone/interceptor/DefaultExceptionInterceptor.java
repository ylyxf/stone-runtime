package org.siqisource.stone.interceptor;

import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.siqisource.stone.result.ActionResult;
import org.siqisource.stone.result.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

@Component
public class DefaultExceptionInterceptor implements HandlerExceptionResolver {

	public static final Logger logger = LoggerFactory.getLogger(DefaultExceptionInterceptor.class);

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		logger.error("ExceptionInterceptor Catch Error:", ex);
		ModelAndView mv = new ModelAndView();
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
			Method method = handlerMethod.getMethod();
			ResponseBody responseBodyAnn = AnnotationUtils.findAnnotation(method, ResponseBody.class);
			if (responseBodyAnn != null) {
				MappingJackson2JsonView view = new MappingJackson2JsonView();
				mv.setView(view);
				ActionResult actionResult = new ActionResult(false);
				if (ex instanceof BusinessException) {
					actionResult.setMessage(ex.getMessage());
				} else {
					actionResult.setMessage("系统发生了未识别的错误");
				}
				mv.addObject(actionResult);
			}
		} else {
			mv.setViewName("error/Error");
			mv.addObject("error", ex);
		}

		return mv;

	}

}
