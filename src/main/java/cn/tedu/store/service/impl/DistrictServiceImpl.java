package cn.tedu.store.service.impl;
/**
 * 处理省/市/区数据的业务层实现类
 */
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import cn.tedu.store.entity.District;
import cn.tedu.store.mapper.DistrictMapper;
import cn.tedu.store.service.IDistrictService;

@Service//该类交给Spring管理
public class DistrictServiceImpl implements IDistrictService{

	@Autowired
	private DistrictMapper districtMapper;
	
	@Override
	public List<District> getByParent(String parent) {
		//调用持久层对象的查询，得到列表
		List<District> list=districtMapper.findByParent(parent);
		//遍历查询得到的列表
		for(District district:list) {
			System.err.println(district);
			//将列表的id和parent设置为null
			district.setId(null);
			district.setParent(null);
		}
		//返回列表
		return list;
	}
	
	@Override
	public String getNameByCode(String code) {
		String result=districtMapper.findNameByCode(code);
		return result;
	}
	
	
	
	
}
