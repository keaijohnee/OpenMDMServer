package com.jiangge.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "profile")
public class Profile implements Serializable{

    private static final long serialVersionUID = -7048625537017892345L;
    
    @Id
    private String id;
    /**设备编号（和Device主键对应）**/
    private String deviceId;        
    /**注册时间**/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());
    /**命令类型**/
    private  String ctype; 
    /**执行结果**/
    @Column(length = 65535)
    private  String result;  
    /**更新时间**/
    private Timestamp updateTime = new Timestamp(System.currentTimeMillis()); 
    

    public String getCtype() {
		return ctype;
	}

	public void setCtype(String ctype) {
		this.ctype = ctype;
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

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
    
    public Timestamp getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Timestamp updateTime) {
		this.updateTime = updateTime;
	}


}
