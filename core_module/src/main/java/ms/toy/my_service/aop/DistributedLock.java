package ms.toy.my_service.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface DistributedLock {
    // Lock을 걸 Key 값
    String key();

    // Lock을 기다리는 시간
    long waitTime() default 5L;

    // Lock을 획득하고 반납하는 시간
    long leaseTime() default 5L;

    // Lock 시간 단위
    TimeUnit timeUnit() default TimeUnit.SECONDS;
}
