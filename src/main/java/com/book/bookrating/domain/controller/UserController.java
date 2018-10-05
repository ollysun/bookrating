package com.book.bookrating.domain.controller;

import com.book.bookrating.domain.exception.ResourceNotFoundException;
import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.models.UserDto;
import com.book.bookrating.domain.repositories.UserRepository;
import com.book.bookrating.domain.resources.UserResource;
import com.book.bookrating.domain.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @RequestMapping(value="", method = RequestMethod.GET)
    @ApiOperation(value = "List all users",notes = "List all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Users found"),
            @ApiResponse(code = 404,message = "Users not found")
    })
    public ResponseEntity<Resources<UserResource>>  allUser(){
        final List<UserResource> collection =
                userRepository.findAll().stream().map(UserResource::new).collect(Collectors.toList());
        final Resources<UserResource> resources = new Resources<>(collection);
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resources.add(new Link(uriString, "self"));
        return ResponseEntity.ok(resources);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Find User",notes = "Find the User by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "User found"),
            @ApiResponse(code = 404,message = "User not found"),
    })
    public ResponseEntity<UserResource> getbyId(@PathVariable(value = "id") final Long id){
        return userRepository
                .findById(id)
                .map(p -> ResponseEntity.ok(new UserResource(p)))
                .orElseThrow(() -> new ResourceNotFoundException("BookId " + id + "not found"));
    }


    @RequestMapping(value="/signup", method = RequestMethod.POST)
    @ApiOperation(value = "Create New User",notes = "It permits to create a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "User created successfully"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public User saveUser(@RequestBody UserDto user){
        return userService.save(user);
    }

    @RequestMapping(value="/{userId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update User",notes = "It permits to update a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "User update successfully"),
            @ApiResponse(code = 404,message = "User not found"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public User saveUser(@PathVariable Long userId,
                         @RequestBody UserDto user){
        return userRepository.findById(userId)
                .map(question -> userService.save(user)).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Remove User by id",notes = "It permits to remove a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "User removed successfully"),
            @ApiResponse(code = 404,message = "User not found")
    })
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
