package dto;


import java.io.Serializable;

/**
 * Created by root on 14/10/16.
 */
public class SmsNotificationDTO implements Serializable {
    String Notify_Title,Notify_Type,Id,LatDir ,LangDir ,Lat ,Lang ,Speed ,Time ,Date ,ImeiNo,UpdateTime,UserId ;

    public String getUserId() {
		return UserId;
	}

	public void setUserId(String userId) {
		UserId = userId;
	}

	public String getId() {
        return Id;
    }

    public String getNotify_Title() {
        return Notify_Title;
    }

    public void setNotify_Title(String notify_Title) {
        Notify_Title = notify_Title;
    }

    public String getNotify_Type() {
        return Notify_Type;
    }

    public void setNotify_Type(String notify_Type) {
        Notify_Type = notify_Type;
    }

    public void setId(String id) {

        Id = id;
    }

    public String getLatDir() {
        return LatDir;
    }

    public void setLatDir(String latDir) {
        LatDir = latDir;
    }

    public String getLangDir() {
        return LangDir;
    }

    public void setLangDir(String langDir) {
        LangDir = langDir;
    }

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLang() {
        return Lang;
    }

    public void setLang(String lang) {
        Lang = lang;
    }

    public String getSpeed() {
        return Speed;
    }

    public void setSpeed(String speed) {
        Speed = speed;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getImeiNo() {
        return ImeiNo;
    }

    public void setImeiNo(String imeiNo) {
        ImeiNo = imeiNo;
    }

    public String getUpdateTime() {
        return UpdateTime;
    }

    public void setUpdateTime(String updateTime) {
        UpdateTime = updateTime;
    }
}
