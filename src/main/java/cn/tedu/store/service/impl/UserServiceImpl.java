package cn.tedu.store.service.impl;
/**
 * 处理用户数据的业务层接口的实现类，用于处理用户注册和登录
 */
import java.util.Date;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import cn.tedu.store.entity.User;
import cn.tedu.store.mapper.UserMapper;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameDuplicateException;

@Service//该类交给Spring管理
public class UserServiceImpl implements IUserService {

	@Autowired//注入属性
	private UserMapper userMapper;
	
	
	@Override
	public void reg(User user) {
		//输出日志
		System.err.println("UserServiceImple.reg()");
		System.err.println("\t注册数据：" + user);
		//从参数user中获取用户名
		String username=user.getUsername();
		//调用userMapper的findByUsername()方法执行查询
		User result=userMapper.findByUsername(user.getUsername());
		//判断查询结果是否不为null
		if(result!=null) {
			//是：查询结果不为null，表示用户名已经被占用，则抛出UsernameDuplicateException
			throw new UsernameDuplicateException("注册失败，您尝试注册的用户名(" + username + ")已经被占用");
		}
		//准备执行注册
		//补全数据：加密后的密码，盐值
		//盐值
		String salt=UUID.randomUUID().toString();
		user.setSalt(salt);
		//密码
		String md5Password = getMd5Password(user.getPassword(), salt);
		user.setPassword(md5Password);
		//补全数据，isDelete：值为0
		user.setIsDelete(0);
		//补全数据：4项日志
		Date now=new Date();
		user.setCreatedUser(username);
		user.setCreatedTime(now);
		user.setModifiedUser(username);
		user.setModifiedTime(now);
		//调用userMapper的insert()方法执行注册，并获取返回的受影响行数
		System.err.println("\t插入数据：" + user);
		Integer rows=userMapper.insert(user);
		//判断受影响的行数是否不为1
		if(rows!=1) {
			//是：抛出InsertException
			throw new InsertException("注册失败，执行插入数据时出现未知错误，请联系系统管理员");
		}
	}
	
	
	@Override
	public User login(String username, String password) {
		System.err.println("UserServiceImple.login()");
		System.err.println("\tusername=" + username);
		System.err.println("\tpassword=" + password);
		//根据参数username，调用userMapper.findByUsername()方法执行查询
		User result=userMapper.findByUsername(username);
			//判断查询结果是否为null 
			if(result==null) {
			//是：抛出UserNotFoundException
			throw new UserNotFoundException("登录失败，用户数据不存在");
			}
			//判断查询结果中的isDelete是否为1
			if(result.getIsDelete()==1) {
				//是：抛出UserNotFoundException
				throw new UserNotFoundException("登录失败，用户数据不存在");
			}
			//从查询结果中取出盐值
			String salt=result.getSalt();
			String md5Password=getMd5Password(password, salt);
			//基于参数password与盐值执行加密，得到密文
			//判断以上得到的密文与查询结果中的password是否不一致
			//输入的密码和数据库中password加密后的密码对比
			System.err.println("\t数据库中的密码：" + result.getPassword());
			if(!(md5Password).equals(result.getPassword())) {
				//是：抛出PasswordNotMatchException
				throw new PasswordNotMatchException("登录失败，密码错误");
			}
			System.err.println("\t用户名密码正确，登录成功");
			//创建新的User对象
			User user=new User();
			//将查询结果中的uid、username、avatar的值封装到新创建的对象中
			user.setUid(result.getUid());
			user.setUsername(result.getUsername());
			user.setAvatar(result.getAvatar());
			//返回新创建的对象
			return user;
		}
	
	
	public void changePassword(Integer uid,String oldPassword,String newPassword,String username) {
			// 日志：输出原密码，新密码
		   System.err.println("UserServiceImple.change()");
			System.err.println("oldPassword:"+oldPassword);
			System.err.println("newPassword:"+newPassword);
			// 基于参数uid调用userMapper的findByUid()查询用户数据
			User result=userMapper.findByUid(uid);
			// 判断查询结果是否为null
			if(result==null) {
				// 是：UserNotFoundException
				throw new UserNotFoundException("用户数据不存在");
			}
			// 判断查询结果中的isDelete是否为1
			if(result.getIsDelete()==1) {
				// 是：UserNotFoundException
				throw new UserNotFoundException("用户数据不存在");
			}
			// 从查询结果中取出盐值
			String salt=result.getSalt();
			// 日志：“将原密码执行加密”
			System.err.println("将原密码执行加密");
			// 将参数oldPassword结合盐值执行加密，得到oldMd5Password
			String oldMd5Password=getMd5Password(oldPassword, salt);
			// 日志：输出查询结果中的password
			System.err.println("\t数据库中的密码:"+result.getPassword());
			// 判断oldMd5Password与查询结果中的password是否不一致
			if(!(oldMd5Password).equals(result.getPassword())) {
				// 是：PasswordNotMatchException
				throw new PasswordNotMatchException("密码错误");
			}
			// 日志：“将新密码执行加密”
			System.err.println("将新密码执行加密");
			// 将参数newPassword结合盐值执行加密，得到newMd5Password
			String newMd5password=getMd5Password(newPassword, salt);
			// 调用userMapper的updatePasswordByUid()执行更新密码，并获取返回的受影响的行数
			Integer rows=userMapper.updatePasswordByUid(uid, newMd5password, username , new Date());
			// 判断受影响的行数是否不为1
			if(rows!=1) {
				// 是：UpdateException
				throw new UpdateException("密码修改成功");
			}
		
	}
	
	
	//获取已经修改好的信息
	public User showInfo(Integer uid) {
		//根据参数uid查询用户数据
		User result=userMapper.findByUid(uid);
		//判断查询结果是否为null
		if(result==null) {
			// 是：UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}
		//判断查询结果中的isDelete是否为1
		if(result.getIsDelete()==1) {
			// 是：UserNotFoundException
			throw new UserNotFoundException("用户数据不存在");
		}
		//创建新的User对象
		User user=new User();
		//设置从结果中获得的属性
		user.setUsername(result.getUsername());
		user.setPhone(result.getPhone());
		user.setEmail(result.getEmail());
		user.setGender(result.getGender());
		//返回User类的对象
		return user;
	}
	
	
	public void changeInfo(Integer uid,String username,User user) {
				//基于参数uid调用userMapper的findByUid()查询用户数据
				User result=userMapper.findByUid(uid);
				// 判断查询结果是否为null
				if (result==null) {
					// 是：抛出UserNotFoundException
					throw new UserNotFoundException("修改用户资料失败，用户数据不存在");
				}
				// 判断查询结果中的isDelete是否为1
				if (result.getIsDelete()==1) {
					// 是：抛出UserNotFoundException
					throw new UserNotFoundException("修改用户资料失败，用户数据不存在");
				}
				//向参数user中封装uid
				user.setUid(uid);
				// 向参数user中封装username到modifiedUser属性：
				user.setModifiedUser(username);
				// 向参数user中封装当前时间到modifiedTime属性：
				user.setModifiedTime(new Date());
				// 调用持久层userMapper的updateInfoByUid(user)执行更新，获取返回的受影响的行数
				Integer rows=userMapper.updateInfoByUid(user);
				// 判断受影响的行数是否不为1
				if (rows != 1) {
				// 是：UpdateException
					throw new UpdateException(
							"修改用户资料失败，执行更新用户资料时出现未知错误，请联系系统管理员");
				}
		}
	
	
	public void changeAvatar(Integer uid,String avatar,String username) {
		// 基于参数uid调用userMapper的findByUid()查询用户数据
		User result=userMapper.findByUid(uid);
		// 判断查询结果是否为null
				if (result == null) {
					// 是：抛出UserNotFoundException
					throw new UserNotFoundException(
						"修改用户头像失败，用户数据不存在");
				}
				// 判断查询结果中的isDelete是否为1
				if (result.getIsDelete()==1) {
					// 是：抛出UserNotFoundException
					throw new UserNotFoundException(
						"修改用户头像失败，用户数据不存在");
				}
				// 调用userMapper的updateAvatarByUid()执行更新密码，并获取返回的受影响的行数
				Integer rows=userMapper.updateAvatarByUid(uid, avatar, username, new Date());
				// 判断受影响的行数是否不为1
				if(rows!=1) {
					// 是：UpdateException
					throw new UpdateException(
						"修改用户头像失败，执行更新用户头像时出现未知错误，请联系系统管理员");
				}
	}
	
	
	/**
	 * 执行密码加密，获取加密后的密码
	 * @param password 原始密码
	 * @param salt 盐值
	 * @return 加密后的密码
	 */
	private String getMd5Password(String password,String salt) {
		/*
		 * 自定义加密规则：
		 * 1、使用"盐+密码+盐"作为原文
		 * 2、三重加密
		 */
		System.err.println("\t加密原始密码："+password);
		System.err.println("\t加密-盐值："+salt);
		for(int i=0;i<3;i++) {
			password=DigestUtils.md5DigestAsHex((salt+password+salt).getBytes());
		}
		System.err.println("\t加密-密文："+password);
		return password;
	}

	

}
