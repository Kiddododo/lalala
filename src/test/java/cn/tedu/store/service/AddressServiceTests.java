package cn.tedu.store.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.tedu.store.entity.Address;
import cn.tedu.store.service.ex.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AddressServiceTests {

	@Autowired
	private IAddressService addressService;
	
	@Test
	public void insert() {
		try {
			Integer uid=1;
			String username="xx";
			Address address=new Address();
			address.setReceiver("基德");
			addressService.addnew(uid, username, address);
			System.err.println("OK");
		}catch(ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void getByUid() {
		 Integer uid=17;
		 List<Address> list=addressService.getByUid(uid);
		 System.err.println("count=" + list.size());
		for (Address item : list) {
			System.err.println(item);
		}
	}
	
	@Test
	public void setDefault() {
		try {
			Integer uid=18;
			Integer aid=38;
			String username="默认收货地址管理人";
			addressService.setDefault(aid, uid, username);
			System.err.println("OK");
		}catch(ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void delete() {
		try {
			Integer uid=12;     
			Integer aid=46;
			String username="删除执行人";
			addressService.delete(aid, uid, username);
			System.err.println("OK");
		}catch(ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void getByAid() {
		try {
			Integer uid = 12;
			Integer aid = 47;
			Address result = addressService.getByAid(aid, uid);
			System.err.println(result);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}
	
	
}
