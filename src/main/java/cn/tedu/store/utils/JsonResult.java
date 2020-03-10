package cn.tedu.store.utils;
/**
 * 用于封装向客户端的响应结果的类
 * @author soft01
 * @param <T> 向客户端响应的数据类型
 */
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

//@JsonInclude(value=Include.NON_NULL)
public class JsonResult<T> {
	//占位符：随便取，但是不能与已经识别的名称一致
	private Integer state;//状态
	private String message;//错误信息
	//@JsonInclude(value=Include.NON_NULL)//浏览器不显示
	private T data;//数据
	
	//获得创建对象重载附带的参数
	public JsonResult() {
		super();
	}
	
	public JsonResult(Integer state) {
		super();
		this.state=state;
	}
	
	public JsonResult(String message) {
		super();
		this.message = message;
	}

	public JsonResult(Throwable ex) {
		super();
		this.message =ex.getMessage();
	}

	public JsonResult(Integer state, T data) {
		super();
		this.state = state;
		this.data = data;
	}

	//获取私有属性值
	public Integer getState() {
		return state;
	}
	public void setState(Integer state) {
		this.state = state;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	
	@Override
	public String toString() {
		return "JsonResult [state=" + state + ", message=" + message + ", data=" + data + "]";
	}
	
	
}
