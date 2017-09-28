package com.hellogood.http.aop;

import com.hellogood.enumeration.ApiName;

import java.lang.annotation.*;

/**
 * Created by kejian on 2017/4/19.
 */
@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ApiReturnValue {
    ApiName[] name() default {};
}
