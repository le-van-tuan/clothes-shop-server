package vn.triumphstudio.clothesshop.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import vn.triumphstudio.clothesshop.domain.annotation.AllowAdmin;
import vn.triumphstudio.clothesshop.domain.entity.UserEntity;
import vn.triumphstudio.clothesshop.service.UserService;

import java.util.List;

@RestController
@RequestMapping("/admin")
@AllowAdmin
public class AdminController {

    @Autowired
    private UserService userService;

    @GetMapping("/users")
    public List<UserEntity> getAllUsers() {
        return this.userService.getAllUsers();
    }

    @GetMapping("/users/{id}/toggle-status")
    public UserEntity updateUser(@PathVariable long id) {
        return this.userService.toggleUserStatus(id);
    }
}
