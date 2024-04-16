package ssac.LMS.domain;

public enum Role {
    ROLE_STUDENT("student"),
    ROLE_INSTRUCTOR("instructor"),
    ROLE_ADMIN("admin");

    private final String value;

    Role(String value) {
        this.value = value;
    }

    public static Role fromValue(String value) {
        for (Role role : Role.values()) {
            if (role.value.equals(value)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid Role value: " + value);
    }
}
