package com.hromenko.computerperipherals.controller;

import com.hromenko.computerperipherals.dto.JwtRequest;
import com.hromenko.computerperipherals.dto.JwtResponse;
import com.hromenko.computerperipherals.dto.RegisterRequest;
import com.hromenko.computerperipherals.dto.UserProfileResponse;
import com.hromenko.computerperipherals.exceptions.AppError;
import com.hromenko.computerperipherals.model.User;
import com.hromenko.computerperipherals.repository.UserRepository;
import com.hromenko.computerperipherals.service.JwtService;
import com.hromenko.computerperipherals.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@Slf4j
public class AuthController {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    @PostMapping("/auth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Wrong login/password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getEmail());
        String token = jwtService.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        if (userService.existsByUsername(request.getEmail())) {
            return new ResponseEntity<>(new AppError(HttpStatus.BAD_REQUEST.value(), "Користувач вже існує"), HttpStatus.BAD_REQUEST);
        }

        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setName(request.getName());
        user.setSurname(request.getSurname());
        user.setLocation(request.getLocation());
        user.setBirthDate(LocalDate.parse(request.getBirthDate()));
        user.setPhoneNumber(request.getPhoneNumber());

        userService.save(user);

        UserDetails userDetails = userService.loadUserByUsername(user.getEmail());
        String token = jwtService.generateToken(userDetails);

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/logoutacc")
    public ResponseEntity<?> logout(HttpServletRequest request) {
        try {
            return ResponseEntity.ok("Успішно");
        } catch (Exception e) {
            return new ResponseEntity<>(new AppError(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Error!"), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping ("/checkToken")
    public boolean checkToken() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication());
        return SecurityContextHolder.getContext().getAuthentication().isAuthenticated();
    }

    @GetMapping("/profile")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Principal principal){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow();

        UserProfileResponse dto = new UserProfileResponse();
        dto.setName(user.getName());
        dto.setSurname(user.getSurname());
        dto.setEmail(user.getEmail());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setLocation(user.getLocation());
        dto.setBirthDate(user.getBirthDate().toString());

        return ResponseEntity.ok(dto);
    }

}




