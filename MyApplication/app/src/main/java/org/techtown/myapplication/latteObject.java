package org.techtown.myapplication;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.sql.Date;

public class latteObject {
}

class Alert {
    private String deviceID;
    private int hour;           // 시간
    private int min;            // 분
    private String weeks;       // 알람 수행 요일
    private boolean flag;       // 알람 on/off


    // constructor
    private Alert() {
        this.deviceID = "Android01";
    }

    public Alert(int hour, int min, String weeks, boolean flag) {
        this();
        this.hour = hour;
        this.min = min;
        this.weeks = weeks;
        this.flag = flag;
    }


    // get, set
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public String getWeeks() {
        return weeks;
    }

    public void setWeeks(String weeks) {
        this.weeks = weeks;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

}

class LatteMessage {
    private String deviceID;
    private String deviceType;
    private String dataType;
    private String jsonData;
    private static Gson gson = new GsonBuilder().setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS").create();


    // constructor
    private LatteMessage() {
        this.deviceID = MainActivity.getDeviceId();
        this.deviceType = MainActivity.getDeviceType();
    }

    public LatteMessage(SensorData data) {
        this();
        this.dataType = "SensorData";
        this.jsonData = LatteMessage.gson.toJson(data);
    }

    public LatteMessage(Alert data) {
        this();
        this.dataType = "Alert";
        this.jsonData = LatteMessage.gson.toJson(data);
    }

    public LatteMessage(String id, String type, String data) {
        this.deviceID = id;
        this.dataType = type;
        this.jsonData = data;
    }

    public LatteMessage(String states, String stateDetail) {
        this();
        this.dataType = "Request";
        this.jsonData = LatteMessage.gson.toJson(new SensorData(states, stateDetail));
    }
    public LatteMessage(String SensorId) {
        this();
        this.dataType = "Request";
        this.jsonData = SensorId;
    }

    // get, set method
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getDeviceType() { return deviceType; }

    public void setDeviceType(String deviceType) { this.deviceType = deviceType; }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String voType) {
        this.dataType = voType;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    @Override
    public String toString() {
        return "Message [deviceID=" + deviceID + ", voType=" + dataType + ", jsonData=" + jsonData + "]";
    }

}
class Sensor {

    private String deviceID = "Android01";
    private String sensorID = "" + this.hashCode();
    private String sensorType;
    private SensorData recentData;


    // constructor
    public Sensor(String sensorType) {
        this.sensorType = sensorType;
    }
//
//    public Sensor(LatteBaseClient device, String sensorType) {
//        this.deviceID = LatteBaseClient.getDeviceId();
//        this.sensorType = sensorType;
//    }


    // custom method
    public String getStates() {
        return this.recentData.getStates();
    }

    public String getStateDetail() {
        return this.recentData.getStateDetail();
    }

    // 지정된 센서에 최신 데이터 업데이트 (states)
    public SensorData setRecentData(String states) {
        this.recentData = new SensorData(this.sensorID, states);
        return this.recentData;
    }

    // 지정된 센서에 최신 데이터 업데이트 (states, stateDetail)
    public SensorData setRecentData(String states, String stateDetail) {
        this.recentData = new SensorData(this.sensorID, states, stateDetail);
        return this.recentData;
    }


    // get, set
    public String getDeviceID() {
        return deviceID;
    }

    public void setDeviceID(String deviceID) {
        this.deviceID = deviceID;
    }

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public String getSensorType() {
        return sensorType;
    }

    public void setSensorType(String sensorType) {
        this.sensorType = sensorType;
    }

    public SensorData getRecentData() {
        return recentData;
    }

//	public void setRecentData(SensorData recentData) {
//		this.recentData = recentData;
//	}

    public SensorData setRecentData(SensorData data) {
        this.recentData = data;
        return this.recentData;
    }

}
class SensorData {
    private int dataID;
    private String sensorID;
    private Date time;
    private String states;
    private String stateDetail;


    // constructor
    private SensorData() {
        this.dataID = this.hashCode();
        this.time = new Date(System.currentTimeMillis());
    }

    public SensorData(String sensorID, String states) {
        this();
        this.sensorID = sensorID;
        this.states = states;
    }

    public SensorData(String sensorID, String states, String stateDetail) {
        this(sensorID, states);
        this.stateDetail = stateDetail;
    }


    // get, set
    public int getDataID() {
        return dataID;
    }

    public void setDataID(int dataID) {
        this.dataID = dataID;
    }

    public String getSensorID() {
        return sensorID;
    }

    public void setSensorID(String sensorID) {
        this.sensorID = sensorID;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getStates() {
        return states;
    }

    public void setStates(String states) {
        this.states = states;
    }

    public String getStateDetail() {
        return stateDetail;
    }

    public void setStateDetail(String stateDetail) {
        this.stateDetail = stateDetail;
    }

    @Override
    public String toString() {
        return "SensorData{" +
                "dataID=" + dataID +
                ", sensorID='" + sensorID + '\'' +
                ", time=" + time +
                ", states='" + states + '\'' +
                ", stateDetail='" + stateDetail + '\'' +
                '}';
    }
}

