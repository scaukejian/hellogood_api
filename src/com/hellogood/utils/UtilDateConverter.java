package com.hellogood.utils;

import org.apache.commons.beanutils.Converter;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by kejian on 2017/5/21.
 */
public class UtilDateConverter implements Converter {
    public Object convert(Class type, Object value) {
        if (value == null) {
            return value;
        }
        if (value instanceof Date) {//instanceof判断是否属于此类型
            return value;
        }
        Date date = null;
        if (value instanceof String) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = sdf.parse(value.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return date;
    }

    public void test(){
    }
}
