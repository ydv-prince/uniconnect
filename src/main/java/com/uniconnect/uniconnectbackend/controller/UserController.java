package com.uniconnect.uniconnectbackend.controller;

import com.uniconnect.uniconnectbackend.dto.*;
import com.uniconnect.uniconnectbackend.model.User;
import com.uniconnect.uniconnectbackend.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ApiResponse<UserResponse> register(@RequestBody RegisterRequest request) {
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());

        UserResponse response = userService.register(user);

        return new ApiResponse<>(true, "User registered successfully", response);
    }

    @PostMapping("/login")
    public ApiResponse<String> login(@RequestBody LoginRequest request) {
        String token = userService.login(
                request.getEmail(),
                request.getPassword()
        );

        return new ApiResponse<>(true, "Login successful", token);
    }
    
    @GetMapping("/profile")
    public ApiResponse<String> profile() {
        return new ApiResponse<>(true, "Access granted", "This is protected data");
    }
}