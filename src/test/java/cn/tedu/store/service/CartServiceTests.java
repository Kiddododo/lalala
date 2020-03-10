package cn.tedu.store.service;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.mapper.CartMapper;
import cn.tedu.store.service.ex.ServiceException;
import cn.tedu.store.vo.CartVO;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CartServiceTests {

	@Autowired
	private ICartService cartService;
	
	@Test
	public void addToCart() {
		try {
			Integer uid = 1;
			Integer pid = 10000002;
			Integer amount = 30;
			String username = "次元大介";
			cartService.addToCart(uid, username, pid, amount);
			System.err.println("OK");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void getVOByUid() {
		Integer uid=12;
		List<CartVO> list=cartService.getVOByUid(uid);
		System.err.println("count="+list.size());
		for(CartVO item:list) {
			System.err.println(item);
		}
	}
	
	@Test
	public void getVOByCids() {
		Integer[] cids= {23,24,25,26};
		Integer uid=12;
		List<CartVO> list=cartService.getVOByCids(cids,uid);
		System.err.println("count="+list.size());
		for(CartVO item:list) {
			System.err.println(item);
		}
	}
	
	@Test
	public void addNum() {
		try {
			Integer cid = 23;
			Integer uid = 12;
			String username = "不知道";
			Integer num = cartService.addNum(cid, uid, username);
			System.err.println("OK. New num=" + num);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	
	@Test
	public void delete() {
		try {
			Integer[] cids = {23};
			Integer uid = 12;
			cartService.delete(cids, uid);
			System.err.println("OK.");
		} catch (ServiceException e) {
			System.err.println(e.getClass().getSimpleName());
			System.err.println(e.getMessage());
		}
	}
	
	
}
