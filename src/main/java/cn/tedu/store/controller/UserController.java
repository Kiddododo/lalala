package cn.tedu.store.controller;
/**
 * static：唯一，常驻内存，正在执行的程序和数据必须在内存中
 * static修饰类的成员：属性，方法，内部类，内部接口，enum枚举
 * final修饰的量只能赋值一次，即使是相同的值也不能行
 * final修饰全局变量必须赋值，因为它有默认值0，null
 * int,Integer都行，建议统一使用Integer
 */
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import cn.tedu.store.controller.ex.FileEmptyException;
import cn.tedu.store.controller.ex.FileSizeException;
import cn.tedu.store.controller.ex.FileTypeException;
import cn.tedu.store.controller.ex.FileUploadIOException;
import cn.tedu.store.entity.User;
import cn.tedu.store.service.IUserService;
import cn.tedu.store.utils.JsonResult;

//@RestController=@Controller该类交给Spring管理+@ResponseBody返回值的类型转换为json类型的字符串
@RestController//使得当前类是Spring框架的全局化的类
@RequestMapping("users")//请求路径，通过上下文或者配置文件拼接前后缀
public class UserController extends BaseController{

	@Autowired//注入属性
	private IUserService userService;
	
	
	//http://localhost:8080/users/reg?username=工藤新一&password=123456&gender=1&phone=12345678901&email=abc@qq.com
	@RequestMapping("reg")
	public JsonResult<Void> reg(User user){
		//JsonResult<Void> jsonResult=new JsonResult<Void>(1);
			userService.reg(user);
			/*jsonResult.setState(1);
			jsonResult.setMessage("注册成功");*/
			return new JsonResult<>(OK);
		}
		
	//http://localhost:8080/users/login?username=root&password=1234
	@RequestMapping("login")
	public JsonResult<User> login(String username,String password,HttpSession session){
		User data=userService.login(username,password);
		session.setAttribute("uid", data.getUid());
		session.setAttribute("username", data.getUsername());//不加双引号就是变量，加了就是一个参数
		System.out.println("session:"+session);
		return new JsonResult<>(OK,data);
	}
	
	//http://localhost:8080/users/password/change?oldPassword=0000&newPassword=1234
	@RequestMapping("password/change")
	public JsonResult<Void> change(String oldPassword,String newPassword,HttpSession session){
		//从参数session取出uid和username
		//Integer uid=Integer.valueOf(session.getAttribute("uid").toString());
		//String username=session.getAttribute("username").toString();
		Integer uid = getUidFromSession(session);
	   String username = getUsernameFromSession(session);
		System.out.println("session:"+session);
		//调用业务对象的方法执行修改密码
		userService.changePassword(uid, oldPassword, newPassword, username);
		return new JsonResult<>(OK);
	}
	
	//http://localhost:8080/users/info/show
	@GetMapping("info/show")
	public JsonResult<User> showInfo(HttpSession session){
		//从session中获取参数
		//Integer uid=Integer.valueOf(session.getAttribute("uid").toString());
		Integer uid = getUidFromSession(session);
		//调用业务对象的showInfo()方法查询用户数据
		User data=userService.showInfo(uid);
		//返回OK与以上调用时的返回结果
		return new JsonResult<>(OK,data);
	}
	
	//http://localhost:8080/users/info/change?phone=12345678901&emali=123456@qq.com&gender=1
		@RequestMapping("info/change")
		public JsonResult<User> changeInfo(User user,HttpSession session){
			//从session中获取uid和username
			Integer uid = getUidFromSession(session);
			String username = getUsernameFromSession(session);
			//调用业务对象执行修改
			userService.changeInfo(uid,username,user);
			//返回OK与以上调用时的返回结果
			return new JsonResult<>(OK);
		}
		
		/**
		 * 允许上传的文件大小的上限值，以字节为单位
		 */
		private static final long AVATAR_MAX_SIZE=101*1024;
		
