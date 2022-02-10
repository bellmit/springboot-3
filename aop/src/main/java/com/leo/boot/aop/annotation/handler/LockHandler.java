package com.leo.boot.aop.annotation.handler;

import com.leo.boot.aop.annotation.Lock;
import com.leo.boot.aop.lock.RedisLock;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
@Aspect
public class LockHandler {

    @Autowired
    private RedisLock redisLock;

    @Around("@annotation(com.leo.boot.aop.annotation.Lock)")
    public Object handle(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature methodSignature = ((MethodSignature) joinPoint.getSignature());
        Lock lock = methodSignature.getMethod().getAnnotation(Lock.class);

        String lockKey = lock.key();
        if (!StringUtils.isEmpty(lock.key())) {
            lockKey = "lock:" + methodSignature + ":%s";
        }
        if (!StringUtils.isEmpty(lock.id())) {
            String lockId = SpelContext.of(methodSignature, joinPoint.getArgs()).getValue(lock.id());
            lockKey = String.format(lock.key(), lockId);
        } else {
            lockKey = String.format(lockKey, "all");
        }
        String lockTS = String.valueOf(System.currentTimeMillis() + lock.timeout());
        int retryTimes = lock.retryTimes();

        if (redisLock.lock(lockKey, lockTS, retryTimes)) {
            try {
                return joinPoint.proceed();
            } finally {
                redisLock.unlock(lockKey, lockTS);
            }
        } else if (lock.interrupt()) {
            throw new IllegalStateException("unable to acquire lock");
        } else {
            return null;
        }
    }

}
