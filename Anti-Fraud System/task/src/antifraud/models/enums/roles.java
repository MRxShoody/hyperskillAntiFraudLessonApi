package antifraud.models.enums;

import antifraud.persitence.models.user.role;

public enum roles {

    ADMIN("ADMINISTRATOR"),
    MERCHANT("MERCHANT"),
    SUPPORT("SUPPORT");

    private final role role;

    private final String roleName;

    roles(String roleName) {
        this.roleName = roleName;
        this.role = new role(roleName);
    }

    public String getRoleName() {
        return roleName;
    }

    public role getRole() {
        return role;
    }
}
