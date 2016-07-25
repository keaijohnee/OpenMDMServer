package com.jiangge.utils;
import java.io.IOException;
import java.util.Properties;
public class ReadPropertity {
        static Properties props = new Properties();
        static {
                try {
                        props.load(ReadPropertity.class.getClassLoader().getResourceAsStream("config.properties"));
                } catch (IOException e1) {
                        e1.printStackTrace();
                }
        }
        public static String getProperty(String key) {
                return props.getProperty(key);
        }
}