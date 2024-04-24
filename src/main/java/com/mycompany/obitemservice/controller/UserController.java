package com.mycompany.obitemservice.controller;



import com.mycompany.obitemservice.model.ItemModel;
import com.mycompany.obitemservice.model.UserModel;
import com.mycompany.obitemservice.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1")


public class UserController {

    @Autowired
    private UserRepository userRepository;


    @GetMapping("/users")
    public List<UserModel> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/users/{id}")
    public UserModel getUser(@PathVariable String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot Find Item By ID: " + id));
    }

    @GetMapping("/register")
    public LoginResponse saveUser(@RequestParam String username, @RequestParam String password) {
        // Check if a user with the same username already exists

        UserModel existingUser = userRepository.findByUsername(username);
        if (existingUser != null) {
            return new LoginResponse(false, "username already used", null);
        } else {

            // If the username is unique, save the new user
            UserModel savedUser = userRepository.save(new UserModel(username,password));
            URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(savedUser.getId())
                    .toUri();
            return new LoginResponse(true, "Register successful", new UserModel(username,password));
        }
    }




    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


    @GetMapping("/login")
    public LoginResponse login(@RequestParam String username, @RequestParam String password) {
        UserModel user = userRepository.findByUsername(username);
        if (user == null) {
            return new LoginResponse(false, "User not found", null);
        } else if (!user.getPassword().equals(password)) {
            return new LoginResponse(false, "Wrong password", null);
        } else {
            return new LoginResponse(true, "Login successful", user);
        }
    }
}
