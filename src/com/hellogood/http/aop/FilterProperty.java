package com.hellogood.http.aop;

import com.hellogood.enumeration.ApiName;

import java.lang.annotation.*;

/**
 * Created by yanyuan on 2017/4/19.
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface FilterProperty {
    ApiName name() ;
}
