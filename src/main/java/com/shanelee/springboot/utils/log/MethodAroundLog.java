package com.shanelee.springboot.utils.log;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD})
@Inherited
@Retention(RetentionPolicy.RUNTIME)
public @interface MethodAroundLog {
    //日志级别
    Level LEVEL() default Level.INFO;

    //方法名 (覆盖默认方法名)
    String methodName() default "";

    //备注
    String remark() default "";

    //不打印的参数名称1
    String[] excludeParam() default {};

    boolean isPrintStart() default false;

    boolean isPrintEnd() default true;

    enum Level{
        TRACE, DEBUG, INFO, WARN, ERROR
    }
}
