package com.hellogood.utils;

import com.hellogood.enumeration.ApiName;
import com.hellogood.http.aop.ApiReturnValue;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.apache.commons.beanutils.converters.*;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by kejian on 2017/5/21.
 */
public class BeaUtils extends BeanUtils{

    static {
        ConvertUtils.register(new LongConverter(null), Long.class);
        ConvertUtils.register(new ShortConverter(null), Short.class);
        ConvertUtils.register(new IntegerConverter(null), Integer.class);
        ConvertUtils.register(new DoubleConverter(null), Double.class);
        ConvertUtils.register(new BigDecimalConverter(null), BigDecimal.class);
        //注册sql.date的转换器，即允许BeanUtils.copyProperties时的源目标的sql类型的值允许为空
        ConvertUtils.register(new SqlDateConverter(), java.util.Date.class);
        //ConvertUtils.register(new SqlTimestampConverter(), java.sql.Timestamp.class);
        //注册util.date的转换器，即允许BeanUtils.copyProperties时的源目标的util类型的值允许为空
        ConvertUtils.register(new UtilDateConverter(), java.util.Date.class);
    }
    public static void copyProperties(Object target, Object source) throws
            InvocationTargetException, IllegalAccessException {
        //支持对日期copy
        org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);
    }

    public static void copyProperties(Object target, Object source, ApiName reqApiName) throws
            InvocationTargetException, IllegalAccessException {
        //支持对日期copy
        org.apache.commons.beanutils.BeanUtils.copyProperties(target, source);

        filterProperties(target, reqApiName);
    }

    /**
     * 依据API接口过滤属性
     * @param targetList
     * @param reqApiName
     * @return
     */
    public static List filterProperties(List targetList, ApiName reqApiName){
        for (Object object : targetList) {
            filterProperties(object, reqApiName);
        }
        return targetList;
    }

    /**
     * 依据API接口过滤属性
     * @param target
     * @param reqApiName
     * @return
     */
    public static Object filterProperties(Object target, ApiName reqApiName){
        Field[] fields = target.getClass().getDeclaredFields();

        try{
            for (Field field : fields) {

                //私有变量必须先设置Accessible为true
                field.setAccessible(true);

                if (!field.isAnnotationPresent(ApiReturnValue.class)) {
                    field.set(target, null);
                    continue;
                }

                ApiName[] apiNameArr = field.getAnnotation(ApiReturnValue.class).name();

                boolean exist = false;
                for (ApiName apiNameEnum : apiNameArr) {
                    if (apiNameEnum == reqApiName) {
                        exist = true;
                        break;
                    }
                }

                if (!exist) {
                    field.set(target, null);
                    continue;
                }
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return target;
    }
}
