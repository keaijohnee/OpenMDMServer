package com.jiangge.utils;
import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Map;
 
/**
 * 判断对象是否为空工具类
 * 
 * @author lihaoguo
 * @since 2014-05-01
 */
public class Objects
{
 
    public static boolean isNull(Object obj)
    {
        return obj == null;
    }
 
    public static boolean isNotNull(Object obj)
    {
        return !isNull(obj);
    }
 
    @SuppressWarnings("rawtypes")
    public static boolean isEmpty(Object obj)
    {
        if (obj == null)
        {
            return true;
        }
        if (obj instanceof CharSequence)
        {
            return ((CharSequence) obj).length() == 0;
        }
        if (obj instanceof Collection)
        {
            return ((Collection) obj).isEmpty();
        }
        if (obj instanceof Map)
        {
            return ((Map) obj).isEmpty();
        }
        if (obj.getClass().isArray())
        {
            return Array.getLength(obj) == 0;
        }
        return false;
    }
 
    public static boolean isNotEmpty(Object obj)
    {
        return !isEmpty(obj);
    }
}