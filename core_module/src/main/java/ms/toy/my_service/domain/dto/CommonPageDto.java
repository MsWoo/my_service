package ms.toy.my_service.domain.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommonPageDto<T> {
    private int pageIndex;
    private int pageSize;
    private long totalCount;
    private int totalPage;
    private List<T> list;
}
