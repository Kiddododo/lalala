package cn.tedu.store.service;
/**
 * 处理用户数据的业务层接口的测试类
 */
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.service.ex.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTests {

	@Autowired
	private IUserService service;

	@Test
	public void insert() {
		try {
			User user=new User();
			user.setUsername("怪盗基德");
			user.setPassword("123456");
			user.setGender(1);
			user.setPhone("12345678901");
			user.setEmail("abcd@qq.com");
			user.setAvatar("");
			service.reg(user);
			System.err.println("OK");
			System.err.println(user);
		}catch(ServiceException e){
			System.err.println(e.getClass().getName());
			//e.printStackTrace();
		}
	}

	@Test
	public void login() {
		try {
			String username="柯南";
			String password="123456";
			User user=service.login(username, password);
			System.err.println("OK");
			System.err.println(user);
		}catch(ServiceException e){
			System.err.println(e.getClass().getName());//获得该异常的类对象的类名
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void changePassword() {
		try {
			Integer uid=12;
			String oldPassword="123456";
			String newPassword="1234";
			String username="鲁邦三世";
			service.changePassword(uid, oldPassword, newPassword, username);
			System.err.println("OK");
		}catch(ServiceException e) {
			System.err.println(e.getClass().getName());//获得该异常的类对象的类名
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void showInfo() {
		try {
			Integer uid=12;
			service.showInfo(uid);
			System.err.println("OK");
		}catch(ServiceException e) {
			System.err.println(e.getClass().getName());//获得该异常的类对象的类名
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void changeInfo() {
		try {
			Integer uid=12;
			String username="鲁邦三世";
			User user=new User();
			user.setPhone("12345678901");
			user.setEmail("abcd@qq.com");
			user.setGender(1);
			service.changeInfo(uid, username, user);
			System.err.println("OK.");
			System.err.println(user);
		}catch(ServiceException e) {
			System.err.println(e.getClass().getName());//获得该异常的类对象的类名
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void changeAvatar() {
		try {
			Integer uid=12;
			String avatar="头像";
			String username="鲁邦三世";
			service.changeAvatar(uid, avatar, username);
			System.err.println("OK");
		}catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}
	
	
	
}



