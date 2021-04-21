package com.shanelee.springboot.utils.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.*;

@Slf4j
@Component
@Aspect
public class MethodAroundLogInterceptor {

    @Around("@com.shanelee.springboot.utils.log.MethodAroundLog)")
    public Object paramLogger(ProceedingJoinPoint joinPoint) throws Throwable {
        //若不是方法则直接返回
        if (!isMethodSignature(joinPoint)) {
            return joinPoint.proceed();
        }
        //若没有注解则直接返回
        MethodAroundLog paramLog = getMethodAnnotation(joinPoint);
        if (null == paramLog) {
            return joinPoint.proceed();
        }
        MethodAroundLog.Level level = paramLog.LEVEL();

        //报错信息
        String errorMessage = "";
        //执行方法
        Object returnValue = null;
        Long start = System.currentTimeMillis();
        try {
            returnValue = joinPoint.proceed();
        } catch (RuntimeException e) {
            errorMessage = e.getClass().getName() + ":" + e.getMessage();
            throw e;
        } catch (Throwable e) {
            errorMessage = e.getClass().getName() + ":" + e.getMessage();
            throw e;
        } finally {
            //生成日志
            String printContent = buildLogContent(paramLog, start, joinPoint, returnValue, errorMessage);
            doPrint(level, printContent);
        }
        return returnValue;
    }

    private String buildLogContent(MethodAroundLog paramLog, Long start,
                                   ProceedingJoinPoint joinPoint, Object returnValue, String errorMessage) {
        Long costTime = System.currentTimeMillis() - start;
        String reqParam = getReqParam(joinPoint, paramLog);
        String methodName = this.getMethodName(paramLog, getSignatureName(joinPoint));
        String result = "";
        if (null != returnValue && !(returnValue instanceof Void)) {
            result = TypeConverter.getResultType(returnValue);
        }
        String printContent = MessageFormat.format(
                "调用开始:{0} 方法名称:{1} 参数:{2};调用结束 消耗时间:{3}ms 返回值:{4} ;",
                paramLog.remark(), methodName, reqParam, costTime, result);
        if (StringUtils.isNotEmpty(errorMessage)) {
            printContent += "错误内容:" + errorMessage;
        }
        return printContent;
    }

    /**
     * 获取请求参数
     */
    private String getReqParam(ProceedingJoinPoint pjp, MethodAroundLog paramLog) {
        Object[] args = pjp.getArgs();
        Map<String, Object> paramMap = new HashMap<>();

        Signature signature = pjp.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        String[] parameterNames = methodSignature.getParameterNames();
        if (null == parameterNames) {
            return "";
        }
        String[] exclude = paramLog.excludeParam();
        for (int i = 0; i < args.length; i++) {
            if (Arrays.asList(exclude).contains(parameterNames[i])) {
                continue;
            }
            paramMap.put(parameterNames[i], TypeConverter.changeType(args[i]));
        }
        List<String> paramList = new ArrayList<>();
        Object value;
        for (String key : paramMap.keySet()) {
            value = paramMap.get(key);
            paramList.add(key + ":[" + TypeConverter.changeType(value) + "]");
        }
        if (!CollectionUtils.isEmpty(paramList)) {
            return String.join(",", paramList);
        }
        return "";
    }

    /**
     * 是否是方法（该注解只允许方法通过）
     * @param pjp
     * @return
     */
    private boolean isMethodSignature(ProceedingJoinPoint pjp) {
        Signature sig = pjp.getSignature();
        return (sig instanceof MethodSignature);
    }

    /**
     * 方法上是否有当前注解
     */
    private MethodAroundLog getMethodAnnotation(ProceedingJoinPoint pjp) {
        Signature sig = pjp.getSignature();
        MethodSignature msig = (MethodSignature) sig;
        Object target = pjp.getTarget();
        //获取当前方法
        Method currentMethod;
        try {
            currentMethod = target.getClass().getMethod(msig.getName(), msig.getParameterTypes());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            return null;
        }
        //获取paramLogger注解
        return currentMethod.getAnnotation(MethodAroundLog.class);
    }

    /**
     * 打印
     */
    private void doPrint(MethodAroundLog.Level level, String printContent) {
        //打印级别
        try {
            switch (level) {
                case TRACE:
                    log.trace(printContent);
                    break;
                case DEBUG:
                    log.debug(printContent);
                    break;
                case INFO:
                    log.info(printContent);
                    break;
                case WARN:
                    log.warn(printContent);
                    break;
                case ERROR:
                    log.error(printContent);
                    break;
                default:
            }
        } catch (Exception e) {
            log.error("paramLog error", e);
        }
    }

    /**
     * 方法名称
     */
    private String getMethodName(MethodAroundLog paramLog, String defaultName) {
        //方法名
        if (!StringUtils.isEmpty(paramLog.methodName())) {
            //当methodName为默认值时，用signature(原方法名)代替
            return paramLog.methodName();
        }
        return defaultName;
    }

    private String getSignatureName(ProceedingJoinPoint pjp) {
        if (Objects.nonNull(pjp.getSignature())) {
            return pjp.getSignature().toShortString();
        }
        return StringUtils.EMPTY;
    }
}