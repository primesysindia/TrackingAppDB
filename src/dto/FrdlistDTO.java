package dto;

public class FrdlistDTO {

	String Userid,Name,Email,City,flag,Entity,MobNo;

	public String getMobNo() {
		return MobNo;
	}

	public void setMobNo(String MobNo) {
		MobNo = MobNo;
	}

	public String getUserid() {
		return Userid;
	}

	public void setUserid(String userid) {
		Userid = userid;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}

	public String getCity() {
		return City;
	}

	public void setCity(String city) {
		City = city;
	}

	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public String getEntity() {
		return Entity;
	}

	public void setEntity(String entity) {
		Entity = entity;
	}
}
