package com.jiangge.pojo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity()
@Table(name = "user")
public class User implements Serializable{

    private static final long serialVersionUID = -7048625537017892345L;
    
    @Id
    private String id;
    /**用户名**/
    @Column(length = 100)
    private String email;  
    /**密码**/
    @Column(length = 100)
    private  String password;        
    /**备注**/
    @Column(length = 255)
    private  String remark;   
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

   
}
