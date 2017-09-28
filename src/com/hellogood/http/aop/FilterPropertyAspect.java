package com.hellogood.http.aop;

import com.hellogood.enumeration.ApiName;
import com.hellogood.utils.DateUtil;
import com.hellogood.utils.BeaUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by kejian on 2017/4/19.
 */
@Aspect
@Component
public class FilterPropertyAspect {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    //Controller层切点
    @Pointcut("@annotation(com.hellogood.http.aop.FilterProperty)")
    public void filterPropertyControllerAspect() {
        logger.info("filterPropertyControllerAspect ........");
    }

    /**
     * 前置通知 用于拦截Controller层记录用户的操作
     *
     * @param proceedingJoinPoint 切点
     */
    @Around("filterPropertyControllerAspect()")
    public Map<String, Object> around(ProceedingJoinPoint proceedingJoinPoint) {
        logger.info("Around.......");
        Map<String, Object> resultMap = new HashMap();
        Object resultObject = null;
        try {
            /**
             * 获取接口名
             */
            ApiName name  = getControllerOperatingType(proceedingJoinPoint);
            logger.info(name.toString());

            resultObject = proceedingJoinPoint.proceed();

            if(resultObject instanceof Map){

                resultMap = (Map<String, Object>) proceedingJoinPoint.proceed();
                List list = (List) resultMap.get("dataList");

                if(list != null){
                    resultMap.put("dataList", DateUtil.list2MapDateFormat(BeaUtils.filterProperties(list, name)));

                }

                Object data = resultMap.get("data");
                if(data != null){
                    resultMap.put("data", DateUtil.object2MapDateFormat(BeaUtils.filterProperties(data, name)));
                }
            }
        } catch (Exception e){
            logger.error("过滤属性: " + e.getMessage());
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return resultMap;
    }


    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param proceedingJoinPoint 切点
     * @return 方法描述
     * @throws Exception
     */
    public static ApiName getControllerOperatingType(ProceedingJoinPoint proceedingJoinPoint) throws Exception {
        String targetName = proceedingJoinPoint.getTarget().getClass().getName();
        String methodName = proceedingJoinPoint.getSignature().getName();
        Object[] arguments = proceedingJoinPoint.getArgs();
        Class targetClass = Class.forName(targetName);
        Method[] methods = targetClass.getMethods();
        ApiName name = null;
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                Class[] clazzs = method.getParameterTypes();
                if (clazzs.length == arguments.length) {
                    name = method.getAnnotation(FilterProperty.class).name();
                    break;
                }
            }
        }
        return name;
    }

}
