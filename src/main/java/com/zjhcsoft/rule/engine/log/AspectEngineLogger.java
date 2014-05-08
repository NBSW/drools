package com.zjhcsoft.rule.engine.log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


/**
 * Created with IntelliJ IDEA.
 * User: XuanLubin  Date: 14-2-28  Time: 上午10:07
 */
@Component
@Aspect
public class AspectEngineLogger {
    private static Logger logger = LoggerFactory.getLogger(AspectEngineLogger.class);

    @Pointcut("execution(* com.zjhcsoft.rule.*.event.*Listener.*Execute(..))")
    public void aspect() {
    }

    @Around("aspect()")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        Signature signature = joinPoint.getSignature();
        logger.debug("{} {}", signature.getDeclaringTypeName(),signature.getName());
        try {
            Object object = joinPoint.proceed();
            return object;
        } catch (Throwable throwable) {
            throw throwable;
        }
    }
}
