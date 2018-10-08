package com.book.bookrating.domain.controller;

import com.book.bookrating.domain.exception.ResourceNotFoundException;
import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.repositories.UserRepository;
import com.book.bookrating.domain.services.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {
    public static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @RequestMapping(value="/", method = RequestMethod.GET)
    @ApiOperation(value = "List all users",notes = "List all users")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Users found"),
            @ApiResponse(code = 404,message = "Users not found")
    })
    public ResponseEntity<List<User>> getAllUsers(){
        List<User> users = userService.findAll();
        if (users.isEmpty()) {
            return new ResponseEntity(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<List<User>>(users, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Find User",notes = "Find the User by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "User found"),
            @ApiResponse(code = 404,message = "User not found"),
    })
    public ResponseEntity<?> getbyId(@PathVariable(value = "id") final Long id){
        logger.info("Fetching User with id {}", id);
        User user = userService.findById(id);
        if (user == null) {
            logger.error("User with id {} not found.", id);
            return new ResponseEntity<>(new ResourceNotFoundException("UserId  " + id + "not found"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<User>(user, HttpStatus.OK);
    }


    @RequestMapping(value="/signup", method = RequestMethod.POST)
    @ApiOperation(value = "Create New User",notes = "It permits to create a new user")
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "User created successfully"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public ResponseEntity<?> saveUser(@RequestBody User user, UriComponentsBuilder ucBuilder){
        logger.info("Creating User : {}", user);
        if (userService.isUserExist(user)) {
            logger.error("Unable to create. A User with name {} already exist", user.getUsername());
            return new ResponseEntity<>(
                    new ResourceNotFoundException("Unable to create. A User with name " + user.getUsername() + " already exist."), HttpStatus.CONFLICT);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/api/users/{id}").buildAndExpand(user.getId()).toUri());
        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }

    @RequestMapping(value="/{userId}", method = RequestMethod.PUT)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update User",notes = "It permits to update a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "User update successfully"),
            @ApiResponse(code = 404,message = "User not found"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public ResponseEntity<?> updateUser(@PathVariable Long userId,
                                   @RequestBody User user){

        logger.info("Updating User with id {}", userId);

        User currentUser = userService.findById(userId);

        if (currentUser == null) {
            logger.error("Unable to update. User with id {} not found.", userId);
            return new ResponseEntity<>(new ResourceNotFoundException("Unable to upate. User with id " + userId + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        currentUser.setEmail(user.getEmail());
        userRepository.save(currentUser);
        return new ResponseEntity<User>(currentUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Remove User by id",notes = "It permits to remove a user")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "User removed successfully"),
            @ApiResponse(code = 404,message = "User not found")
    })
    public ResponseEntity<?> delete(@PathVariable("id") final long id) {

        logger.info("Fetching & Deleting User with id {}", id);

        User user = userService.findById(id);
        if (user == null) {
            logger.error("Unable to delete. User with id {} not found.", id);
            return new ResponseEntity<>(new ResourceNotFoundException("Unable to delete. User with id " + id + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        userService.delete(id);
        return new ResponseEntity<>("User Deleted",HttpStatus.NO_CONTENT);
    }



}
