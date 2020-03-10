package cn.tedu.store.controller;

/**
 * 处理订单相关请求的控制器类
 */

import java.util.Arrays;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cn.tedu.store.entity.Order;
import cn.tedu.store.service.IOrderService;
import cn.tedu.store.utils.JsonResult;
@RestController
@RequestMapping("orders")
public class OrderController extends BaseController {

	@Autowired
	private IOrderService orderService;
	
	//http://localhost:8080/orders/create?aid=26&cids=8&cids=9&cids=10
	@RequestMapping("create")
	public JsonResult<Order> create(Integer aid, Integer[] cids, HttpSession session) {
		System.err.println("OrderController.create()");
		System.err.println("\taid=" + aid);
		System.err.println("\tcids=" + Arrays.deepToString(cids));
		Integer uid = getUidFromSession(session);
		String username = getUsernameFromSession(session);
		Order data = orderService.create(aid, cids, uid, username);
		return new JsonResult<>(OK, data);
	}
	
}
