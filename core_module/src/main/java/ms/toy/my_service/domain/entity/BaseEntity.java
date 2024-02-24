package ms.toy.my_service.domain.entity;

import jakarta.persistence.MappedSuperclass;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

@SuperBuilder
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public class BaseEntity {
    @CreationTimestamp
    private LocalDateTime createdAt;
    private String createdBy;
    @UpdateTimestamp
    private LocalDateTime updatedAt;
    private String updatedBy;

    public void update(String userId){
        this.updatedBy = userId;
    }
}
