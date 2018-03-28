package com.jiangge.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.jiangge.pojo.Admin;
import com.jiangge.pojo.Apps;
import com.jiangge.pojo.Command;
import com.jiangge.pojo.Device;
import com.jiangge.pojo.Profile;
import com.jiangge.pojo.User;
import com.jiangge.service.AdminService;
import com.jiangge.service.AppsService;
import com.jiangge.service.CommandService;
import com.jiangge.service.DeviceService;
import com.jiangge.service.ProfileService;
import com.jiangge.service.UserService;
import com.jiangge.utils.StringUtil;
import com.jiangge.utils.StringUtils;
import com.jiangge.vo.AdminVO;
import com.jiangge.vo.CommandTypeEnum;
import com.jiangge.vo.CommandVO;
import com.jiangge.vo.DeviceStateEnum;
import com.jiangge.vo.DoTypeEnum;
import com.jiangge.vo.InstallTypeEnum;
import com.jiangge.vo.PageBean;

@Controller
@RequestMapping("/sysadmin")
public class SysadminController {
	
	private AdminService adminService;
	private DeviceService deviceService;
	private CommandService commandService;
	private AppsService appsService;
	private ProfileService profileService;
	private UserService userService;
	
	
	/**
	 * 描述文件详情页面
	 */
	@RequestMapping("/profileInfo")
	public ModelAndView profileInfo(HttpServletRequest request,HttpServletResponse response){
		String hql = "from Profile where deviceId = ? and ctype = ?";
		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
		String ctype = request.getParameter("ctype")==null?"":request.getParameter("ctype");
		Profile profile = profileService.getProfileByHql(hql, deviceId , ctype);
		if(null != profile){
			String result = profile.getResult();
			result = result.replace("<", "&lt;");
			result = result.replace(">", "&gt;");
			result = "<pre style='font-family:\"微软雅黑\"'>" + result + "</pre>";
			profile.setResult(result);
		}
		ModelAndView mav = new ModelAndView("device/profile");  
		mav.addObject("profile", profile);
		mav.addObject("deviceId", deviceId);
		mav.addObject("ctype", ctype);
	    return mav;  
	}
	
	/**
	 * 设备移除
	 */
	@RequestMapping("/removeDevice")
	@ResponseBody
	public Map<String, String> removeDevice(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
		if(StringUtils.isNotEmpty(deviceId)){
			deviceService.deleteDeviceByDeviceId(deviceId);
			appsService.deleteAppsByDeviceId(deviceId);
			commandService.deleteCommandByDeviceId(deviceId);
			map.put("msg", "设备移除成功!");
		}else{
			map.put("msg", "deviceId不能为空!");
		}
		return map;
	}
	
	/**
	 * 密码修改
	 */
	@RequestMapping("/changePwd")
	@ResponseBody
	public Map<String, String> changePwd(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		String uid = request.getParameter("uid")==null?"":request.getParameter("uid");
		String password = request.getParameter("password")==null?"":request.getParameter("password");
		User user = userService.getUserByHql("from User where id = ?", uid);
		if(StringUtils.isNotEmpty(password)){
			String newpassword = StringUtil.MD5(password);
			user.setPassword(newpassword);
			userService.saveOrUpdtae(user);
			map.put("msg", "密码修改成功!");
		}else{
			map.put("msg", "输入的新密码为空,密码未修改!");
		}
		return map;
	}
	
	/**
	 * 密码修改
	 */
	@RequestMapping("/adminChangePwd")
	@ResponseBody
	public Map<String, String> adminChangePwd(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		String uid = request.getParameter("uid")==null?"":request.getParameter("uid");
		String password = request.getParameter("password")==null?"":request.getParameter("password");
		Admin admin = adminService.getAdminByHql("from Admin where id = ?", uid);
		if(StringUtils.isNotEmpty(password)){
			String newpassword = StringUtil.MD5(password);
			admin.setPassword(newpassword);
			adminService.saveOrUpdtae(admin);
			map.put("msg", "密码修改成功!");
		}else{
			map.put("msg", "输入的新密码为空,密码未修改!");
		}
		return map;
	}
	
	/**
	 * 到密码修改页面
	 */
	@RequestMapping("/changePwdInput")
	public ModelAndView changePwdInput(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView("admin/pwdinput");  
	    return mav;  
	}
	
