package cn.tedu.store.service.ex;
/**
 * 用户名冲突的异常
 */
public class UsernameDuplicateException extends ServiceException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4488079259092192344L;

	public UsernameDuplicateException() {
		super();
	}

	public UsernameDuplicateException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public UsernameDuplicateException(String message, Throwable cause) {
		super(message, cause);
	}

	public UsernameDuplicateException(String message) {
		super(message);
	}

	public UsernameDuplicateException(Throwable cause) {
		super(cause);
	}

	
}
