package ms.toy.my_service.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import ms.toy.my_service.domain.dto.AdminEditDto;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

@Entity
@Table(name = "admin")
@SuperBuilder
@Data
@DynamicInsert
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
public class Admin extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "authorityId", nullable = false)
    private Long authorityId;

    @Column(name = "userId", nullable = false, length = 20)
    private String userId;

    @Column(name = "userName", nullable = false, length = 10)
    private String userName;

    @Column(name = "phoneNumber", nullable = false, length = 15)
    private String phoneNumber;

    @Column(name = "password", nullable = false, length = 100)
    private String password;

    @ColumnDefault("'N'")
    @Column(name = "deleteYn", length = 1)
    private String deleteYn;

    @ColumnDefault("0")
    @Column(name = "failCount", length = 1)
    private Integer failCount;

    @Column(name = "loginLockTime")
    private LocalDateTime loginLockTime;


    public void increaseFailCount() {
        this.failCount = this.failCount + 1;
        if (this.failCount >= 5) {
            this.failCount = 0;
            this.loginLockTime = LocalDateTime.now().plusMinutes(30);
        }
    }

    public void initLoginFail() {
        this.failCount = 0;
        this.loginLockTime = null;
    }

    public void update(AdminEditDto adminEditDto, String userId) {
        this.userName = adminEditDto.getUserName();
        this.phoneNumber = adminEditDto.getPhoneNumber();
        super.update(userId);
    }
}