	/**
	 * 到密码修改页面
	 */
	@RequestMapping("/adminChangePwdInput")
	public ModelAndView adminChangePwdInput(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView("admin/adminpwdinput");  
	    return mav;  
	}
	
	/**
	 * 到安装APP页面
	 */
	@RequestMapping("/installAppInput")
	public ModelAndView installAppInput(HttpServletRequest request,HttpServletResponse response){
		String hql = "from Device where deviceId = ?";
		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
		Device device = deviceService.getDeviceByHql(hql, deviceId);
		Map<String,String> typeMap = InstallTypeEnum.getAllType();
		ModelAndView mav = new ModelAndView("device/installapp");  
		mav.addObject("device", device);
		mav.addObject("typeMap", typeMap);
	    return mav;  
	}
	
	/**
	 * 更改设备标签页面
	 */
	@RequestMapping("/updateFlagInput")
	public ModelAndView updateFlagInput(HttpServletRequest request,HttpServletResponse response){
		String hql = "from Device where deviceId = ?";
		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
		Device device = deviceService.getDeviceByHql(hql, deviceId);
		ModelAndView mav = new ModelAndView("device/flaginput");  
		mav.addObject("device", device);
	    return mav;  
	}
	
	/**
	 * 更改设备标签
	 */
	@RequestMapping("/updateFlag")
	public ModelAndView updateFlag(HttpServletRequest request,HttpServletResponse response){
		String hql = "from Device where deviceId = ?";
		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
		String deviceFlag = request.getParameter("deviceFlag")==null?"":request.getParameter("deviceFlag");
		Device device = deviceService.getDeviceByHql(hql, deviceId);
		device.setDeviceFlag(deviceFlag);
		deviceService.saveOrUpdate(device);
		ModelAndView mav = new ModelAndView("redirect:/sysadmin/deviceList.do");  
	    return mav;  
	}
	
	/**
	 * 更改设备标签
	 */
	@RequestMapping("/allUpdateFlag")
	public ModelAndView allUpdateFlag(HttpServletRequest request,HttpServletResponse response){
		String hql = "from Device where deviceId = ?";
		String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
		String deviceFlag = request.getParameter("deviceFlag")==null?"":request.getParameter("deviceFlag");
		Device device = deviceService.getDeviceByHql(hql, deviceId);
		device.setDeviceFlag(deviceFlag);
		deviceService.saveOrUpdate(device);
		ModelAndView mav = new ModelAndView("redirect:/sysadmin/allDeviceList.do");  
	    return mav;  
	}
	
	
	
	/**
	 * 设备数据列表
	 */
	@RequestMapping("/deviceInfo")
	public ModelAndView deviceInfo(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = null;
		AdminVO admin = (AdminVO)request.getSession().getAttribute("sysadmin");
		if(null != admin){
			String hql = "from Device where deviceId = ?";
			int pageIndex = request.getParameter("pageIndex")==null?1:Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = request.getParameter("pageSize")==null?10:Integer.parseInt(request.getParameter("pageSize"));
			String deviceId = request.getParameter("deviceId")==null?"":request.getParameter("deviceId");
			Device device = deviceService.getDeviceByHql(hql, deviceId);
			device.setBatteryLevel(StringUtil.formatFourPoint(device.getBatteryLevel()));
			device.setDeviceCapacity(StringUtil.formatTwoPoint(device.getDeviceCapacity()));
			device.setAvailableDeviceCapacity(StringUtil.formatTwoPoint(device.getAvailableDeviceCapacity()));
			List<Apps> list = appsService.pageQuery("from Apps where deviceId = '"+deviceId+"' order by managedAppsOnly desc", pageIndex, pageSize);
			int count = appsService.getCount("select count(A.id) as number from Apps A where A.deviceId = '"+deviceId+"'");
			int totalPageNum = (int) (count / pageSize);
			if (count % pageSize != 0) {
				totalPageNum++;
			}
			PageBean pageList = new PageBean(pageIndex,totalPageNum,count,list);
			mav = new ModelAndView("device/info");  
			mav.addObject("device", device);
			mav.addObject("pageList", pageList);
			mav.addObject("searchText", "&deviceId="+deviceId);
		}else{
			mav = new ModelAndView("login");  
			mav.addObject("msg", "登录过期，请重新登录");
		}
	    return mav;  
	}
	
