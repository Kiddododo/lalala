package cn.tedu.store.mapper;

import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.Cart;
import cn.tedu.store.vo.CartVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartMapperTests {

	@Autowired
	private CartMapper cartMapper;
	
	@Test
	public void insert() {
		Cart cart=new Cart();
		cart.setUid(1);
		cart.setPid(1);
		cart.setNum(99);
		cart.setPrice(100L);
		cart.setCreatedUser("峰不二子");
		cart.setCreatedTime(new Date());
		cart.setModifiedUser("峰不二子");
		cart.setModifiedTime(new Date());
		Integer rows=cartMapper.insert(cart);
		System.err.println("rows:"+rows);
		
	}
	
	@Test
	public void deleteByCids() {
		Integer[] cids= {1,2,3,4};
		Integer rows=cartMapper.deleteByCids(cids);
		System.err.println("rows="+rows);
	}
	
	@Test
	public void updateNumByCid() {
		Integer cid=17;
		Integer num=99;
		String modifiedUser="不二子";
		Date modifiedTime=new Date();
		Integer rows=cartMapper.updateNumByCid(cid, num, modifiedUser, modifiedTime);
		System.err.println("rows:"+rows);
	}
	
	@Test
	public void findByCid() {
		Integer cid=24;
		Cart result=cartMapper.findByCid(cid);
		System.err.println(result);
	}
	
	@Test
	public void findByUidAndPid() {
		Integer uid=1;
		Integer pid=1;
		Cart cart=cartMapper.findByUidAndPid(uid, pid);
		System.err.println(cart);
	}
	
	@Test
	public void findVOByUid() {
		Integer uid=1;
		List<CartVO> list=cartMapper.findVOByUid(uid);
		System.err.println("count="+list.size());
		for(CartVO item:list) {
			System.err.println(item);
		}
	}
	
	@Test
	public void findVOByCids() {
		Integer[] cids= { 9, 10, 11, 800, 900 };
		List<CartVO> list=cartMapper.findVOByCids(cids);
		System.err.println("count="+list.size());
		for(CartVO item:list) {
			System.err.println(item);
		}
	}
	
	
}
