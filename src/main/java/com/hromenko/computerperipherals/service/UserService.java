package com.hromenko.computerperipherals.service;

import com.hromenko.computerperipherals.model.User;
import com.hromenko.computerperipherals.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;
    public Optional<User> findByEmail(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user=findByEmail(email).orElseThrow(()->new UsernameNotFoundException(
                String.format("User not found")
        ));


        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                List.of()
        );
    }

    public boolean existsByUsername(String username) {
        return userRepository.findByEmail(username).isPresent();
    }

    public void save(User user) {
        userRepository.save(user);
    }
}
