package com.nisum.challenge.security;

import com.nisum.challenge.model.AppUser;
import com.nisum.challenge.model.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<AppUser> appUser = appUserRepository.findByEmail(username);
        if(appUser.isPresent()){
            return toUserDetails(appUser.get());
        }
        return null;
    }

    private UserDetails toUserDetails(AppUser userObject) {
        return User.withUsername(userObject.getEmail())
                .password(userObject.getPassword())
                .authorities(new ArrayList<>()).build();
    }
}
