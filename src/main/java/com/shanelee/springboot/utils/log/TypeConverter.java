package com.shanelee.springboot.utils.log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

public class TypeConverter {

    public static String getResultType(Object arg){
        if (arg == null) {
            return StringUtils.EMPTY;
        }
        try {
            return arg.getClass().getSimpleName() + ":" + JSON.toJSONString(arg, SerializerFeature.WriteDateUseDateFormat);
        } catch (Throwable e) {
            return StringUtils.EMPTY;
        }
    }

    public static String changeType(Object arg) {
        if (arg == null) {
            return StringUtils.EMPTY;
        }
        if (arg instanceof String || arg instanceof BigDecimal) {
            return String.valueOf(arg);
        }
        if (isPrimitive(arg)) {
            return String.valueOf(arg);
        } else {
            try {
                return arg.getClass().getSimpleName() + ":" + JSON.toJSONString(arg, SerializerFeature.WriteDateUseDateFormat);
            } catch (Throwable e) {
                return StringUtils.EMPTY;
            }
        }
    }

    private static boolean isPrimitive(Object obj) {
        try {
            return ((Class<?>) obj.getClass().getField("TYPE").get(null)).isPrimitive();
        } catch (Throwable e) {
            return false;
        }
    }

}
