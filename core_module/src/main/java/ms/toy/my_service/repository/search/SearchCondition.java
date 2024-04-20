package ms.toy.my_service.repository.search;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.util.StringUtils;

@Data
public class SearchCondition {
    private int pageIndex;
    private int pageSize;
    private String startSearchAt;
    private String endSearchAt;
    private String orders;

    public SearchCondition() {
        pageIndex = 1;
        pageSize = 10;
        // 기본으로 생성일 기준 내림차순 설정
        orders = "createdAt:desc";
    }

    public Pageable getPageable() {
        String[] ordersList = orders.split(";"); // ex) createdAt:desc;userId:asc;
        Sort sort = null;

        for (String order : ordersList) {
            if (StringUtils.hasText(order.trim())) {
                String[] orderList = order.split(":");
                Direction direction = orderList[1].equalsIgnoreCase("asc") ? Direction.ASC : Direction.DESC;
                String property = orderList[0];

                if (sort == null) {
                    sort = Sort.by(direction, property);
                } else {
                    sort = sort.and(Sort.by(direction, property));
                }
            }
        }

        return PageRequest.of(pageIndex - 1, pageSize, sort);
    }

    public List<OrderSpecifier> getOrderSpecifier(Sort sort, EntityPathBase<?> entity){

        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder pathBuilder = new PathBuilder(entity.getType(), entity.getMetadata());

            orders.add(new OrderSpecifier(direction, pathBuilder.get(property)));
        });

        return orders;
    }

}
