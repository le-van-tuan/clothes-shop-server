package vn.triumphstudio.clothesshop.util;

import org.springframework.security.core.context.SecurityContextHolder;
import vn.triumphstudio.clothesshop.security.UserPrincipal;

public final class SecurityContextUtil {

    private SecurityContextUtil() {
        throw new IllegalStateException("Utility class.");
    }

    public static UserPrincipal getCurrentUser() {
        return (UserPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public static boolean hasLoggedIn() {
        try {
            return SecurityContextUtil.getCurrentUser() != null;
        } catch (Exception e) {
            return false;
        }
    }
}
