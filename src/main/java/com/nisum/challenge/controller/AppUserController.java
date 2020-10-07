package com.nisum.challenge.controller;

import com.nisum.challenge.model.AppUser;
import com.nisum.challenge.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class AppUserController {

    private final String USER_PATH = "/users";
    private final String USER_PATH_ID = USER_PATH + "/{id}";

    @Autowired
    private AppUserService appUserService;

    @PostMapping(path = USER_PATH, consumes = "application/json", produces = "application/json")
    public ResponseEntity addUser(@RequestBody AppUser user) throws Exception {
        AppUser newUser = appUserService.createUser(user);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping(path = USER_PATH_ID, produces = "application/json")
    public ResponseEntity<AppUser> getUser(@PathVariable UUID id) throws Exception {
        AppUser user = appUserService.getUser(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping(path = USER_PATH, produces = "application/json")
    public ResponseEntity<List<AppUser>> listUsers() throws Exception {
        List<AppUser> users = appUserService.getUserList();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping(path = USER_PATH_ID, consumes = "application/json", produces = "application/json")
    public ResponseEntity patchReserve(@PathVariable UUID id, @RequestBody AppUser user) throws Exception {
        AppUser updatedUser = appUserService.updateUser(user, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }
}

