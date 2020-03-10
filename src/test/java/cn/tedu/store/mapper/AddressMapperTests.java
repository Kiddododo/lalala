package cn.tedu.store.mapper;

import java.util.Date;
import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.tedu.store.entity.Address;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressMapperTests {

	@Autowired
	private AddressMapper addressMapper;
	
	@Test
	public void insert() {
		Address address=new Address();
		address.setUid(1);
		address.setReceiver("鲁邦三世");
		address.setProvinceName("湖北");
		address.setProvinceCode("420000");
		address.setCityName("武汉");
		address.setCityCode("430000");
		address.setAreaName("洪山区");
		address.setAreaCode("000000");
		address.setZip("");
		address.setAddress("");
		address.setPhone("");
		address.setTel("");
		address.setTag("");
		address.setIsDefault(1);
		Integer rows=addressMapper.insert(address);
		System.err.println(address);
		System.err.println("OK");
		System.err.println("rows:"+rows);
		
	}
	
	@Test
	public void count() {
		Integer uid=1;
		Integer count=addressMapper.countByUid(uid);
		System.err.println("count:"+count);
		System.err.println("OK");
	}
	
	@Test
	public void findByUid() {
		Integer uid=17;
		List<Address> list=addressMapper.findByUid(uid);
		for (Address address : list) {
			System.err.println(address);
		}
		System.err.println("OK");
		
	}
	
	@Test
	public void updateDefaultByAid() {
		Integer aid=1;
		String modifiedUser="默认管理员";
		Date modifiedTime=new Date();
		Integer rows=addressMapper.updateDefaultByAid(aid, modifiedUser, modifiedTime);
		System.err.println("rows:"+rows);
	}
	
	@Test
	public void updateNonDefaultByUid() {
		Integer uid=13;
		Integer rows=addressMapper.updateNonDefaultByUid(uid);
		System.err.println("rows:"+rows);
	}
	
	@Test
	public void findByAid() {
		Integer aid=13;
		Address address=addressMapper.findByAid(aid);
		System.err.println(address);
	}
	
	@Test
	public void deleteByAid() {
		Integer aid=34;
		Integer rows=addressMapper.deleteByAid(aid);
		System.err.println("rows:"+rows);
		
	}
	
	@Test
	public void findLastModifiedByUid() {
		Integer uid=17;
		Address address=addressMapper.findLastModifiedByUid(uid);
		System.err.println(address);
	}
	
	
	
	
	
}
