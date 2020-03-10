package cn.tedu.store.entity;
/**
 * 实体类的基类
 */
import java.io.Serializable;
import java.util.Date;

//该类只用作被继承，即设计成抽象类
abstract class BaseEntity implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -4006824984031415365L;
	
		private String createdUser;
	    private Date createdTime;
		private String modifiedUser;
		private Date modifiedTime;
		
		public String getCreatedUser() {
			return createdUser;
		}
		public void setCreatedUser(String createdUser) {
			this.createdUser = createdUser;
		}
		public Date getCreatedTime() {
			return createdTime;
		}
		public void setCreatedTime(Date createdTime) {
			this.createdTime = createdTime;
		}
		public String getModifiedUser() {
			return modifiedUser;
		}
		public void setModifiedUser(String modifiedUser) {
			this.modifiedUser = modifiedUser;
		}
		public Date getModifiedTime() {
			return modifiedTime;
		}
		public void setModifiedTime(Date modifiedTime) {
			this.modifiedTime = modifiedTime;
		}
		
		@Override
		public String toString() {
			return "BaseEntity [createdUser=" + createdUser + ", createdTime=" + createdTime + ", modifiedUser="
					+ modifiedUser + ", modifiedTime=" + modifiedTime + "]";
		}
		
		
}
