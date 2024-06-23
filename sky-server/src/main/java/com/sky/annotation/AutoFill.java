package com.sky.annotation;

import com.sky.enumeration.OperationType;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义注解，用于标识某个方法需要进行功能字段自动填充处理
 */
// 自定义注解（即在 @Target 注解之后的注解）可以标注在方法上，而不能标注在其他类型的元素（如类、字段、参数等）上
@Target(ElementType.METHOD)
// 自定义注解在运行时也会被保留，并且可以通过反射机制读取到
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoFill {
    //数据库操作类型：UPDATE INSERT
    OperationType value();
}
