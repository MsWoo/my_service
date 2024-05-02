package ms.toy.my_service.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import ms.toy.my_service.domain.entity.QReservation;
import ms.toy.my_service.domain.entity.Reservation;
import ms.toy.my_service.domain.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.ObjectUtils;

public class ReservationSearchRepositoryImpl extends QuerydslRepositorySupport implements ReservationSearchRepository {

    private final JPAQueryFactory queryFactory;

    public ReservationSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Space.class);
        this.queryFactory = queryFactory;
    }


    @Override
    public Page<Reservation> searchReservation(ReservationSearchCondition searchCondition, Pageable pageable) {
        QReservation reservation = QReservation.reservation;

        BooleanBuilder booleanBuilder = makeSearchCondition(searchCondition, reservation);

        List<Reservation> reservationList = queryFactory.selectFrom(reservation)
                .where(booleanBuilder)
                .orderBy(searchCondition.getOrderSpecifier(pageable.getSort(), reservation).toArray(
                        OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(searchCondition.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(reservation.count()).from(reservation).where(booleanBuilder);

        return PageableExecutionUtils.getPage(reservationList, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder makeSearchCondition(ReservationSearchCondition searchCondition , QReservation reservation){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (!ObjectUtils.isEmpty(searchCondition.getUserId())) {
            booleanBuilder.and(reservation.userId.eq(searchCondition.getUserId()));
        }

        if (!ObjectUtils.isEmpty(searchCondition.getSpaceId())) {
            booleanBuilder.and(reservation.space.id.eq(searchCondition.getSpaceId()));
        }

        if (!ObjectUtils.isEmpty(searchCondition.getStatus())) {
            booleanBuilder.and(reservation.status.eq(searchCondition.getStatus()));
        }

        if (!ObjectUtils.isEmpty(searchCondition.getReservationDate())) {
            LocalDateTime startOfDay = searchCondition.getReservationDate().atStartOfDay();
            LocalDateTime endOfDay = searchCondition.getReservationDate().atTime(23, 59, 59);
            booleanBuilder.and(reservation.reservationStartDt.goe(startOfDay)).and(reservation.reservationEndDt.loe(endOfDay));
        }

        return booleanBuilder;
    }

}
