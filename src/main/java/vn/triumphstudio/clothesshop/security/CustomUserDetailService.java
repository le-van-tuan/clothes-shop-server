package vn.triumphstudio.clothesshop.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.exception.BusinessLogicException;
import vn.triumphstudio.clothesshop.service.UserService;

import javax.transaction.Transactional;

@Service
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity user = userService.getUserByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("User not found with email = " + email);
        }
        return new UserPrincipal(user);
    }

    @Transactional
    public UserDetails loadUserById(long id) {
        UserEntity user = userService.getUserById(id);
        if (user == null) {
            throw new BusinessLogicException("No user found with id = " + id);
        }
        return new UserPrincipal(user);
    }
}
