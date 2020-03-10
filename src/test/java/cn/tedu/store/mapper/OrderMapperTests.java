package cn.tedu.store.mapper;

import java.sql.Date;

import org.apache.catalina.mapper.Mapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.Order;
import cn.tedu.store.entity.OrderItem;

@RunWith(SpringRunner.class)
@SpringBootTest
public class OrderMapperTests {

	@Autowired
	private OrderMapper orderMapper;
	
	@Test
	public void insertOrder() {
		Order order=new Order();
		order.setOid(2);
		order.setUid(12);
		order.setRecvName("鲁邦三世");
		order.setRecvPhone("12345678901");
		order.setRecvProvince("北京");
		order.setRecvCity(null);
		order.setRecvArea(null);
		order.setRecvAddress(null);
		order.setStatus(0);
		Integer rows=orderMapper.insertOrder(order);
		System.err.println("rows="+rows);
		
	}
	
	@Test
	public void insertOrderItem() {
		OrderItem orderItem=new OrderItem();
		orderItem.setOid(1);
		orderItem.setPid(2);
		Integer rows=orderMapper.insertOrderItem(orderItem);
		System.err.println("rows=" + rows);
		
	}
	
}
