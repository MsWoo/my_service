package ms.toy.my_service.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import ms.toy.my_service.domain.entity.QSpace;
import ms.toy.my_service.domain.entity.Space;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

public class SpaceSearchRepositoryImpl extends QuerydslRepositorySupport implements SpaceSearchRepository {

    private final JPAQueryFactory queryFactory;

    public SpaceSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Space.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Space> searchSpace(SpaceSearchCondition searchCondition, Pageable pageable) {
        QSpace space = QSpace.space;

        BooleanBuilder booleanBuilder = makeSearchCondition(searchCondition, space);

        List<Space> spaceList = queryFactory.selectFrom(space)
                .where(booleanBuilder)
                .orderBy(searchCondition.getOrderSpecifier(pageable.getSort(), space).toArray(
                        OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(searchCondition.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(space.count()).from(space).where(booleanBuilder);

        return PageableExecutionUtils.getPage(spaceList, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder makeSearchCondition(SpaceSearchCondition searchCondition , QSpace space){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (StringUtils.hasText(searchCondition.getSpaceName())) {
            booleanBuilder.and(space.spaceName.eq(searchCondition.getSpaceName()));
        }

        return booleanBuilder;
    }
}
