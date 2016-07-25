package com.jiangge.controller;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jiangge.pojo.DeviceTemp;
import com.jiangge.pojo.User;
import com.jiangge.service.AdminService;
import com.jiangge.service.CommandService;
import com.jiangge.service.DeviceService;
import com.jiangge.service.DeviceTempService;
import com.jiangge.service.UserService;
import com.jiangge.utils.ConfigUtils;
import com.jiangge.utils.EmailUtil;
import com.jiangge.utils.MDMTaskUtils;
import com.jiangge.utils.StringUtil;
import com.jiangge.vo.AdminVO;

@SuppressWarnings("all")
@Controller
@RequestMapping("/user")
public class UserController {
	
	private DeviceService deviceService;
	private CommandService commandService;
	private DeviceTempService deviceTempService;
	private UserService userService;
	private AdminService adminService;
	
	/**
     * 邮箱注册
     * @throws Exception
     */
	@ResponseBody
    @RequestMapping("/register")
    public Map<String, String> getCode(HttpServletRequest request,HttpServletResponse response) throws Exception {
        Map<String, String> map  = new HashMap<String, String>();
        String email = request.getParameter("email")==null?"":request.getParameter("email").trim();
        boolean ismobile = StringUtil.isEmail(email);
        if(ismobile){
        	User user = userService.getUserByHql("from User where email = ?", email);
        	if(null != user){
        		map.put("state", "0");
                map.put("msg", "该邮箱已经激活过了，请到邮箱中查收登录邮件！");
        	}else{
        		String id = UUID.randomUUID().toString().replace("-", "");
        		/**发送邮件**/
        		String content = "用户 " + email+" 您好：<br/><br/>描述文件下载地址："
        				+ "<a href='http://mdm.mbaike.net/user/download.do?uid="+id+"' target='_blank'>http://mdm.mbaike.net/user/download.do?uid="+id+"</a><br/>"
        				+ "描述文件二维码地址：<br/><img src='http://qr.topscan.com/api.php?el=l&w=200&m=10&text=http://mdm.mbaike.net/user/download.do?uid="+id+"'/><br/>"
        				+ "平台登录地址：<a href='http://mdm.mbaike.net/sysadmin/login.jsp' target='_blank'>http://mdm.mbaike.net</a>，登录账号是：" + email + "，登录密码是：" + id + "</br>"
        				+ "<br/>感谢支持！http://www.mbaike.net/";
        		boolean sendOK = EmailUtil.send("MDM测试激活邮件",email,content);
        		if(sendOK){
        			/**保存数据**/
            		user = new User();
            		user.setEmail(email);
            		user.setId(id);;
            		String password = StringUtil.MD5(id);
            		user.setPassword(password);
            		userService.saveOrUpdtae(user);
        			map.put("state", "1");
                    map.put("msg", "邮箱激活成功，请登录邮箱查收激活信息！");
        		}else{
        			map.put("state", "0");
                    map.put("msg", "激活失败，请联系管理员：459104018@qq.com!");
        		}
        	}
        }else{
        	 map.put("state", "0");
             map.put("msg", "邮箱不合法，请重新输入邮箱地址!");
        }
        return map;
    }
	
	 /**
     * 下载设备控制描述文件功能
     * @throws Exception
     */
    
    @RequestMapping("/download")
    public void downConfig(HttpServletRequest request,HttpServletResponse response) throws Exception {
        System.out.println("-------------------Download MobileConfig File Start---------------");
        String uuid = request.getParameter("uid")==null?"":request.getParameter("uid");
    	String url = ConfigUtils.getConfig("BASE_URL")+ConfigUtils.getConfig("DOWN_MOBILECONFIG");
        String mobileconfig = MessageFormat.format(url,uuid);
		/**生成描述文件**/
	    String configPath =  request.getRealPath("mdmtool");
	    String tempPath =  request.getRealPath("mdmtool/down");
		MDMTaskUtils.createMobileconfig(configPath, tempPath, uuid);
		Thread.sleep(1500);
        System.out.println("-------------------getCode End---------------");
        String newPath =  tempPath + "/" + uuid + "Signed.mobileconfig";
        response.setHeader("content-type", "application/x-apple-aspen-config");
        response.setCharacterEncoding("UTF-8");
        String configTitle = "MDMApp_"+uuid;
        response.setHeader("Content-Disposition", "attachment; filename=" + configTitle + ".mobileconfig");
        /**获取配置文件动态组装参数**/
        /**写入文件**/
        java.io.FileInputStream fis = new java.io.FileInputStream(newPath);
        java.io.OutputStream os = response.getOutputStream();
        byte[] b = new byte[1024];
        int i = 0;
        while ((i = fis.read(b)) > 0) { os.write(b, 0, i); }
        System.out.println("-------------------Download MobileConfig File End---------------");
        fis.close();
        os.flush();
        os.close();
    }
    
    /**
	 * 后台登陆
	 */
	@RequestMapping("/login")
	public ModelAndView adminLogin(HttpServletRequest request,HttpServletResponse response) {
		ModelAndView mav = null;
		String email = request.getParameter("email")==null?"":request.getParameter("email");
		String password = request.getParameter("password")==null?"":request.getParameter("password");
		String newpassword = StringUtil.MD5(password);
		User user = userService.getUserByHql("from User where email = ?", email);
		if(null != user){
			String dbpassword = user.getPassword()==null?"":user.getPassword();
			if(dbpassword.equals(newpassword)){
				AdminVO admin = new AdminVO();
				admin.setId(user.getId());
				admin.setEmail(email);
				admin.setPassword(dbpassword);
				admin.setRemark(user.getRemark());
				request.getSession().setAttribute("sysadmin", admin);
				mav = new ModelAndView("redirect:/sysadmin/main.do");  
			}else{
				mav = new ModelAndView("login");  
				mav.addObject("msg", "密码输入错误,登录失败！");
			}
		}else{
			mav = new ModelAndView("login");  
			mav.addObject("msg", "邮箱账号不存在,登录失败！");
		}
		return mav;  
	}
	
	/****************************************************************/
	public AdminService getAdminService() {
		return adminService;
	}
	
	@Resource
	public void setAdminService(AdminService adminService) {
		this.adminService = adminService;
	}
	
	public DeviceService getDeviceService() {
		return deviceService;
	}
	@Resource
	public void setDeviceService(DeviceService deviceService) {
		this.deviceService = deviceService;
	}
	
	public CommandService getCommandService() {
		return commandService;
	}
	@Resource
	public void setCommandService(CommandService commandService) {
		this.commandService = commandService;
	}
	
	public DeviceTempService getDeviceTempService() {
		return deviceTempService;
	}
	@Resource
	public void setDeviceTempService(DeviceTempService deviceTempService) {
		this.deviceTempService = deviceTempService;
	}
	
	public UserService getUserService() {
		return userService;
	}
	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
