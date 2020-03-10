package cn.tedu.store.mapper;
/**
 * 处理用户数据的持久层接口的测试类
 */
import java.sql.SQLException;
import java.util.Date;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.tedu.store.entity.User;
import cn.tedu.store.mapper.UserMapper;

//测试类前，只要是SpringBoot 2.1.x项目中的单元测试类都必须添加这2个注解
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserMapperTests {

	@Autowired
	public DataSource dataSource;
	
	@Autowired
	private UserMapper userMapper;
	
	@Test
	public void insert() {
		User user=new User();
		user.setUsername("工藤新一");
		user.setPassword("123456");
		user.setSalt("");
		user.setGender(1);
		user.setPhone("12345678901");
		user.setEmail("abcd@qq.com");
		user.setAvatar("头像");
		Integer rows=userMapper.insert(user);
		System.err.println("rows:"+rows);
		System.err.println(user);
	}
	
	@Test
	public void updateInfoByUid() {
		User user=new User();
		user.setUid(12);
		user.setPhone("12345678901");
		user.setEmail("123456@qq.com");
		user.setGender(1);
		user.setAvatar("");
		//user.setCreatedUser("");
		//user.setCreatedTime(new Date());
		user.setModifiedUser("");
		user.setModifiedTime(new Date());
		Integer rows=userMapper.updateInfoByUid(user);
		System.err.println("rows="+rows);
	}
	
	@Test
	public void updatePasswordByUid() {
		Integer uid=4;
		String password="1234";
		String modifiedUser="峰不二子";
		Date modifiedTime=new Date();
		Integer rows=userMapper.updatePasswordByUid(uid, password, modifiedUser, modifiedTime);
		System.err.println("rows=" + rows);
	}
	
	@Test
	public void updateAvatarByUid() {
		Integer uid=12;
		String avatar="头像";
		String modifiedUser="xxx";
		Date modifiedTime=new Date();
		Integer rows=userMapper.updateAvatarByUid(uid, avatar, modifiedUser, modifiedTime);
		System.err.println("rows="+rows);
	}
	
	@Test
	public void findByUid() {
		Integer uid=4;
		User result=userMapper.findByUid(uid);
		System.err.println(result);
	}
	
	@Test
	public void findByUsername() {
		String username="工藤新一";
		User result=userMapper.findByUsername(username);
		System.err.println("result:"+result);
	}
	
	@Test
	public void getConnection() throws SQLException{
		System.err.println(dataSource.getConnection());
	}
	
}
