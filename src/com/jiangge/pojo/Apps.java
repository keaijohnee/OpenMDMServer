package com.jiangge.pojo;
import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="apps")
public class Apps implements Serializable {

    private static final long serialVersionUID = -7048625537017892345L;

    @Id
    private String  id;
    /**设备编号（和Device主键对应）**/
    private String  deviceId; 
    /**以下是通过MDM获取的**/
    private String  bundleSize;
    private String  dynamicSize;
    private String  identifier;
    private String  appName;
    private String  shortVersion;
    private String  version;
    private String  managedAppsOnly;
    
    /**添加时间**/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());
    
    
	public String getManagedAppsOnly() {
		return managedAppsOnly;
	}

	public void setManagedAppsOnly(String managedAppsOnly) {
		this.managedAppsOnly = managedAppsOnly;
	}

	public String getBundleSize() {
		return bundleSize;
	}

	public void setBundleSize(String bundleSize) {
		this.bundleSize = bundleSize;
	}

	public String getDynamicSize() {
		return dynamicSize;
	}

	public void setDynamicSize(String dynamicSize) {
		this.dynamicSize = dynamicSize;
	}

	public String getIdentifier() {
		return identifier;
	}

	public void setIdentifier(String identifier) {
		this.identifier = identifier;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getShortVersion() {
		return shortVersion;
	}

	public void setShortVersion(String shortVersion) {
		this.shortVersion = shortVersion;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
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

    

}
