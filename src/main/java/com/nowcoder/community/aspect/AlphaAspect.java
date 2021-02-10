package com.nowcoder.community.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

/**
 * @Author 杜俊宏
 * Date on 2021/2/9 23:56
 */

//@Component
//@Aspect
public class AlphaAspect {

    @Pointcut("execution(* com.nowcoder.community.Service.*.*(..))")
    public void AlphaPointCut() {

    }

    @Before("AlphaPointCut()")
    public void before() {
        System.out.println("before");
    }

    @After("AlphaPointCut()")
    public void after() {
        System.out.println("after");
    }

    @AfterReturning("AlphaPointCut()")
    public void afterReturing() {
        System.out.println("afterReturing");
    }

    @AfterThrowing("AlphaPointCut()")
    public void afterThrowing() {
        System.out.println("afterThrowing");
    }


    @Around("AlphaPointCut()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("around before");
        Object object = joinPoint.proceed();
        System.out.println("around after");
        return object;
    }

}
