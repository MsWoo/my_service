package ms.toy.my_service.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER", 1L),
    ADMIN("ADMIN", 2L),
    ;

    private String role;
    private String authority;
    private Long authorityId;

    Role(String role, Long authorityId) {
        this.role = role;
        this.authority = "ROLE_" + role;
        this.authorityId = authorityId;
    }
}
