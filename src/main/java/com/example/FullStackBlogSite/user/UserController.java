package com.example.FullStackBlogSite.user;

import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;

@RestController
@RequestMapping("/api/user")
public class UserController {

    private final JdbcClient jdbcClient;
    private final PasswordEncoder passwordEncoder;

    public UserController(JdbcClient jdbcClient, PasswordEncoder passwordEncoder) {
        this.jdbcClient = jdbcClient;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request, HttpSession session) {
        String hashedPassword = passwordEncoder.encode(request.getPassword());

        try {
            // Save to DB
            jdbcClient.sql("INSERT INTO users (username, password, enabled) VALUES (?, ?, true)")
                    .params(Arrays.asList(request.getUsername(), hashedPassword))
                    .update();

            return ResponseEntity.ok("User registered");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Registration failed: " + e.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request, HttpSession session) {
        try {
            String encodedPassword = jdbcClient.sql("SELECT password FROM users WHERE username = ?")
                    .params(Collections.singletonList(request.getUsername()))
                    .query(String.class)
                    .single();

            if (passwordEncoder.matches(request.getPassword(), encodedPassword)) {
                User securityUser = new User(
                        request.getUsername(),
                        encodedPassword,
                        AuthorityUtils.createAuthorityList("ROLE_USER")
                );

                UsernamePasswordAuthenticationToken auth =
                        new UsernamePasswordAuthenticationToken(securityUser, null, securityUser.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(auth);
                session.setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

                return ResponseEntity.ok("Login successful.");
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Login failed.");
        }
    }
}
