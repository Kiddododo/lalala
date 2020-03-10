package cn.tedu.store.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class TimerAspect {

	@Around("execution(* cn.tedu.store.service.impl.*.*(..))")//有空格
	public Object aaaa(ProceedingJoinPoint pjp) throws Throwable {
		//记录起始时间
		long start=System.currentTimeMillis();
		//执行业务层的方法，例如UserServiceImpl中的reg()方法或login()方法
		//调用以下方法时，会出现异常，必须抛出，不可以try...catch
		Object obj=pjp.proceed();
		//记录结束时间
		long end=System.currentTimeMillis();
		System.err.println("耗时："+(end-start)+"ms");
		//必须返回以上proceed()方法的返回值
		//否则相当于login(),reg()等业务方法都不会返回值
		return obj;
		
	}
	
}
