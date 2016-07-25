package com.jiangge.vo;

/**
 * 服务器给客户端传递的JSON基类
 * @author jiang.li
 */
public class MobileBean {

    public int status;       /**数据状态**/
    public String desc;      /**状态描述**/
    public Object data;      /**返回的数据**/


    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        if (data == null) {
            data = "";
        }
        this.data = data;
    }
}
