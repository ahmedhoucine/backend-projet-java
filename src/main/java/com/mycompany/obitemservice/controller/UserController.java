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
    public List<UserModel> getAllUsers() {return userRepository.findAll();}

    @GetMapping("/users/{id}")
    public UserModel getUser(@PathVariable String id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot Find Item By ID: " + id));
    }

    @PostMapping("/users")
    public ResponseEntity<String> saveUser(@RequestBody UserModel user) {
        UserModel saveduser = userRepository.insert(user);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(saveduser.getId())
                .toUri();

        return ResponseEntity.created(uri).build();
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        userRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PostMapping("/users/login")
    public ResponseEntity<LoginResponse> loginUser(@RequestBody UserModel user) {
        // Retrieve the user from the database based on the provided user ID
        Optional<UserModel> userOptional = userRepository.findById(user.getId());

        // Extract UserModel from Optional
        UserModel existingUser = userOptional.orElse(null);

        // Check if the user exists and the provided password matches
        if (existingUser != null) {
            if (existingUser.getPassword().equals(user.getPassword())) {
                // User authenticated successfully
                return ResponseEntity.ok().body(new LoginResponse(true, "Login success"));
            } else {
                // Incorrect password
                return ResponseEntity.ok().body(new LoginResponse(false, "Wrong password"));
            }
        } else {
            // User not found
            return ResponseEntity.ok().body(new LoginResponse(false, "User not found"));
        }
    }
}