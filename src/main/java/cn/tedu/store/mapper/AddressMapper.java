package cn.tedu.store.mapper;
import java.util.Date;
/**
 * 处理收货地址数据的持久层接口
 */
import java.util.List;
import org.apache.ibatis.annotations.Param;
import cn.tedu.store.entity.Address;

public interface AddressMapper {

	/**
	 * 插入收货地址数据
	 * @param address 收货地址数据
	 * @return 受影响的行数
	 */
	Integer insert(Address address);
	
	/**
	 * 根据收货地址id删除数据
	 * @param aid 收货地址id
	 * @return 受影响的行数
	 */
	Integer deleteByAid(Integer aid);
	
	/**
	 * 将指定的收货地址设置为默认
	 * @param aid 收货地址的id
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 * @return 受影响的行数
	 */
	Integer updateDefaultByAid(
			@Param("aid") Integer aid, 
			@Param("modifiedUser") String modifiedUser, 
			@Param("modifiedTime") Date modifiedTime);

	/**
	 * 将某用户的收货地址全部设置为非默认
	 * @param uid 用户id
	 * @return 受影响的行数
	 */
	Integer updateNonDefaultByUid(Integer uid);

	/**
	 * 统计某用户的收货地址数据的数量
	 * @param uid 用户的id
	 * @return 该用户的收货地址数据的数量
	 */
	Integer countByUid(Integer uid);
	
	/**
	 * 根据收货地址id查询收货地址详情
	 * @param aid 收货地址id
	 * @return 匹配的收货地址详情，如果没有匹配的数据，则返回null
	 */
	Address findByAid(Integer aid);
	
	/**
	 * 查询某用户最后修改的收货地址
	 * @param uid 用户的id
	 * @return 该用户最后修改的收货地址，如果该用户没有收货地址，则返回null
	 */
	Address findLastModifiedByUid(Integer uid);
	
	/**
	 * 查询某用户的收货地址列表
	 * @param uid 用户的id
	 * @return 该用户的收货地址列表
	 */
	List<Address> findByUid(Integer uid);
	
	
	
}
