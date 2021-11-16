package com.example.demo.controller;

import com.example.demo.entity.app_user.AppUserRequest;
import com.example.demo.entity.app_user.AppUserResponse;
import com.example.demo.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
public class AppUserController {

    @Autowired
    AppUserService appUserService;

    @PostMapping
    public ResponseEntity<AppUserResponse> createAppUser(@Valid @RequestBody AppUserRequest request) {
        AppUserResponse response = appUserService.createUser(request);

        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(response.getId()).toUri();
        return ResponseEntity.created(location).body(response);
    }

    @GetMapping("/id")
    public ResponseEntity<AppUserResponse> getUser(@PathVariable("id") String id) {
        AppUserResponse response = appUserService.getUserResponseById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<AppUserResponse>> getUsers() {
        List<AppUserResponse> responses = appUserService.getUserResponses();
        return ResponseEntity.ok(responses);
    }
}