		/**
		 * 允许上传的文件类型的集合
		 */
		private static final ArrayList<String> AVATAR_TYPES=new ArrayList<String>();
		
		static {
			AVATAR_TYPES.add("image/jpeg");
			AVATAR_TYPES.add("image/png");
			AVATAR_TYPES.add("image/gif");
			AVATAR_TYPES.add("image/bmp");
		}
		
		@RequestMapping("avatar/change")
		public JsonResult<String> changeAvatar(MultipartFile file,HttpSession session) {
			//日志
			System.err.println("UserController.changeAvatar()");
			//关于MultipartFile接口的API
			//获取客户端上传的文件的原始文件名(客户端设备中的文件名)
			String originalFilename=file.getOriginalFilename();
			System.err.println("\toriginalFilename="+originalFilename);
			boolean isEmpty=file.isEmpty();
			//判断上传的文件是否为空boolean值，在表单中没有选择文件或者选择的文件是0字节则视为空，空则true值，否则false值
			System.err.println("\tfile.isEmpty()="+file.isEmpty());
			if(isEmpty) {
				//上传的文件为空，则抛出异常
				throw new FileEmptyException("上传失败，请选择要上传的文件");
			}
			
			long size=file.getSize();
			//获取文件的大小long值，以字节为单位
			System.err.println("\tfile.getSize()="+file.getSize());
			if(size>AVATAR_MAX_SIZE) {
				throw new FileSizeException("上传失败，不允许上传超过"+(AVATAR_MAX_SIZE/1024)+"KB的文件");
			}
			
			//获取文件的mime值String类型，MIME(Multipurpose Internet Mail Extensions)多用途互联网邮件扩展类型。
			//是设定某种扩展名的文件用一种应用程序来打开的方式类型，当该扩展名文件被访问的时候，浏览器会自动使用指定应用程序来打开
			String contentType=file.getContentType();
			System.err.println("\tfile.getContentType()="+file.getContentType());
			// 判断上传的文件类型是否符合：image/jpeg，image/png，image/gif，image/bmp
			if(!AVATAR_TYPES.contains(contentType)) {
				throw new FileTypeException("\"上传失败！仅允许上传以下类型的文件：\" + AVATAR_TYPES");
			}
			
			/*获取文件的输入流，InputStream getInputStream()，接收，通常用于自定义处理客户端提交的文件的数据，
			   不可以与void transferTo(File dest)同时使用
			  void transferTo(File dest)，保存客户端上传的文件，该方法不可以与getInputStream()同时使用
			  System.err.println("\tfile.getInputStream()="+file.getInputStream());
			 */			
			//将文件上传到哪个文件夹
			String parent=session.getServletContext().getRealPath("upload");
			System.err.println("\tupload path="+parent);
			File dir=new File(parent);
			if(!dir.exists()) {
				dir.mkdirs();
			}
			
			//保存上传的文件时使用的文件名，毫秒+纳秒，即可不会重复
			String filename=""+System.currentTimeMillis()+System.nanoTime();
			//后缀名
			String suffix="";
			int beginIndex=originalFilename.lastIndexOf(".");
			if(beginIndex>=1) {
				suffix=originalFilename.substring(beginIndex);
			}
			String child=filename+suffix;
			
			//将客户端上传的文件保存到服务器端
			File dest=new File(parent,child);
			try {
				file.transferTo(dest);
			} catch (IllegalStateException e) {
				throw new FileEmptyException("上传失败！您的文件的状态异常");
			} catch (IOException e) {
				throw new FileUploadIOException("上传失败！读写文件时出现错误，请重新上传！");
			}
			
			System.err.println("\tfile.transferTo(File dest)="+dest);
			//将保存的文件的路径记录到数据库中
			String avatar="/upload/"+child;
			System.err.println("\tavatar path="+avatar);
			Integer uid=getUidFromSession(session);
			String username=getUsernameFromSession(session);
			userService.changeAvatar(uid, avatar, username);
			//返回
			return new JsonResult<>(OK,avatar);	
		}
		
	
}
