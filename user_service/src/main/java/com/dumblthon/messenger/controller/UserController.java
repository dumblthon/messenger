package com.dumblthon.messenger.controller;

import com.dumblthon.messenger.entities.User;
import com.dumblthon.messenger.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RestController
@RequestMapping(path="/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping(path="/add")
    public @ResponseBody String addNewUser(@RequestBody @Validated User user) {
        // @ResponseBody means the returned String is the response, not a view name
        userService.addNewUser(user);
        return "Saved";
    }

    @PostMapping(path="/update")
    public @ResponseBody String updateUser(@RequestBody @Validated User user) {
        userService.updateUser(user);
        return "Updated";
    }

    @GetMapping(path="/all")
    public @ResponseBody Iterable<User> getAllUsers() {
        // This returns a JSON
        return userService.allUsers();
    }

}
