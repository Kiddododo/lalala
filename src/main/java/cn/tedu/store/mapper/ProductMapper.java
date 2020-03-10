package cn.tedu.store.mapper;
/**
 * 处理商品数据的持久层接口
 */
import java.util.List;
import cn.tedu.store.entity.Product;

public interface ProductMapper {

	/**
	 * 查询热销商品
	 * @return 热销商品列表
	 */
	List<Product> findHotList();
	
	/**
	 * 根据商品id查询商品详情
	 * @param id 商品id
	 * @return 匹配的商品详情，如果没有匹配的数据，则返回null
	 */
	Product findById(Integer id);


	
}
