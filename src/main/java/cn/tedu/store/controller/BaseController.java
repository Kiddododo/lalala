package cn.tedu.store.controller;
/**
 * 控制器的基类
 * @author soft01
 *
 */
import javax.servlet.http.HttpSession;

abstract class BaseController {

	/**
	 * 响应正确时使用的状态码
	 */
	public static final Integer OK=2000;
	
	/**
	 * 从Session中获取当前登陆的用户的id
	 * @param session
	 * @return 返回当前登陆的用户的id
	 */
	protected final Integer getUidFromSession(HttpSession session) {
		return Integer.valueOf((session.getAttribute("uid")).toString());
	}
	
	/**
	 * 从Session中获取当前登陆的用户的用户名
	 * @param session
	 * @return 返回当前登陆的用户的用户名
	 */
	protected final String getUsernameFromSession(HttpSession session) {
		return session.getAttribute("username").toString();
	}
	
	
}
