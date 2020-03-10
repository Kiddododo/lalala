package cn.tedu.store.service;

import java.util.List;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import cn.tedu.store.entity.District;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DistrictServiceTests {

	@Autowired
	private IDistrictService districtService;
	
	@Test
	public void getParent() {
		String parent="420000";
		List<District> result=districtService.getByParent(parent);
		for (District district : result) {
			System.err.println(district);
		}
		
	}
	
	@Test
	public void getNameByCode() {
		String code="420000";
		String name=districtService.getNameByCode(code);
		System.err.println("name:"+name);
		
	}
	
}
