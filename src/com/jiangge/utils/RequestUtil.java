package com.jiangge.utils;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

@SuppressWarnings("all")
public class RequestUtil {
	
	private static Logger logger = Logger.getLogger(RequestUtil.class);
	
	/**
	 * 获取request对象中的键/值对，存入map中并返回
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParam(HttpServletRequest request) {
		Map<String, String> map = new HashMap<String, String>();
		Enumeration<?> paramNames = request.getParameterNames();
		while (paramNames.hasMoreElements()) {
			String paramName = paramNames.nextElement().toString();
			String paramValue = request.getParameter(paramName);
			map.put(paramName, paramValue);
		}
		return map;
	}

	
	/**
	 * 从request中拿参数(过滤所有有可能会产生XSS攻击的特殊字符)
	 * 如果参数在request中不存在，则返回defaultValue
	 * @param request
	 * @param key
	 * @param defaultValue  
	 * @return
	 */
	public static String getString(HttpServletRequest request, String key, String defaultValue) {
		String value = request.getParameter(key);
		if (value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return StringUtil.escapeHTMLTag(value.trim());
	}
	
	/**
	 * 为textarea进行录入的方法
	 * @param request
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String getStringFromAreaText(HttpServletRequest request, String key, String defaultValue){
		String value = request.getParameter(key);
		if (value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return StringUtil.parserToHTMLForTextArea(value);
	}
	
	
	public static Integer getInt(HttpServletRequest request, String key, Integer defaultValue) {
		String value = request.getParameter(key);
		if (value == null || "".equals(value.trim())) {
			return defaultValue;
		}
		return Integer.valueOf(value.trim());
	}
	
	  public static String getOriginallyAddr(HttpServletRequest paramHttpServletRequest)
	  {
	    if (paramHttpServletRequest == null)
	      return "";
	    String str1 = paramHttpServletRequest.getHeader("X-Forwarded-For");
	    if (str1 == null)
	      return paramHttpServletRequest.getRemoteAddr();
	    if (str1.indexOf(",") != -1)
	    {
	      String str2 = str1.substring(0, str1.indexOf(","));
	      if (str2.substring(0, 3).equals("192"))
	        str2 = str1.substring(0, str1.lastIndexOf(",") + 1);
	      return str2;
	    }
	    if (!(isLicitIp(str1)))
	      return paramHttpServletRequest.getRemoteAddr();
	    return str1;
	  }
	  
	  public static boolean isLicitIp(String paramString)
	  {
	    if ((paramString == null) || (paramString.length() == 0))
	      return false;
	    String str = "^[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}$";
	    Pattern localPattern = Pattern.compile(str);
	    Matcher localMatcher = localPattern.matcher(paramString);
	    return (localMatcher.find());
	  }
	  
		/**
		 * 得到剔除过<html>，</html>,<script>,</script>过后的的字符串
		 * @param request
		 * @param key
		 * @param defaultValue
		 * @return
		 */
		public static String getSecureString(HttpServletRequest request, String key, String defaultValue) {
			String value=getString(request, key, defaultValue);
			
			if (value==null) return value;
			value.trim();
			
			return filterScript(value);

		}
		
		/**
		 * 去掉输入中的<html>，</html>,<script>,</script>过后的的字符串,把结果返回
		 * @param value 可以为null，则返回null
		 * @return
		 */
		public static String filterScript(String value) {
		   
		   if (value==null) return null;
			
		   String regex = "<html>|<script>|</html>|</script>"; 
	        // 不区分大小写  
	       Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);   
	       Matcher matcher = pattern.matcher(value);  
	       
	       String result = matcher.replaceAll(""); 
		   
	       return result;
		}
		
		/**
		 * 输入一个字符串数组，返回一个剔除掉<html>，</html>,<script>,</script>的字符串数组
		 * 如果输入为null，则返回null
		 * @param values
		 * @return
		 */
		public static String[] filterScript(String[] values ) {
			if (values==null) return null;
			
			String[] newString=new String[values.length];
			
			int i=0;
			for (String value:values) {
				String mid=filterScript(value);
				newString[i]=mid;
				i++;
			}
			
			return newString;
		}

	/**
	 * 从request中获得值,填充到fillObject中
	 * 要求request中参数名称以如下格式来定义 类名.属性名  (全部小写)，举例:person.name  person.middleschoolname	
	 * 对于checkbox这种，则转换为"value1,value2,value3,"这种形式进行赋值
	 * @param fillObject
	 * @param map
	 * @return 转换成功则返回true,否则则返回false
	 */
	public static boolean fillObjectFromRequest(Object fillObject, HttpServletRequest request) {
		   
		try {
			Map parameterMap = request.getParameterMap();
	        if (parameterMap == null)
	            return false;
	        
	        //object所对应的类名(全小写)
	        String objectClassName = fillObject.getClass().getSimpleName().toLowerCase();
	        
	        
	        Field[] fieldArray = fillObject.getClass().getDeclaredFields();
	        for (Field field:fieldArray) {
	        	
	            String key = objectClassName+"."+field.getName().toLowerCase();
	            String[] valueArray = (String[]) parameterMap.get(key);
	            
	               
	            if ( valueArray==null  ) {
	             	continue;
	            }
	            
	          
	            
	            String value = valueArray[0];
	            
	            field.setAccessible(true);
	              
	                //空数据的处理
	            if (valueArray.length==1 && StringUtil.isBlank(value)==true ) {
	                if (field.getType().equals(String.class) || field.getType().equals(Date.class) ) {
	                	field.set(fillObject, null);
	                }else if (field.getType().equals(int.class) || field.getType().equals(double.class) || 
	                	      field.getType().equals(long.class) ){
	                	field.set(fillObject, 0);
	                }else if (field.getType().equals(boolean.class)) {
	                	field.set(fillObject, true);
	                }
	            	
	            }else {
	            
	                if ( field.getType().equals(String.class) ) {
	                	if ( valueArray.length>1 ) {
	                	    StringBuilder strBuilder = new StringBuilder("");
	                		for (String str:valueArray) {
	                	    	strBuilder.append(str+",");
	                	    }	
	                		
	                		field.set(fillObject, StringUtil.escapeHTMLTag(strBuilder.toString()) );
	                	}else {
	                		field.set(fillObject, StringUtil.escapeHTMLTag(value));
	                	}
	                }else if ( field.getType().equals(int.class) ) {
	                	
	                	field.set(fillObject, Integer.valueOf( value) );
	                	
	                }else if ( field.getType().equals(Date.class)  ){
	                        //日期的处理
	                	Date date = DateUtil.StringtoDate(value, DateUtil.LONG_DATE_FORMAT);	
	                	field.set(fillObject, date);
	                	
	                }else if ( field.getType().equals(double.class)  ) {
	                	
	                	field.set(fillObject, Double.valueOf( value ) );
	                
	                }else if ( field.getType().equals(boolean.class) ) {
	                	
	                	if ( "0".equals( value ) ) {
	                		field.set(fillObject, false);
	                	}else if ( "1".equals( value) ){
	                		field.set(fillObject, true);
	                	}
	                	
	                }else if (field.getType().equals(long.class) ) {
	                	
	                	field.set(fillObject, Long.valueOf( value) );
	                }
	            }
		   }
		        

		        
	    } catch (Exception e) {
		      logger.error("RequestUtil.fillObjectFromRequest 转换出错"+e);
	    	
	    	return false;
		      
	    }
		
		return true;
		    
    }
	
}
