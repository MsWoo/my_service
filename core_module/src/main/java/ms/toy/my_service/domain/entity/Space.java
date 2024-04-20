package ms.toy.my_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ms.toy.my_service.domain.dto.SpaceRequestDto;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "space")
@SuperBuilder
@Data
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class Space extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "spaceName", nullable = false, length = 10)
    private String spaceName;

    @Column(name = "spaceDescription", length = 30)
    private String spaceDescription;

    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    @ColumnDefault("'N'")
    @Column(name = "deleteYn", length = 1)
    private String deleteYn;

    public void update(SpaceRequestDto spaceRequestDto, String userId) {
        this.spaceName = spaceRequestDto.getSpaceName();
        this.spaceDescription = spaceRequestDto.getSpaceDescription();
        this.capacity = spaceRequestDto.getCapacity();
        super.update(userId);
    }
}
