package ms.toy.my_service.aop;

import java.lang.reflect.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class DistributedLockAop {

    private static final String REDISSON_LOCK_PREFIX = "LOCK:";

    private final RedissonClient redissonClient;
    private final AopForTransaction aopForTransaction;

    @Around("@annotation(ms.toy.my_service.aop.DistributedLock)")
    public Object lock(final ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        DistributedLock distributedLock = method.getAnnotation(DistributedLock.class);

        String key = REDISSON_LOCK_PREFIX + CustomSpringELParser.getDynamicValue(signature.getParameterNames(), joinPoint.getArgs(), distributedLock.key());
        RLock lock = redissonClient.getLock(key);

        try {
            // 동일한 key (같은 날 같은 공간 예약)로 lock을 얻는데 실패했으면 늦게 들어온 요청으로 wait Time, lease Time을 설정하지 않아도 되지만, 학습 및 참고를 위해 사용
            boolean available = lock.tryLock(distributedLock.waitTime(), distributedLock.leaseTime(), distributedLock.timeUnit());

            if (!available) {
                return false;
            }

            return aopForTransaction.proceed(joinPoint);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        } finally {
            try {
                lock.unlock();
            } catch (IllegalMonitorStateException e) {
                log.info("Redisson Lock Already UnLock");
            }
        }
    }

}
