package ms.toy.my_service.repository.search;

import lombok.Data;

@Data
public class AdminSearchCondition extends SearchCondition {
    private String userId;
    private String userName;
}
