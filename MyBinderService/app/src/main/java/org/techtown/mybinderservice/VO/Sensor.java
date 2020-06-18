package org.techtown.mybinderservice.VO;

public class Sensor {
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
