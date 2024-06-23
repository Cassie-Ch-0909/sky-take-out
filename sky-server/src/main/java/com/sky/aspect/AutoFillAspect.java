package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面，实现公共字段自动填充处理逻辑实现思路：
 * 1. 自定义注解AutoFill,用于标识需要进行公共字段自动填充的方法
 * 2. 自定义切面类AutoFillAspect, 统一拦截加入了AutoFill注解的方法，通过反射为公共字段赋值
 * insert时，create_time和create_user需要赋值
 * insert和update时，create_time create_user update_time update_user需要赋值
 * 3. 在Mapper方法上加入AutoFill注解
 */

// Spring 框架中用于声明切面的注解
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    /**
     * 切入点: 明确对哪些类的哪些方法来进行拦截
     * com.sky.mapper表示com.sky.mapper 包及其所有子包
     * *.*：前面的*表示所有类，后面的*表示所有方法
     * com.sky.mapper表示对 com.sky.mapper 包中所有类的所有方法都将应用该切面定义的通知（Advice）逻辑。
     *
     * @annotation(com.sky.annotation.AutoFill)：匹配所有被 com.sky.annotation.AutoFill 注解标注的方法
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    /**
     * 前置通知，在通知中进行公共字段的赋值
     * 当匹配上autoFillPointCut()切点，则执行该方法
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint){
        log.info("开始进行公共字段自动填充...");

        //获取到当前被拦截的方法上的数据库操作类型
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();//方法签名对象
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);//获得方法上的注解对象
        OperationType operationType = autoFill.value();//获得数据库操作类型

        //获取到当前被拦截的方法的参数--实体对象
        Object[] args = joinPoint.getArgs();
        if(args == null || args.length == 0){
            return;
        }

        // 实体类是不确定的，可能是Employee，可能是Category，也可能是其他，所以这边是Object类型
        Object entity = args[0];

        //准备赋值的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        //根据当前不同的操作类型，为对应的属性通过反射来赋值
        if(operationType == OperationType.INSERT){
            //为4个公共字段赋值
            try {
                /*
                * 在 entity 的类中查找一个名为 AutoFillConstant.SET_CREATE_TIME（假设其值为 "setCreateTime"）的方法，
                * 这个方法接受一个 LocalDateTime 类型的参数，并将这个方法对象赋值给 setCreateTime 变量。
                * */
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setCreateTime.invoke(entity,now);
                setCreateUser.invoke(entity,currentId);
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if(operationType == OperationType.UPDATE){
            //为2个公共字段赋值
            try {
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);

                //通过反射为对象属性赋值
                setUpdateTime.invoke(entity,now);
                setUpdateUser.invoke(entity,currentId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
