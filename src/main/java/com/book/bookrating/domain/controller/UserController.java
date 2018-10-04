package com.book.bookrating.domain.controller;

import com.book.bookrating.domain.exception.ResourceNotFoundException;
import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.models.UserDto;
import com.book.bookrating.domain.repositories.UserRepository;
import com.book.bookrating.domain.resources.UserResource;
import com.book.bookrating.domain.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="/users", method = RequestMethod.GET)
    public ResponseEntity<Resources<UserResource>>  allUser(){
        final List<UserResource> collection =
                userRepository.findAll().stream().map(UserResource::new).collect(Collectors.toList());
        final Resources<UserResource> resources = new Resources<>(collection);
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resources.add(new Link(uriString, "self"));
        return ResponseEntity.ok(resources);
    }


    @GetMapping("/users/{id}")
    public ResponseEntity<UserResource> getbyId(@PathVariable(value = "id") final Long id){
        return userRepository
                .findById(id)
                .map(p -> ResponseEntity.ok(new UserResource(p)))
                .orElseThrow(() -> new ResourceNotFoundException("BookId " + id + "not found"));
    }


    @RequestMapping(value="/users/signup", method = RequestMethod.POST)
    public User saveUser(@RequestBody UserDto user){
        return userService.save(user);
    }

    @RequestMapping(value="/users/{userId}", method = RequestMethod.PUT)
    public User saveUser(@PathVariable Long userId,
                         @RequestBody UserDto user){
        return userRepository.findById(userId)
                .map(question -> {
                    return userService.save(user);
                }).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable("id") final long id) {
        return userRepository
                .findById(id)
                .map(
                        p -> {
                            userRepository.deleteById(id);
                            return ResponseEntity.noContent().build();
                        })
                .orElseThrow(() -> new ResourceNotFoundException("book id " + id + "not found"));
    }



}
