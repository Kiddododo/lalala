package cn.tedu.store.service.impl;
/**
 * 处理收货地址数据的业务层实现类
 */
/**
 * 关于事务(Transaction)
事务是数据库领域中，能保证同一个业务中需要执行的多次增删改操作全部成功或全部失败的机制
在基于SpringJDBC的开发模式下，对业务方法添加`@Transactional`注解，
即可表示“该业务方法中将执行的多次增删改操作将以事务的方式执行”，可以达到“全部执行成功”或“全部执行失败”的效果
框架在处理时，大致是：
	开启事务：begin
	try {
		执行若干次增删改操作
		提交事务：commit
	} catch(RuntimeException ex) {
		回滚事务：rollback
	}
所以，在编写代码时，必须做到：
1. 凡执行增、删、改操作，应该及时获取返回的受影响的行数，并判断受影响的行数是否与预期值相符，
如果不相符，应该抛出`RuntimeException`或其子孙类异常；
2. 如果某个业务涉及超过1次的增、删、改操作(例如2次Update操作，或1次Insert加1次Delete操作)，
则必须在业务方法之前添加`@Transactional`注解；
另外，`@Transactional`注解也可以添加在业务类的声明之前，则整个类的所有方法在执行时都是有事务保障的，
但是，并不推荐这样使用
关于事务，还应该了解它的**ACID特性**、**事务的传播**、**事务的隔离**。
 */
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import cn.tedu.store.entity.Address;
import cn.tedu.store.mapper.AddressMapper;
import cn.tedu.store.service.IAddressService;
import cn.tedu.store.service.IDistrictService;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.AddressCountLimitException;
import cn.tedu.store.service.ex.AddressNotFoundException;
import cn.tedu.store.service.ex.DeleteException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.UpdateException;

@Service
public class AddressServiceImpl implements IAddressService{

	@Autowired
	private AddressMapper addressMapper;

	@Autowired
	private IDistrictService districtService;

	@Value("${project.address-max-count}")
	private int addressMaxCount;

	@Override
	public void addnew(Integer uid, String username, Address address) {
		//根据uid统计该用户的收货地址
		Integer count=addressMapper.countByUid(uid);
		//判断数量是否超出设置值addressMaxCount
		if(count>=addressMaxCount) {
			throw new AddressCountLimitException("增加收货地址失败！收货地址数量已经达到上限("+ addressMaxCount + ")！");
			//是：抛出AddressCountLimitException
		}
		// 根据以上统计的数量是否为0，得到isDefault值
		Integer isDefault=count==0?1:0;
		//创建当前时间对象now
		Date now=new Date();
		//补全数据，省
		String provinceCode=address.getProvinceCode();
		String provinceName=districtService.getNameByCode(provinceCode);
		address.setProvinceName(provinceName);
		//补全数据，市
		String cityCode=address.getCityCode();
		String cityName=districtService.getNameByCode(cityCode);
		address.setCityName(cityName);
		//补全数据，区
		String areaCode=address.getAreaCode();
		String areaName=districtService.getNameByCode(areaCode);
		address.setAreaName(areaName);
		//补全数据：uid
		address.setUid(uid);
		//补全数据：is_default
		address.setIsDefault(isDefault);
		//4项日志(username,now)
		address.setCreatedUser(username);
		address.setCreatedTime(now);
		address.setModifiedUser(username);
		address.setModifiedTime(now);
		//执行插入用户数据
		insert(address);
		
	}

	
	@Override
	@Transactional
	public void setDefault(Integer aid, Integer uid, String username) {
		// 根据参数aid查询收货地址数据
		Address result = addressMapper.findByAid(aid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：AddressNotFoundException
			throw new AddressNotFoundException(
				"设置默认收货地址失败！尝试访问的数据不存在！");
		}
		// 判断查询结果中的uid与参数uid是否不一致
		if (!result.getUid().equals(uid)) {
			// 是：AccessDeniedException
			throw new AccessDeniedException(
				"设置默认收货地址失败！非法访问已经被拒绝！");
		}
		// 将该用户的所有收货地址设置为非默认，并获取返回值
		Integer rows = addressMapper.updateNonDefaultByUid(uid);
		// 判断返回值是否小于1
		if (rows < 1) {
			// 是：UpdateException
			throw new UpdateException(
				"设置默认收货地址失败[1]！更新收货地址数据时出现未知错误，请联系系统管理员！");
		}
		// 将指定的收货地址设置为默认
		updateDefaultByAid(aid, username, new Date());
	}
	
	
	@Override
	@Transactional
	public void delete(Integer aid, Integer uid, String username) {
		// 根据参数aid查询收货地址数据
		Address result = addressMapper.findByAid(aid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：AddressNotFoundException
			throw new AddressNotFoundException(
				"设置默认收货地址失败！尝试访问的数据不存在！");
		}
		// 判断查询结果中的uid与参数uid是否不一致
		// 注意：对比Integer类型的值时，如果值的范围在 -128~127 之间，使用==或!=或equals()均可，如果超出这个范围，必须使用equals()
		if (!result.getUid().equals(uid)) {
			// 是：AccessDeniedException
			throw new AccessDeniedException(
				"设置默认收货地址失败！非法访问已经被拒绝！");
		}
		// 执行删除
		deleteByAid(aid);
		// 判断查询结果(对应刚刚删除的数据)中的isDefault是否不为1
		if (result.getIsDefault() != 1) {
			return;
		}
		// 统计当前用户还有多少收货地址
		Integer count = addressMapper.countByUid(uid);
		// 判断统计结果是否为0
		if (count == 0) {
			return;
		}
		// 查询当前用户的最后修改的那一条收货地址
		Address address = addressMapper.findLastModifiedByUid(uid);
		// 从本次查询中取出数据的aid
		Integer lastModifiedAid = address.getAid();
		// 执行设置默认收货地址
		updateDefaultByAid(lastModifiedAid, username, new Date());
	}
	
	
	@Override
	public Address getByAid(Integer aid, Integer uid) {
		// 根据参数aid查询收货地址数据
		Address result = findByAid(aid);
		// 判断查询结果是否为null
		if (result == null) {
			// 是：AddressNotFoundException
			throw new AddressNotFoundException(
				"查询收货地址失败！尝试访问的数据不存在！");
		}

		// 判断查询结果中的uid与参数uid是否不一致
		if (!result.getUid().equals(uid)) {
			// 是：AccessDeniedException
			throw new AccessDeniedException(
				"查询收货地址失败！非法访问已经被拒绝！");
		}
		
		// 将查询结果中的某些属性设置为null
		result.setUid(null);
		result.setProvinceCode(null);
		result.setCityCode(null);
		result.setAreaCode(null);
		result.setIsDefault(null);
		result.setCreatedUser(null);
		result.setCreatedTime(null);
		result.setModifiedUser(null);
		result.setModifiedTime(null);
		
		// 返回查询结果
		return result;
	}
	
	
	@Override
	public List<Address> getByUid(Integer uid) {
		List<Address> list = findByUid(uid);
		for (Address address : list) {
			address.setUid(null);
			address.setProvinceCode(null);
			address.setCityCode(null);
			address.setAreaCode(null);
			address.setIsDefault(null);
			address.setCreatedUser(null);
			address.setCreatedTime(null);
			address.setModifiedUser(null);
			address.setModifiedTime(null);
		}
		return list;
	}
	
	
	
