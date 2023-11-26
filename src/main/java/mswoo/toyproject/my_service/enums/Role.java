package mswoo.toyproject.my_service.enums;

import lombok.Getter;

@Getter
public enum Role {
    USER("USER"),
    ADMIN("ADMIN"),
    SUPER("SUPER"),
    ;

    private String role;
    private String authority;

    Role(String role) {
        this.role = role;
        this.authority = "ROLE_" + role;
    }
}
