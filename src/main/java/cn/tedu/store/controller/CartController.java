package cn.tedu.store.controller;
/**
 * 处理购物车相关请求的控制器类
 */

import java.util.List;

import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.mapper.CartMapper;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.utils.JsonResult;
import cn.tedu.store.vo.CartVO;

@RestController
@RequestMapping("carts")
public class CartController extends BaseController {

	@Autowired
	private ICartService cartService;
	
	//http://localhost:8080/carts/add_to_cart?pid=10000005&amount=3
	@RequestMapping("add_to_cart")
	public JsonResult<Void> addToCart(Integer pid, Integer amount, HttpSession session) {
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		cartService.addToCart(uid, username, pid, amount);
		return new JsonResult<>(OK);
	}
	
	//http://localhost:8080/carts/
	@GetMapping({"", "/"})
	public JsonResult<List<CartVO>> getVOByUid(HttpSession session){
		Integer uid=getUidFromSession(session);
		List<CartVO> data=cartService.getVOByUid(uid);
		return new JsonResult<>(OK,data);
	}
	
	//http://localhost:8080/carts/get_by_cids?cids=6&
	//cids=7&cids=8&cids=9&cids=10&cids=11&cids=12&cids=13&cids=14&cids=15
	@GetMapping("get_by_cids")
	public JsonResult<List<CartVO>> getVOByCids(Integer[] cids,HttpSession session){
		Integer uid=getUidFromSession(session);
		List<CartVO> data=cartService.getVOByCids(cids, uid);
		return new JsonResult<>(OK,data);
	}
	
	//http://localhost:8080/carts/10/num/add
	@RequestMapping("{cid}/num/add")
	public JsonResult<Integer> addNum(@PathVariable("cid") Integer cid, HttpSession session) {
		// 从Session中获取uid和username
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		// 调用业务对象执行增加数量
		Integer data = cartService.addNum(cid, uid, username);
		// 返回成功
		return new JsonResult<>(OK, data);
	}

	
}