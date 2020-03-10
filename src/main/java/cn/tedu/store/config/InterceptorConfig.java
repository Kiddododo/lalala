package cn.tedu.store.config;
/**
 * 设置拦截器类
 * @author soft01
 *
 */
import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import cn.tedu.store.interceptor.LoginInterceptor;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		HandlerInterceptor interceptor= new LoginInterceptor();//向上造型
		List<String> patterns = new ArrayList<>();
		//白名单
		patterns.add("/users/reg");
		patterns.add("/users/login");
		patterns.add("/web/register.html");
		patterns.add("/web/login.html");
		patterns.add("/web/index.html");
		patterns.add("/web/product.html");
		patterns.add("/bootstrap3/**");
		patterns.add("/css/**");
		patterns.add("/images/**");
		patterns.add("/js/**");
		patterns.add("/districts/**");
		patterns.add("/products/**");
		
		//添加注册拦截器//拦截所有//除了该patterns之外的
		registry.addInterceptor(interceptor)
			.addPathPatterns("/**")
			.excludePathPatterns(patterns);
		
	}

}