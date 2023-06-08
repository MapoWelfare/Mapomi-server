package net.mapomi.mapomi.common;

import lombok.RequiredArgsConstructor;
import net.mapomi.mapomi.common.error.UserNotLoginException;
import net.mapomi.mapomi.domain.Role;
import net.mapomi.mapomi.domain.user.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UserUtils {
    private final UserAdaptor userAdaptor;
    private static SimpleGrantedAuthority anonymous = new SimpleGrantedAuthority("ROLE_ANONYMOUS");
    private static SimpleGrantedAuthority swagger = new SimpleGrantedAuthority("ROLE_SWAGGER");

    private static List<SimpleGrantedAuthority> notUserAuthority = List.of(anonymous, swagger);

    public Long getCurrentUserId() {
        return getContextHolderId();
    }

    public User getCurrentUser() {
        return userAdaptor.queryUser(getCurrentUserId());
    }
    public User getCurrentUser(String role) {
        return userAdaptor.queryUser(getCurrentUserId(), role);
    }

    public static Long getContextHolderId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new UserNotLoginException();
        }
        if (authentication.isAuthenticated()&& !CollectionUtils.containsAny(
                authentication.getAuthorities(), notUserAuthority)){
            return Long.valueOf(authentication.getName());
        }

        throw new UserNotLoginException();
    }
}
