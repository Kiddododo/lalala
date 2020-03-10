package cn.tedu.store.interceptor;
/**
 * 拦截器
 */
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

public class LoginInterceptor implements HandlerInterceptor{

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		if(request.getSession().getAttribute("uid")==null) {
			response.sendRedirect("/web/login.html");
			//拦截
			return false;
		}
		//放行
		return true;
	}
	
}
