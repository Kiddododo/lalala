package cn.tedu.store.mapper;
/**
 * 处理购物车数据的持久层接口
 */
import java.util.Date;
import java.util.List;
import org.apache.ibatis.annotations.Param;
import cn.tedu.store.entity.Cart;
import cn.tedu.store.vo.CartVO;

public interface CartMapper {

	/**
	 * 插入购物车数据
	 * @param cart 购物车数据
	 * @return 受影响的行数
	 */
	Integer insert(Cart cart);
	
	/**
	 * 根据购物车数据id，批量删除购物车中的数据
	 * @param cids 购物车数据id
	 * @return 受影响的行数
	 */
	Integer deleteByCids(Integer[] cids);
	
	/**
	 * 修改购物车数据中商品的数量
	 * @param cid 购物车数据id
	 * @param num 新的数量
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 * @return 受影响的行数
	 */
	Integer updateNumByCid(
	    @Param("cid") Integer cid, 
	    @Param("num") Integer num, 
	    @Param("modifiedUser") String modifiedUser, 
	    @Param("modifiedTime") Date modifiedTime);

	/**
	 * 根据购物车数据id查询购物车详情
	 * @param cid 购物车数据id
	 * @return 匹配的购物车详情，如果没有匹配的数据，则返回null
	 */
	Cart findByCid(Integer cid);
	
	/**
	 * 根据用户id与商品id查询购物车数据
	 * @param uid 用户id
	 * @param pid 商品id
	 * @return 匹配的购物车数据，如果没有匹配的数据，则返回null
	 */
	Cart findByUidAndPid(
	    @Param("uid") Integer uid, 
	    @Param("pid") Integer pid);
	
	/**
	 * 查询某用户的购物车数据的列表
	 * @param uid 用户id
	 * @return 该用户的购物车数据的列表
	 */
	List<CartVO> findVOByUid(Integer uid);
	
	/**
	 * 根据多个数据id查询购物车数据的列表
	 * @param cids 多个购物车数据id
	 * @return 匹配的购物车数据的列表
	 */
	List<CartVO> findVOByCids(Integer[] cids);

	
	
	
}
