package com.st.newtest.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @title: NoRepeatSubmit
 * @Description:  自定义注解，用于标记Controller中的提交请求
 * @Author: ZouTao
 * @Date: 2020/4/14
 */
@Target(ElementType.METHOD)  // 作用到方法上
@Retention(RetentionPolicy.RUNTIME) // 运行时有效
public @interface NoRepeatSubmit {

}