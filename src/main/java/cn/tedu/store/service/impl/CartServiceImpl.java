package cn.tedu.store.service.impl;
/**
 * 处理购物车数据的业务层实现类
 */
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.tedu.store.entity.Cart;
import cn.tedu.store.entity.Product;
import cn.tedu.store.mapper.CartMapper;
import cn.tedu.store.service.ICartService;
import cn.tedu.store.service.IProductService;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.CartNotFoundException;
import cn.tedu.store.service.ex.DeleteException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.vo.CartVO;

@Service
public class CartServiceImpl implements ICartService{

	@Autowired
	private CartMapper cartMapper;
	
	@Autowired
	private IProductService productService;
	
	@Override
	public void addToCart(Integer uid, String username, Integer pid, Integer amount) {
		// 创建当前时间对象
		Date now = new Date();
		// 根据uid和pid查询购物车数据
		Cart result = findByUidAndPid(uid, pid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：表示该用户尚未将该商品添加到购物车，则需要插入购物车数据
			// 调用productService.getById()方法查询商品数据
			Product product = productService.getById(pid);
			// 创建Cart对象
			Cart cart = new Cart();
			// 补全数据：uid, pid, num, price(从商品数据中获取)
			cart.setUid(uid);
			cart.setPid(pid);
			cart.setNum(amount);
			cart.setPrice(product.getPrice());
			// 补全数据：4个日志
			cart.setCreatedUser(username);
			cart.setCreatedTime(now);
			cart.setModifiedUser(username);
			cart.setModifiedTime(now);
			// 调用insert(cart)执行插入数据
			insert(cart);
		} else {
			// 否：表示该用户已经将该商品添加到购物车，则需要修改购物车数据中商品的数量
			// 从查询结果中获取cid
			Integer cid = result.getCid();
			// 从查询结果中获取num，与参数amount相加，得到新的数量
			Integer num = result.getNum() + amount;
			// 调用updateNumByCid(cid, num, modifiedUser, modifiedTime)方法执行修改数量
			updateNumByCid(cid, num, username, now);
		}
	}

	@Override
	public List<CartVO> getVOByUid(Integer uid){
		return findVOByUid(uid);
	}

	@Override
	public List<CartVO> getVOByCids(Integer[] cids, Integer uid) {
		List<CartVO> carts=findVOByCids(cids);
		//遍历查询结果，判断集合中元素是否归属当前用户，如果不是，则移除
		Iterator<CartVO> it=carts.iterator();
		while(it.hasNext()) {
			CartVO cart=it.next();
			if(!cart.getUid().equals(uid)) {
				it.remove();
			}
		}
		return carts;
	}
	
	
	@Override
	public Integer addNum(Integer cid, Integer uid, String username) {
		// 调用findByCid(cid)根据参数cid查询购物车数据
		Cart result = findByCid(cid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：抛出CartNotFoundException
			throw new CartNotFoundException(
				"尝试访问的购物车数据不存在");
		}

		// 判断查询结果中的uid与参数uid是否不一致
		if (!result.getUid().equals(uid)) {
			// 是：抛出AccessDeniedException
			throw new AccessDeniedException("非法访问");
		}

		// 可选：检查商品的数量是否大于多少(适用于增加数量)或小于多少(适用于减少数量)
		// 根据查询结果中的原数量增加1得到新的数量num
		Integer num = result.getNum() + 1;

		// 创建当前时间对象，作为modifiedTime
		Date now = new Date();
		// 调用updateNumByCid(cid, num, modifiedUser, modifiedTime)执行修改数量
		updateNumByCid(cid, num, username, now);
		
		// 返回新的数量
		return num;
	}

	
	@Override
	public void delete(Integer[] cids, Integer uid) {
		// 根据参数cids调用业务的getVOByCids()方法，得到List<CartVO>
		List<CartVO> carts = getVOByCids(cids, uid);
		if (carts.size() == 0) {
			return;
		}
		// 通过以上查询结果得到有效的、新的cids数组
		Integer[] newCids = new Integer[carts.size()]; 
		for (int i = 0; i < newCids.length; i++) {
			newCids[i] = carts.get(i).getCid();
		}
		// 根据新的cids调用持久层对象删除购物车数据	
		deleteByCids(newCids);
	}
	
	
	/**
	 * 插入购物车数据
	 * @param cart 购物车数据
	 */
	private void insert(Cart cart) {
		Integer rows = cartMapper.insert(cart);
		if (rows != 1) {
			throw new InsertException(
				"插入购物车数据时出现未知错误，请联系系统管理员");
		}
	}

	/**
	 * 修改购物车中商品的数量
	 * @param cid 购物车数据的id
	 * @param num 新的数量
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 */
	private void updateNumByCid(Integer cid, Integer num, 
		String modifiedUser, Date modifiedTime) {
		Integer rows = cartMapper.updateNumByCid(cid, num, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new UpdateException(
				"更新购物车中的商品数量时出现未知错误，请联系系统管理员");
		}
	}

	/**
	 * 根据用户id和商品id查询购物车数据
	 * @param uid 用户id
	 * @param pid 商品id
	 * @return 匹配的购物车数据，如果没有匹配的数据，则返回null
	 */
	private Cart findByUidAndPid(Integer uid, Integer pid) {
		return cartMapper.findByUidAndPid(uid, pid);
	}
	
	/**
	 * 查询某用户的购物车数据
	 * @param uid 用户的id
	 * @return 该用户的购物车数据列表
	 */
	private List<CartVO> findVOByUid(Integer uid) {
		return cartMapper.findVOByUid(uid);
	}

	/**
	 * 根据购物车数据id查询购物车详情
	 * @param cid 购物车数据id
	 * @return 匹配的购物车详情，如果没有匹配的数据，则返回null
	 */
	private Cart findByCid(Integer cid) {
		return cartMapper.findByCid(cid);
	}

	/**
	 * 根据多个购物车数据id查询多条购物车数据
	 * @param cids 多个购物车数据id
	 * @return 匹配的购物车数据的集合
	 */
	private List<CartVO> findVOByCids(Integer[] cids) {
		return cartMapper.findVOByCids(cids);
	}

	/**
	 * 根据购物车数据id，批量删除购物车中的数据
	 * @param cids 购物车数据id
	 */
	private void deleteByCids(Integer[] cids) {
		Integer rows = cartMapper.deleteByCids(cids);
		if (rows < 1) {
			throw new DeleteException(
				"删除购物车数据时出现未知错误，请联系系统管理员");
		}
	}
	
	
}

	
	
