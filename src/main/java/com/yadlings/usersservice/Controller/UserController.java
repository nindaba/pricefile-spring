package com.yadlings.usersservice.Controller;

import com.yadlings.usersservice.Documents.User;
import com.yadlings.usersservice.Service.UserService;
import com.yadlings.usersservice.Service.WiringService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@Log4j2
@RestController
@RequestMapping("/users")
public class UserController {

    @PostConstruct
    public void afterConstructor(){
        log.info("Controller was initialized with {}", userService.getUsers().getStatusCode());
    }
    @Resource(name = "userService")
    private UserService userService;
    @Resource(name = "wiringService")
    private WiringService wiringService;

    @Secured({"ROLE_BASIC"})
    @GetMapping(path = "/wire/{color}")
    public ResponseEntity<String> getWire(@PathVariable String color){
        return ResponseEntity.ok(wiringService.getWire(color));
    }

    @GetMapping
    public ResponseEntity<List<User>> getUsers(){
        return userService.getUsers();
    }
    @Secured({"ROLE_BASIC"})
    @GetMapping(path = "/{id}")
    public ResponseEntity<User> getUser(@PathVariable String id){
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
