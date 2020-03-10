package cn.tedu.store.service;

import java.util.List;

import javax.management.loading.PrivateClassLoader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import cn.tedu.store.entity.Product;
import cn.tedu.store.service.ex.ServiceException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductServiceTests {

	@Autowired
	private IProductService productService;
	
	@Test
	public void getHotList() {
		List<Product> list=productService.getHotList();
		System.err.println("count=" + list.size());
		for(Product product: list) {
		System.err.println(product);
		}
	}
	
	@Test
	public void getById() {
		try {
			Integer id = 10000001;
			Product result = productService.getById(id);
			System.err.println(result);
		} catch (ServiceException e) {
			System.err.println(e.getClass().getName());
			System.err.println(e.getMessage());
		}
	}
	
}
