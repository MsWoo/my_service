package ms.toy.my_service.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * DistributedLock 사용 시 동시성 환경에서 데이터 정합성을 보장해주는 클래스
 */
@Component
public class AopForTransaction {

    // 부모 트랜잭션의 유무에 관계없이 별도의 트랜잭션으로 동작
    // Propagation.REQUIRES_NEW : 현재 트랜잭션이 종료될 때까지 대기한 후 새로운 트랜잭션을 생성하고 실행
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public Object proceed(final ProceedingJoinPoint joinPoint) throws Throwable {
        return joinPoint.proceed();
    }
}
