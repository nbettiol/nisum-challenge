package com.nisum.challenge.service;

import com.nisum.challenge.model.AppUser;
import com.nisum.challenge.model.AppUserRepository;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class AppUserService {

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public AppUser createUser(AppUser user) {
        validateUserData(user, false);
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setCreated(Calendar.getInstance().getTime());
        AppUser newUser = appUserRepository.save(user);
        return newUser;
    }

    public AppUser getUser(UUID id) {
        Optional<AppUser> user = appUserRepository.findById(id);
        if(!user.isPresent()){
            throw new NoSuchElementException("The user does not exists");
        };
        return user.get();
    }

    public List<AppUser> getUserList() {
        List<AppUser> users = new ArrayList<>();
        appUserRepository.findAll().forEach(users::add);
        return users;
    }

    public AppUser updateUser(AppUser user, UUID id) {
        Optional<AppUser> optional = appUserRepository.findById(id);
        if(!optional.isPresent()){
            throw new NoSuchElementException("The user does not exists");
        };
        validateUserData(user, true);
        AppUser userToUpdate = optional.get();
        userToUpdate.setName(user.getName());
        userToUpdate.getPhones().clear();
        userToUpdate.getPhones().addAll(user.getPhones());
        userToUpdate.setModified(Calendar.getInstance().getTime());
        appUserRepository.save(userToUpdate);
        return userToUpdate;
    }

    private void validateUserData(AppUser user, boolean isUpdate) {
        if (!isUpdate && appUserRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("The email already exists");
        }
        if (!EmailValidator.getInstance().isValid(user.getEmail())) {
            throw new IllegalArgumentException("Invalid email");
        }
    }
}
