package ms.toy.my_service.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import ms.toy.my_service.domain.entity.Admin;
import ms.toy.my_service.domain.entity.QAdmin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

public class AdminSearchRepositoryImpl extends QuerydslRepositorySupport implements AdminSearchRepository {
    private final JPAQueryFactory queryFactory;

    public AdminSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Admin.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Admin> searchAdmin(AdminSearchCondition adminSearchCondition, Pageable pageable) {
        QAdmin admin = QAdmin.admin;

        BooleanBuilder booleanBuilder = makeSearchCondition(adminSearchCondition, admin);

        List<Admin> adminList = queryFactory.selectFrom(admin)
                .where(booleanBuilder)
                .orderBy(adminSearchCondition.getOrderSpecifier(pageable.getSort(), admin).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(adminSearchCondition.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(admin.count()).from(admin).where(booleanBuilder);

        return PageableExecutionUtils.getPage(adminList, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder makeSearchCondition(AdminSearchCondition adminSearchCondition , QAdmin admin){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (StringUtils.hasText(adminSearchCondition.getUserId())) {
            booleanBuilder.and(admin.userId.contains(adminSearchCondition.getUserId()));
        }

        if (StringUtils.hasText(adminSearchCondition.getUserName())) {
            booleanBuilder.and(admin.userName.contains(adminSearchCondition.getUserName()));
        }

        return booleanBuilder;
    }

}
