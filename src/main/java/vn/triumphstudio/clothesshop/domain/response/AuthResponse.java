package vn.triumphstudio.clothesshop.domain.response;


import vn.triumphstudio.clothesshop.domain.enumration.Role;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

public class AuthResponse {

    private String accessToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String name;

    public AuthResponse(String accessToken, Role role, String name) {
        this.accessToken = accessToken;
        this.role = role;
        this.name = name;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
