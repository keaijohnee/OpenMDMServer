package com.jiangge.pojo;
import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="device")
public class Device implements Serializable {

    private static final long serialVersionUID = -7048625537017892345L;

    @Id
    private String id;
    /**设备标志**/
    private String deviceFlag; 
    /**设备编号（和Device主键对应）**/
    private String deviceId; 
    /**以下是MDM推送相关**/
    private  String topic;
    private  String token;
    private  String pushMagic;
    private  String udid;
    @Column(length = 65535)
    private  String unlockToken;
    /**以下是通过MDM获取的**/
    private  String modelName;
    private  String model;
    private  String batteryLevel;
    private  String deviceCapacity;
    private  String availableDeviceCapacity;
    private  String oSVersion;
    /**客户新增的几个参数**/
    private  String serialNumber;
    private  String imei;
    private  String iccid;
    private  String meid;
    private  String isSupervised;
    private  String isDeviceLocatorServiceEnabled;
    private  String isActivationLockEnabled;
    private  String isCloudBackupEnabled;
    private  String wifimac;
    private  String bluetoothMAC;
    
    
    /**注册时间**/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis()); 
    /**设备状态（1：已认证；2可控制；-1：已移除）**/
    private String control;
    /**更新时间**/
    private Timestamp updateTime = new Timestamp(System.currentTimeMillis()); 

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

    public Timestamp getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Timestamp createTime) {
        this.createTime = createTime;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
    public String getPushMagic() {
        return pushMagic;
    }

    public void setPushMagic(String pushMagic) {
        this.pushMagic = pushMagic;
    }

    public String getUdid() {
        return udid;
    }

    public void setUdid(String udid) {
        this.udid = udid;
    }

    public String getUnlockToken() {
        return unlockToken;
    }

    public void setUnlockToken(String unlockToken) {
        this.unlockToken = unlockToken;
    }

    public String getControl() {
        return control;
    }

    public void setControl(String control) {
        this.control = control;
    }

	public String getModelName() {
		return modelName;
	}

	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	public String getModel() {
		return model;
	}

	public void setModel(String model) {
		this.model = model;
	}

	public String getBatteryLevel() {
		return batteryLevel;
	}

	public void setBatteryLevel(String batteryLevel) {
		this.batteryLevel = batteryLevel;
	}

	public String getDeviceCapacity() {
		return deviceCapacity;
	}

	public void setDeviceCapacity(String deviceCapacity) {
		this.deviceCapacity = deviceCapacity;
	}

	public String getAvailableDeviceCapacity() {
		return availableDeviceCapacity;
	}

	public void setAvailableDeviceCapacity(String availableDeviceCapacity) {
		this.availableDeviceCapacity = availableDeviceCapacity;
	}

	public String getoSVersion() {
		return oSVersion;
	}

	public void setoSVersion(String oSVersion) {
		this.oSVersion = oSVersion;
	}

	public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public String getIccid() {
		return iccid;
	}

	public void setIccid(String iccid) {
		this.iccid = iccid;
	}

	public String getMeid() {
		return meid;
	}

	public void setMeid(String meid) {
		this.meid = meid;
	}

	public String getIsSupervised() {
		return isSupervised;
	}

	public void setIsSupervised(String isSupervised) {
		this.isSupervised = isSupervised;
	}

	public String getIsDeviceLocatorServiceEnabled() {
		return isDeviceLocatorServiceEnabled;
	}

	public void setIsDeviceLocatorServiceEnabled(
			String isDeviceLocatorServiceEnabled) {
		this.isDeviceLocatorServiceEnabled = isDeviceLocatorServiceEnabled;
	}

	public String getIsActivationLockEnabled() {
		return isActivationLockEnabled;
	}

	public void setIsActivationLockEnabled(String isActivationLockEnabled) {
		this.isActivationLockEnabled = isActivationLockEnabled;
	}

	public String getIsCloudBackupEnabled() {
		return isCloudBackupEnabled;
	}

	public void setIsCloudBackupEnabled(String isCloudBackupEnabled) {
		this.isCloudBackupEnabled = isCloudBackupEnabled;
	}

	public String getWifimac() {
		return wifimac;
	}

	public void setWifimac(String wifimac) {
		this.wifimac = wifimac;
	}

	public String getBluetoothMAC() {
		return bluetoothMAC;
	}

	public void setBluetoothMAC(String bluetoothMAC) {
		this.bluetoothMAC = bluetoothMAC;
	}
	

}
