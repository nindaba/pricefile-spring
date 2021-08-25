package com.yadlings.usersservice.Controller;

import com.yadlings.usersservice.Documents.User;
import com.yadlings.usersservice.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/price-check/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping
    private ResponseEntity<?> getUsers(){
        return userService.getUsers();
    }
    @GetMapping("/{id}")
    private ResponseEntity<?> getUser(@PathVariable String id){
        return userService.getUserById(id);
    }
    @PostMapping
    private ResponseEntity<?> saveUser(@Valid @RequestBody User user){
        return userService.saveUser(user);
    }
    @DeleteMapping("/{id}")
    private ResponseEntity<?> delete(@PathVariable String Id){
        return userService.deleterById(Id);
    }
    @DeleteMapping
    private ResponseEntity<?> deleteUsers(){
        return userService.deleteAll();
    }
    @PutMapping("/{id}")
    private ResponseEntity<?> updateUser(@Valid @RequestBody User user,@PathVariable String id){
        return userService.updateUser(user,id);
    }

}
