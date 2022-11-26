package com.yadlings.usersservice.Service;

import com.yadlings.usersservice.Documents.User;
import com.yadlings.usersservice.Exceptions.UserExceptions;
import com.yadlings.usersservice.Repository.UserRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
@Log4j2
public class UserService{
    protected final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        log.info("User Service Initialized");
    }

    public ResponseEntity<?> saveUser(User user){
        userRepository.findByUsername(user.getUsername())
                .ifPresent(user1 -> {
                    throw new UserExceptions.AlreadyExist(String.format("%s is taken",user1.getId()));
                });
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User save = userRepository.save(user);
        HttpHeaders httpHeaders = new HttpHeaders();
        URI uri = ServletUriComponentsBuilder
                .fromCurrentRequestUri()
                .path("/{id}")
                .build(save.getId());
        httpHeaders.add("location",uri.toString());
        return new ResponseEntity<>(httpHeaders, HttpStatus.OK);
    }
    public ResponseEntity<User> getUserById(String id){
        return userRepository.findById(id)
                .map(user1 -> new ResponseEntity<>(user1, HttpStatus.OK))
                .orElseThrow(() -> new UserExceptions.UserNotFound(String.format("%s is taken",id)));

    }
    public ResponseEntity<?> getUserByUsername(String username){
        return userRepository.findByUsername(username)
                .map(user1 -> new ResponseEntity<>(user1, HttpStatus.OK))
                .orElseThrow(() -> new UserExceptions.UserNotFound(String.format("%s is taken",username)));

    }
    public ResponseEntity<List<User>> getUsers(){
        List<User> all = userRepository.findAll();
        if(all.size() == 0) throw  new UserExceptions.UsersNotFound("No users available");
        return new ResponseEntity<>(all, HttpStatus.OK);
    }
    public ResponseEntity<?> updateUser(User user,String id){
        return userRepository.findById(id)
                .map(user1 -> {
                    if(!user1.getPassword().equals(user.getPassword())) user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setId(id);
                    userRepository.save(user);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElseThrow(() -> new UserExceptions.UserNotFound(String.format("%s does not Exist", user.getId())));
    }
    public ResponseEntity<?> deleterById(String id){
        return userRepository.findById(id)
                .map(user1 -> {
                    userRepository.deleteById(id);
                    return new ResponseEntity<>(HttpStatus.OK);
                })
                .orElseThrow(() -> new UserExceptions.UserNotFound(String.format("%s is taken",id)));

    }
    public ResponseEntity<?> deleteAll(){
        userRepository.deleteAll();
        return  new ResponseEntity<>( HttpStatus.OK);
    }
    protected void some(){

    }
}
