package cn.tedu.store.service;
/**
 * 处理用户数据的业务层接口
 */
import cn.tedu.store.entity.User;

public interface IUserService {

	/**
	 * 用户注册
	 * @param user 用户数据
	 */
	void reg(User user);
	
	/**
	 * 用户登录
	 * @param username 用户名
	 * @param password 密码
	 * @return 成功登录的用户数据
	 */
	User login(String username,String password);
	
	/**
	 * 
	 * @param uid 用户id
	 * @param oldPassword 旧密码
	 * @param newPassword 新密码
	 * @param username 用户名
	 */
	void changePassword(Integer uid, String oldPassword, String newPassword, String username);

	/**
	 * 显示当前登录用户的基本信息
	 * @param uid 当前登录的用户的id
	 * @return 该用户的基本信息
	 */
	User showInfo(Integer uid);
	
	/**
	 * 修改用户的基本资料
	 * @param uid 用户的id
	 * @param username 用户名
	 * @param user 封装了用户的新资料的对象
	 */
	void changeInfo(Integer uid,String username,User user);
	
	/**
	 * 修改头像
	 * @param uid 用户的id
	 * @param avatar 新的头像路径
	 * @param username 用户名
	 */
	void changeAvatar(Integer uid, String avatar, String username);
	
	
	
}
