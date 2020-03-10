package cn.tedu.store.service.impl;
/**
 * 处理订单及订单商品数据的业务层实现类
 */
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.tedu.store.entity.Address;
import cn.tedu.store.entity.Order;
import cn.tedu.store.entity.OrderItem;
import cn.tedu.store.mapper.OrderMapper;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.IOrderService;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.vo.CartVO;

@Service
public class OrderServiceImpl implements IOrderService{

	@Autowired private OrderMapper orderMapper;
	@Autowired private IAddressService addressService;
	@Autowired private ICartService cartService;

	
	@Override
	public Order create(Integer aid, Integer[] cids, Integer uid, String username) {
		// 创建当前时间对象now
		Date now = new Date();
		
		// 根据参数aid调用addressService的Address getByAid(Integer aid)方法查询收货地址数据
		Address address = addressService.getByAid(aid, uid);

		// 根据参数cids调用cartService的List<CartVO> getVOByCids(Integer[] cids, Integer uid)方法查询购物车中的数据，得到List<CartVO>类型的结果
		List<CartVO> carts = cartService.getVOByCids(cids, uid);
		// 声明totalPrice表示总价
		Long totalPrice = 0L;
		// 遍历以上查询结果，在遍历过程中，累加商品单价乘以数量的结果
		for (CartVO cart : carts) {
			totalPrice += cart.getRealPrice() * cart.getNum();
		}

		// 创建Order对象
		Order order = new Order();
		// 补全属性：uid
		order.setUid(uid);
		// 补全属性：收货地址相关的6个属性
		order.setRecvName(address.getReceiver());
		order.setRecvPhone(address.getPhone());
		order.setRecvProvince(address.getProvinceName());
		order.setRecvCity(address.getCityName());
		order.setRecvArea(address.getAreaName());
		order.setRecvAddress(address.getAddress());
		// 补全属性：price -> totalPrice
		order.setPrice(totalPrice);
		// 补全属性：order_time -> now
		order.setOrderTime(now);
		// 补全属性：pay_time -> null
		order.setPayTime(null);
		// 补全属性：status -> 0
		order.setStatus(0);
		// 补全属性：4个日志
		order.setCreatedUser(username);
		order.setCreatedTime(now);
		order.setModifiedUser(username);
		order.setModifiedTime(now);
		// 插入订单数据：insertOrder(order);
		insertOrder(order);

		// 遍历查询到的List<CartVO>对象
		for (CartVO cart : carts) {
			// 创建OrderItem对象
			OrderItem item = new OrderItem();
			// 补全属性：oid -> order.getOid();
			item.setOid(order.getOid());
			// 补全属性：pid, title, image, price, num -> CartVO中的属性
			item.setPid(cart.getPid());
			item.setTitle(cart.getTitle());
			item.setImage(cart.getImage());
			item.setPrice(cart.getRealPrice());
			item.setNum(cart.getNum());
			// 补全属性：4个日志
			item.setCreatedUser(username);
			item.setCreatedTime(now);
			item.setModifiedUser(username);
			item.setModifiedTime(now);
			// 插入若干条订单商品数据：insertOrderItem(orderItem)
			insertOrderItem(item);
		}
		
		// 返回
		return order;
	}
	
	/**
	 * 插入订单数据
	 * @param order 订单数据
	 */
	private void insertOrder(Order order) {
		Integer rows = orderMapper.insertOrder(order);
		if (rows != 1) {
			throw new InsertException(
				"创建订单失败！插入订单数据时出现未知错误，请联系系统管理员！");
		}
	}

	/**
	 * 插入订单商品数据
	 * @param orderItem 订单商品数据
	 */
	private void insertOrderItem(OrderItem orderItem) {
		Integer rows = orderMapper.insertOrderItem(orderItem);
		if (rows != 1) {
			throw new InsertException(
				"创建订单失败！插入订单商品数据时出现未知错误，请联系系统管理员！");
		}
	}

}