	/**
	 * 设备数据列表
	 */
	@RequestMapping("/deviceList")
	public ModelAndView deviceList(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = null;
		AdminVO admin = (AdminVO)request.getSession().getAttribute("sysadmin");
		if(null != admin){
			String hql = "from Device where deviceId= ? order by createTime desc";
			int pageIndex = request.getParameter("pageIndex")==null?1:Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = request.getParameter("pageSize")==null?10:Integer.parseInt(request.getParameter("pageSize"));
			List<Device> list = deviceService.pageQuery(hql, pageIndex, pageSize, admin.getId());
			int count = deviceService.getCount("select count(A.id) as number from Device A  where A.deviceId= '"+admin.getId()+"'");
			int totalPageNum = (int) (count / pageSize);
			if (count % pageSize != 0) {
				totalPageNum++;
			}
			Map<String,String> deviceStateMap = DeviceStateEnum.getAllType();
			PageBean pageList = new PageBean(pageIndex,totalPageNum,count,list);
			mav = new ModelAndView("device/list");  
			mav.addObject("pageList", pageList);
			mav.addObject("deviceStateMap", deviceStateMap);
		}else{
			mav = new ModelAndView("login");  
			mav.addObject("msg", "登录过期，请重新登录");
		}
	    return mav;  
	}
	
	/**
	 * 所有设备数据列表
	 */
	@RequestMapping("/allDeviceList")
	public ModelAndView allDeviceList(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = null;
		AdminVO admin = (AdminVO)request.getSession().getAttribute("sysadmin");
		if(null != admin){
			String hql = "from Device where 1=1 order by createTime desc";
			int pageIndex = request.getParameter("pageIndex")==null?1:Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = request.getParameter("pageSize")==null?10:Integer.parseInt(request.getParameter("pageSize"));
			List<Device> list = deviceService.pageQuery(hql, pageIndex, pageSize);
			int count = deviceService.getCount("select count(A.id) as number from Device A  where 1=1 ");
			int totalPageNum = (int) (count / pageSize);
			if (count % pageSize != 0) {
				totalPageNum++;
			}
			Map<String,String> deviceStateMap = DeviceStateEnum.getAllType();
			PageBean pageList = new PageBean(pageIndex,totalPageNum,count,list);
			mav = new ModelAndView("device/alllist");  
			mav.addObject("pageList", pageList);
			mav.addObject("deviceStateMap", deviceStateMap);
		}else{
			mav = new ModelAndView("login");  
			mav.addObject("msg", "登录过期，请重新登录");
		}
	    return mav;  
	}
	
	/**
	 * 命令日志列表
	 */
	@RequestMapping("/logList")
	public ModelAndView logList(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = null;
		AdminVO admin = (AdminVO)request.getSession().getAttribute("sysadmin");
		if(null != admin){
			String hql = "from Command A where A.deviceId = ? order by createTime desc";
			int pageIndex = request.getParameter("pageIndex")==null?1:Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = request.getParameter("pageSize")==null?10:Integer.parseInt(request.getParameter("pageSize"));
			List<Command> list = commandService.pageQuery(hql, pageIndex, pageSize,admin.getId());
			List<CommandVO> newList = new ArrayList<CommandVO>(); 
			CommandVO vo = null;
			for(Command command : list){
				if(null != command){
					String result = command.getResult()==null?"":command.getResult();
					result = result.replace("<", "&lt;");
					result = result.replace(">", "&gt;");
					result = "<pre style='font-family:\"微软雅黑\"'>" + result + "</pre>";
					command.setResult(result);
				}
				vo = new CommandVO(command);
				String deviceId = command.getDeviceId();
				Device device = deviceService.getDeviceByHql("from Device where deviceId = ?", deviceId);
				if(null != device){
					vo.setDeviceFlag(device.getDeviceFlag());
				}else{
					vo.setDeviceFlag("未知");
				}
				newList.add(vo);
			}
			int count = commandService.getCount("select count(A.id) as number from Command A where A.deviceId ='"+admin.getId()+"'");
			int totalPageNum = (int) (count / pageSize);
			if (count % pageSize != 0) {
				totalPageNum++;
			}
			Map<String,String> commandTypeMap = CommandTypeEnum.getAllType();
			Map<String,String> doTypeMap = DoTypeEnum.getAllType();
			PageBean pageList = new PageBean(pageIndex,totalPageNum,count,newList);
			mav = new ModelAndView("log/list");  
			mav.addObject("pageList", pageList);
			mav.addObject("commandTypeMap", commandTypeMap);
			mav.addObject("doTypeMap", doTypeMap);
		}else{
			mav = new ModelAndView("login");  
			mav.addObject("msg", "登录过期，请重新登录");
		}
	    return mav;  
	}
	
