package cn.tedu.store.mapper;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.Product;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductMapperTests {

	@Autowired
	private ProductMapper productMapper;
	
	@Test
	public void findHotList() {
		
		List<Product> product=productMapper.findHotList();
		for (Product products : product) {
			
			System.err.println(products);
		}
		
	}
	
	@Test
	public void findById() {
		Integer id = 10000001;
		Product result = productMapper.findById(id);
		System.err.println(result);
	}
	
	
	
}
