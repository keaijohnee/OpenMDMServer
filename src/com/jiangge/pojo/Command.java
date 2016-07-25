package com.jiangge.pojo;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "command")
public class Command implements Serializable{

    private static final long serialVersionUID = -7048625537017892345L;
    
    @Id
    private String id;
    /**设备编号（和Device主键对应）**/
    private String deviceId;  
    /**发送的命令**/
    private  String command;   
    /**是否执行（3：失败，2：已成功,1：已执行，0：未执行）**/
    private  String doIt;         
    /**注册时间**/
    private Timestamp createTime = new Timestamp(System.currentTimeMillis());
    /**命令类型**/
    private  String ctype; 
    /**类型值**/
    private  String cvalue; 
    /**执行结果**/
    @Column(length = 65535)
    private  String result;   
    /**回调地址**/
    private  String callBack;
    

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

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDoIt() {
        return doIt;
    }

    public void setDoIt(String doIt) {
        this.doIt = doIt;
    }

	public String getCvalue() {
		return cvalue;
	}

	public void setCvalue(String cvalue) {
		this.cvalue = cvalue;
	}

	public String getCallBack() {
		return callBack;
	}

	public void setCallBack(String callBack) {
		this.callBack = callBack;
	}
    
}