	/**
	 * 插入收货地址数据
	 * @param address 收货地址数据
	 */
	private void insert(Address address) {
		Integer rows = addressMapper.insert(address);
		if (rows != 1) {
			throw new InsertException(
				"插入收货地址数据时出现未知错误，请联系系统管理员！");
		}
	}
	
	/**
	 * 根据收货地址id删除数据
	 * @param aid 收货地址id
	 */
	private void deleteByAid(Integer aid) {
		Integer rows = addressMapper.deleteByAid(aid);
		if (rows != 1) {
			throw new DeleteException(
				"删除收货地址失败！删除收货地址数据时出现未知错误，请联系系统管理员！");
		}
	}
	
	/**
	 * 将指定的收货地址设置为默认
	 * @param aid 收货地址的id
	 * @param modifiedUser 修改执行人
	 * @param modifiedTime 修改时间
	 */
	private void updateDefaultByAid(Integer aid, String modifiedUser, Date modifiedTime) {
		Integer rows = addressMapper.updateDefaultByAid(aid, modifiedUser, modifiedTime);
		if (rows != 1) {
			throw new UpdateException("设置默认收货地址时出现未知错误，请联系系统管理员！");
		}
	}
	
	/**
	 * 将某用户的收货地址全部设置为非默认
	 * @param uid 用户id
	 */
	private void updateNonDefaultByUid(Integer uid) {
		Integer rows = addressMapper.updateNonDefaultByUid(uid);
		if (rows < 1) {
			throw new UpdateException("设置默认收货地址时出现未知错误，请联系系统管理员！");
		}
	}

	/**
	 * 统计某用户的收货地址数据的数量
	 * @param uid 用户的id
	 * @return 该用户的收货地址数据的数量
	 */
	private Integer countByUid(Integer uid) {
		return addressMapper.countByUid(uid);
	}
	
	/**
	 * 根据收货地址id查询收货地址详情
	 * @param aid 收货地址id
	 * @return 匹配的收货地址详情，如果没有匹配的数据，则返回null
	 */
	private Address findByAid(Integer aid) {
		return addressMapper.findByAid(aid);
	}
	
	/**
	 * 查询某用户最后修改的收货地址
	 * @param uid 用户的id
	 * @return 该用户最后修改的收货地址，如果该用户没有收货地址，则返回null
	 */
	private Address findLastModifiedByUid(Integer uid) {
		return addressMapper.findLastModifiedByUid(uid);
	}
	
	/**
	 * 查询某用户的收货地址列表
	 * @param uid 用户的id
	 * @return 该用户的收货地址列表
	 */
	private List<Address> findByUid(Integer uid) {
		return addressMapper.findByUid(uid);
	}


	

}
