package com.maria.ecommerce.controller;

import com.maria.ecommerce.dao.UserRepository;
import com.maria.ecommerce.dto.AuthRequest;
import com.maria.ecommerce.dto.AuthResponse;
import com.maria.ecommerce.dto.RegisterRequest;
import com.maria.ecommerce.entity.Customer;
import com.maria.ecommerce.entity.User;
import com.maria.ecommerce.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/register")
    public String register(@RequestBody RegisterRequest request) {
        if (userRepository.findByEmail(request.email).isPresent()) {
            return "Email già registrata";
        }

        User user = new User();
        user.setEmail(request.email);
        user.setPassword(passwordEncoder.encode(request.password));
        user.setFirstName(request.firstName);
        user.setLastName(request.lastName);
        userRepository.save(user);



        return "Utente registrato con successo";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email, request.password)
        );

        UserDetails user = userRepository.findByEmail(request.email)
                .map(u -> org.springframework.security.core.userdetails.User
                        .withUsername(u.getEmail())
                        .password(u.getPassword())
                        .authorities("USER")
                        .build())
                .orElseThrow();

        String token = jwtUtils.generateToken(user.getUsername());

        return new AuthResponse(token);
    }
}

