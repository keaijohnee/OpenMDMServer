package com.jiangge.pojo;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="devicetemp")
public class DeviceTemp implements Serializable {

    private static final long serialVersionUID = -7048625537017892345L;

    @Id
    private String id;
    /**设备标志**/
    private String deviceFlag; 
    /**设备编号（和Device主键对应）**/
    private String deviceId; 
    /**回调地址**/
    private  String callBack;

	public String getDeviceFlag() {
		return deviceFlag;
	}

	public void setDeviceFlag(String deviceFlag) {
		this.deviceFlag = deviceFlag;
	}

	public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

	public String getCallBack() {
		return callBack;
	}

	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}

   
}
