package cn.tedu.store;

import java.sql.SQLException;
import java.util.Random;
import java.util.UUID;
import javax.sql.DataSource;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.DigestUtils;

@RunWith(SpringRunner.class)
@SpringBootTest
public class StoreApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	public DataSource dataSource;
	
	@Test//测试数据库
	public void getConnection() throws SQLException{
		System.err.println(dataSource.getConnection());
	}
	
	@Test
	public void md5() {
		/*
		 盐值就是为了使相同的密码拥有不同的hash值的一种手段，就是盐化，盐值就是在密码hash的过程中添加的额外的随机值
		 MD5信息摘要算法（英语：MD5 Message-Digest Algorithm），一种被广泛使用的密码散列函数，
		 可以产生出一个128位（16字节）的散列值（hash value），用于确保信息传输完整一致。
		 */		
		// MD = Message Digest
		String password = "1234";
		/*
		 * UUID 是 通用唯一识别码（Universally Unique Identifier）的缩写，
		 * 是一种软件建构的标准，亦为开放软件基金会组织在分布式计算环境领域的一部分。
		 * 其目的，是让分布式系统中的所有元素，都能有唯一的辨识信息，而不需要通过中央控制端来做辨识信息的指定。
		 * 如此一来，每个人都可以创建不与其它人冲突的UUID。在这样的情况下，就不需考虑数据库创建时的名称重复问题
		 */		
		String salt=UUID.randomUUID().toString();
		String md5Password = DigestUtils.md5DigestAsHex(password.getBytes());
		System.err.println(md5Password);
		for (int i = 0; i < 10; i++) {
			password = DigestUtils.md5DigestAsHex((password + salt).getBytes());
			System.err.println(password);
		}
		// 1234
		// 81dc9bdb52d04dc20036dbd8313ed055
		// 0
		// cfcd208495d565ef66e7dff9f98764da
		// 约1000个0
		// 88bb69a5d5e02ec7af5f68d82feb1f1d
		
	}
	
	
	@Test
	public void random() {
		for (int i = 0; i < 10; i++) {
			Random random = new Random();
			System.err.println(random.nextInt());
			// System.err.println(System.nanoTime());
		}
	}
	
	@Test
	public void integer() {
		Integer i1 = 127;
		Integer i2 = 127;
		Integer i3 = 128;
		Integer i4 = 128;
		Integer i5 = -128;
		Integer i6 = -128;
		Integer i7 = -129;
		Integer i8 = -129;
		System.err.println(i1 == i2);
		System.err.println(i3 == i4);
		System.err.println(i3.equals(i4));
		System.err.println("-------------");
		System.err.println(i5 == i6);
		System.err.println(i7 == i8);
		System.err.println(i7.equals(i8));
	}
	
	
	
	
}
