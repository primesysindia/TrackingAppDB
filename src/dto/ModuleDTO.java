package dto;

public class ModuleDTO {
	int	createdBy,updatedBy;
	String module,moduleTitle,moduleDesc,moduleActivity,imageUrl,isEnable_forDemo,appPriority,createdAt,updatedAt,activeStatus;
	
	public int getCreatedBy() {
		return createdBy;
	}
	public void setCreatedBy(int createdBy) {
		this.createdBy = createdBy;
	}
	public int getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(int updatedBy) {
		this.updatedBy = updatedBy;
	}
	public String getModule() {
		return module;
	}
	public void setModule(String module) {
		this.module = module;
	}
	public String getModuleTitle() {
		return moduleTitle;
	}
	public void setModuleTitle(String moduleTitle) {
		this.moduleTitle = moduleTitle;
	}
	public String getModuleDesc() {
		return moduleDesc;
	}
	public void setModuleDesc(String moduleDesc) {
		this.moduleDesc = moduleDesc;
	}
	public String getModuleActivity() {
		return moduleActivity;
	}
	public void setModuleActivity(String moduleActivity) {
		this.moduleActivity = moduleActivity;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	public String getIsEnable_forDemo() {
		return isEnable_forDemo;
	}
	public void setIsEnable_forDemo(String isEnable_forDemo) {
		this.isEnable_forDemo = isEnable_forDemo;
	}
	public String getAppPriority() {
		return appPriority;
	}
	public void setAppPriority(String appPriority) {
		this.appPriority = appPriority;
	}
	public String getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(String createdAt) {
		this.createdAt = createdAt;
	}
	public String getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(String updatedAt) {
		this.updatedAt = updatedAt;
	}
	public String getActiveStatus() {
		return activeStatus;
	}
	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
}
