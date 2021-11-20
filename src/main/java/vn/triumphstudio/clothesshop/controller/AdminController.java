package vn.triumphstudio.clothesshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @PatchMapping("/users/{id}")
    public UserEntity updateUser(@PathVariable long id) {
        return null;
    }

}
