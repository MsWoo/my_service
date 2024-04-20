package ms.toy.my_service.repository.search;

import lombok.Data;

@Data
public class UserSearchCondition extends SearchCondition {
    private String userId;
    private String userName;
}
