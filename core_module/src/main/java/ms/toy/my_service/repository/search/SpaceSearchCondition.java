package ms.toy.my_service.repository.search;

import lombok.Data;

@Data
public class SpaceSearchCondition extends SearchCondition {
    private String spaceName;
}
