package com.jiangge.utils;
import java.io.IOException;
import java.util.Properties;

/**
 * 获取配置信息的工具类
 * @author jiang.li
 */
public class ConfigUtils {
    static Properties props = new Properties();
    /**加载配置文件**/
    static {
        try {
            props.load(ConfigUtils.class.getResourceAsStream("/config.properties"));
        } catch (IOException e1) {
            e1.printStackTrace();
        }
    }
    /**根据KEY获取值**/
    public static String getConfig(String key) {
        return props.getProperty(key);
    }
}
