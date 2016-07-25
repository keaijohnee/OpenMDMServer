package com.jiangge.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "admin")
public class Admin implements Serializable{

    private static final long serialVersionUID = -7048625537017892345L;
    
    @Id
    private String id;
    /**用户名**/
    @Column(length = 50)
    private String account;  
    /**密码**/
    @Column(length = 50)
    private  String password;   
    /**角色**/
    @Column(length = 10)
    private  String role;         
    /**备注**/
    @Column(length = 255)
    private  String remark;   
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

   
}
