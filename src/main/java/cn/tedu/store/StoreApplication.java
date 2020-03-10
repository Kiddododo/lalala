package cn.tedu.store;

import javax.servlet.MultipartConfigElement;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.unit.DataSize;

@SpringBootApplication
@MapperScan("cn.tedu.store.mapper")//扫描整个路径下面的mapper类的文件
@Configuration
public class StoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(StoreApplication.class, args);
	}

	/**
	 * 调整文件上传的最大值，全局化配置
	 * @return
	 */
	@Bean//被Spring框架所管理，或者在application.properties中配置
	public MultipartConfigElement getMultipartConfigElement() {
		MultipartConfigFactory factory=new MultipartConfigFactory();
		factory.setMaxFileSize(DataSize.ofMegabytes(100));//文件最大值
		factory.setMaxRequestSize(DataSize.ofMegabytes(100));//调整本次请求中文件总共上传的最大值
		return factory.createMultipartConfig();
	}
	
	
}