	/**
	 * 命令日志列表
	 */
	@RequestMapping("/allLogList")
	public ModelAndView allLogList(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = null;
		AdminVO admin = (AdminVO)request.getSession().getAttribute("sysadmin");
		if(null != admin){
			String hql = "from Command A where 1=1 order by createTime desc";
			int pageIndex = request.getParameter("pageIndex")==null?1:Integer.parseInt(request.getParameter("pageIndex"));
			int pageSize = request.getParameter("pageSize")==null?10:Integer.parseInt(request.getParameter("pageSize"));
			List<Command> list = commandService.pageQuery(hql, pageIndex, pageSize);
			List<CommandVO> newList = new ArrayList<CommandVO>(); 
			CommandVO vo = null;
			for(Command command : list){
				if(null != command){
					String result = command.getResult()==null?"":command.getResult();
					result = result.replace("<", "&lt;");
					result = result.replace(">", "&gt;");
					result = "<pre style='font-family:\"微软雅黑\"'>" + result + "</pre>";
					command.setResult(result);
				}
				vo = new CommandVO(command);
				String deviceId = command.getDeviceId();
				Device device = deviceService.getDeviceByHql("from Device where deviceId = ?", deviceId);
				if(null != device){
					vo.setDeviceFlag(device.getDeviceFlag());
				}else{
					vo.setDeviceFlag("未知");
				}
				newList.add(vo);
			}
			int count = commandService.getCount("select count(A.id) as number from Command A where 1=1 ");
			int totalPageNum = (int) (count / pageSize);
			if (count % pageSize != 0) {
				totalPageNum++;
			}
			Map<String,String> commandTypeMap = CommandTypeEnum.getAllType();
			Map<String,String> doTypeMap = DoTypeEnum.getAllType();
			PageBean pageList = new PageBean(pageIndex,totalPageNum,count,newList);
			mav = new ModelAndView("log/alllist");  
			mav.addObject("pageList", pageList);
			mav.addObject("commandTypeMap", commandTypeMap);
			mav.addObject("doTypeMap", doTypeMap);
		}else{
			mav = new ModelAndView("login");  
			mav.addObject("msg", "登录过期，请重新登录");
		}
	    return mav;  
	}
	
	/**
	 * 命令日志列表
	 */
	@RequestMapping("/dellog")
	@ResponseBody
	public Map<String, String> logDel(HttpServletRequest request,HttpServletResponse response){
		Map<String, String> map = new HashMap<String, String>();
		String id = request.getParameter("id");
		commandService.deleteCommandById(id);
		map.put("msg", "删除成功!");
		return map;
	}
	
	
	/**
	 * 左边导航
	 */
	@RequestMapping("/main")
	public ModelAndView main(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView("main");  
	    return mav;  
	}
	
	/**
	 * 左边导航
	 */
	@RequestMapping("/left")
	public ModelAndView left(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView("left");  
	    return mav;  
	}
	
	/**
	 * 头部导航
	 */
	@RequestMapping("/top")
	public ModelAndView top(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView("top");  
	    return mav;  
	}
	
	/**
	 * 头部导航
	 */
	@RequestMapping("/welcome")
	public ModelAndView welcome(HttpServletRequest request,HttpServletResponse response){
		ModelAndView mav = new ModelAndView("welcome");  
	    return mav;  
	}
	
	/**
	 * 后台退出
	 */
	@RequestMapping("/logout")
	public ModelAndView logout(HttpServletRequest request,HttpServletResponse response){
		request.getSession().removeAttribute("sysadmin");
		ModelAndView mav = new ModelAndView("login");  
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
	
	public AppsService getAppsService() {
		return appsService;
	}
	@Resource
	public void setAppsService(AppsService appsService) {
		this.appsService = appsService;
	}
	
	public ProfileService getProfileService() {
		return profileService;
	}
	@Resource
	public void setProfileService(ProfileService profileService) {
		this.profileService = profileService;
	}
	public UserService getUserService() {
		return userService;
	}
	@Resource
	public void setUserService(UserService userService) {
		this.userService = userService;
	}
	
}
