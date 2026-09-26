package com.uniconnect.uniconnectbackend.service;

import com.uniconnect.uniconnectbackend.model.User;
import com.uniconnect.uniconnectbackend.repository.UserRepository;
import com.uniconnect.uniconnectbackend.dto.UserResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.uniconnect.uniconnectbackend.config.JwtUtil;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // REGISTER
    public UserResponse register(User user) {
        user.setPassword(encoder.encode(user.getPassword()));
        User saved = userRepository.save(user);

        return new UserResponse(
                saved.getId(),
                saved.getName(),
                saved.getEmail()
        );
    }

    // LOGIN
    public String login(String email, String password) {
        return userRepository.findByEmail(email)
                .filter(u -> encoder.matches(password, u.getPassword()))
                .map(u -> JwtUtil.generateToken(u.getEmail()))
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }
}