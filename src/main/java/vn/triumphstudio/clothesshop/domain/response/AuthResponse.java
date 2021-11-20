package vn.triumphstudio.clothesshop.domain.response;


import vn.triumphstudio.clothesshop.domain.enumration.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class AuthResponse {

    private String accessToken;

    @Enumerated(EnumType.STRING)
    private Role role = Role.ROLE_USER;

    public AuthResponse(String accessToken, Role role) {
        this.accessToken = accessToken;
        this.role = role;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
