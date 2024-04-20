package ms.toy.my_service.repository.search;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import ms.toy.my_service.domain.entity.QUsers;
import ms.toy.my_service.domain.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

public class UserSearchRepositoryImpl extends QuerydslRepositorySupport implements UserSearchRepository {
    private final JPAQueryFactory queryFactory;

    public UserSearchRepositoryImpl(JPAQueryFactory queryFactory) {
        super(Users.class);
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Users> searchUser(UserSearchCondition searchCondition, Pageable pageable) {
        QUsers users = QUsers.users;

        BooleanBuilder booleanBuilder = makeSearchCondition(searchCondition, users);

        List<Users> userList = queryFactory.selectFrom(users)
                .where(booleanBuilder)
                .orderBy(searchCondition.getOrderSpecifier(pageable.getSort(), users).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(searchCondition.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = queryFactory.select(users.count()).from(users).where(booleanBuilder);

        return PageableExecutionUtils.getPage(userList, pageable, countQuery::fetchOne);
    }

    private BooleanBuilder makeSearchCondition(UserSearchCondition searchCondition , QUsers users){
        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (StringUtils.hasText(searchCondition.getUserId())) {
            booleanBuilder.and(users.userId.eq(searchCondition.getUserId()));
        }

        if (StringUtils.hasText(searchCondition.getUserName())) {
            booleanBuilder.and(users.userName.eq(searchCondition.getUserName()));
        }

        return booleanBuilder;
    }

}
