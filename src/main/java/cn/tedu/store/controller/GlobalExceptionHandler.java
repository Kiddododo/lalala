package cn.tedu.store.controller;
/**
 * 用于处理所有异常
 */
/**
使用SpringMVC框架统一处理异常【补充】
添加了`@ExceptionHandler`注解的处理异常的方法，只能作用于当前控制器类，也就是当前控制器中处理请求时出现的异常才可以被处理，
其它控制器处理请求时出现的异常并不会被处理，可以将处理异常的方法写在控制器类的基类中，则每个控制器都相当于有这个方法，就可以统一处理异常了，
这个基类的声明必须使用`public`权限修饰，而并不可以使用默认的访问权限，另外，这个基类可以声明为抽象，
另外，也可以将处理异常的类添加`@ControllerAdvice`注解，并在处理异常的方法之前添加`@ResponseBody`注解，
或者，直接在类之前添加`@RestControllerAdvice`并在方法之前不添加`@ResponseBody`注解，
就可以使得当前类是SpringMVC框架的全局化的类，当前类中处理异常的做法也是全局化的，
所以，无论是哪个控制器类的方法处理异常时出现异常，都会由这个全局化类中的处理方式进行处理
注意：@ControllerAdvice/@RestControllerAdvice这2个注解在SpringMVC框架中默认不可用，必须另外配置，在SpringBoot框架中是可以直接使用的
关于统一处理异常时，使用的`@ExceptionHandler`注解，还可以配置注解参数，例如配置为：
@ExceptionHandler(ServiceException.class)
则表示只有`ServiceException`及其子孙类异常才会被接下来的方法进行处理，而其它类型的异常是不被接下的方法处理的该注解有参数是数组类型的，
也可以使用大括号`{}`框住多个异常的类型
 */
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import cn.tedu.store.controller.ex.FileEmptyException;
import cn.tedu.store.controller.ex.FileSizeException;
import cn.tedu.store.controller.ex.FileTypeException;
import cn.tedu.store.controller.ex.FileUploadException;
import cn.tedu.store.controller.ex.FileUploadIOException;
import cn.tedu.store.controller.ex.FileUploadStateException;
import cn.tedu.store.service.ex.AccessDeniedException;
import cn.tedu.store.service.ex.AddressCountLimitException;
import cn.tedu.store.service.ex.AddressNotFoundException;
import cn.tedu.store.service.ex.DeleteException;
import cn.tedu.store.service.ex.InsertException;
import cn.tedu.store.service.ex.PasswordNotMatchException;
import cn.tedu.store.service.ex.ProductNotFoundException;
import cn.tedu.store.service.ex.ServiceException;
import cn.tedu.store.service.ex.UpdateException;
import cn.tedu.store.service.ex.UserNotFoundException;
import cn.tedu.store.service.ex.UsernameDuplicateException;
import cn.tedu.store.utils.JsonResult;

//@RestControllerAdvice=@ControllerAdvice类前+@ResponseBody方法前
@RestControllerAdvice
public class GlobalExceptionHandler {
	//@ExceptionHandler处理异常的方法，只能作用于当前控制器类，该类及其子孙类都可以被处理
	@ExceptionHandler({ServiceException.class,FileUploadException.class})
	public JsonResult<Void> handleException(Throwable ex){
		JsonResult<Void> jsonResult=new JsonResult<Void>(ex);
		
		if (ex instanceof UsernameDuplicateException) {
			jsonResult.setState(4000);
		} else if (ex instanceof UserNotFoundException) {
			jsonResult.setState(4001);
		} else if (ex instanceof PasswordNotMatchException) {
			jsonResult.setState(4002);
		} else if (ex instanceof AddressCountLimitException) {
			jsonResult.setState(4003);
		} else if (ex instanceof AddressNotFoundException) {
			jsonResult.setState(4004);
		} else if (ex instanceof AccessDeniedException) {
			jsonResult.setState(4005);
		} else if (ex instanceof ProductNotFoundException) {
			jsonResult.setState(4006);
		} else if (ex instanceof InsertException) {
			jsonResult.setState(5000);
		} else if (ex instanceof UpdateException) {
			jsonResult.setState(5001);
		} else if (ex instanceof DeleteException) {
			jsonResult.setState(5002);
		} else if (ex instanceof FileEmptyException) {
			jsonResult.setState(6000);
		} else if (ex instanceof FileSizeException) {
			jsonResult.setState(6001);
		} else if (ex instanceof FileTypeException) {
			jsonResult.setState(6002);
		} else if (ex instanceof FileUploadStateException) {
			jsonResult.setState(6003);
		} else if (ex instanceof FileUploadIOException) {
			jsonResult.setState(6004);
		}
		
		return jsonResult;
	}
	
}