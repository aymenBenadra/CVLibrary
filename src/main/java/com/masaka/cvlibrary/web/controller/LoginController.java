package com.masaka.cvlibrary.web.controller;

import com.masaka.cvlibrary.model.User;
import com.masaka.cvlibrary.repository.UserRepository;
import com.masaka.cvlibrary.util.JwtUtils;
import com.masaka.cvlibrary.web.dto.LoginError;
import com.masaka.cvlibrary.web.dto.LoginResponse;
import com.masaka.cvlibrary.web.dto.UserCredentials;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(
        origins = "*",
        allowedHeaders = "*",
        methods = {RequestMethod.POST, RequestMethod.GET}
)
public class LoginController {

    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public LoginController(AuthenticationManager authenticationManager, JwtUtils jwtUtils,
                           UserDetailsService userDetailsService, UserRepository userRepository) {
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @PostMapping("/auth/login")
    public ResponseEntity<?> login(@RequestBody UserCredentials userCredentials) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                userCredentials.email(), userCredentials.password());
        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            if (authentication.isAuthenticated()) {
                User user = userRepository.findByEmail(userCredentials.email())
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                String accessToken = jwtUtils.generateToken(userDetailsService.loadUserByUsername(userCredentials.email()));
                return ResponseEntity.ok(new LoginResponse(accessToken, user.getName(), user.getEmail(), user.getRole().name()));
            }
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginError("Invalid email or password", HttpStatus.UNAUTHORIZED));
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginError("Invalid email or password", HttpStatus.UNAUTHORIZED));
    }

    @GetMapping("/auth/logged")
    public ResponseEntity<String> logged(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
            String username = jwtUtils.getUsernameFromToken(token);
            return ResponseEntity.ok().body("Logged in with : " + username + " using token: " + token);
        }
        return ResponseEntity.ok("Not logged");
    }
}
