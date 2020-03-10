package cn.tedu.store.service;
/**
 * 处理商品数据的业务层接口
 */
import java.util.List;
import cn.tedu.store.entity.Product;

public interface IProductService {

	/**
	 * 根据商品id查询商品详情
	 * @param id 商品id
	 * @return 匹配的商品详情
	 */
	Product getById(Integer id);
	
	/**
	 * 查询热销商品
	 * @return 热销商品列表
	 */
	List<Product> getHotList();
	
	
	
}
