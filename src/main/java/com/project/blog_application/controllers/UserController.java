package com.project.blog_application.controllers;

import com.project.blog_application.Entities.User;
import com.project.blog_application.payloads.PasswordClass;
import com.project.blog_application.payloads.UserDTO;
import com.project.blog_application.services.UserService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/fetchUser/{Id}")
    public ResponseEntity<UserDTO> getUserById(@PathVariable("Id") Integer Id){
        log.info("UserController : getUserById()");
        UserDTO userDTO = userService.getUserById(Id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @GetMapping("/fetchAll")
    public ResponseEntity<List<UserDTO>> getAllUsers(){
        log.info("UserController : getAllUsers()");
        List<UserDTO> fetchUsers = userService.getAllUsers();
        if (!fetchUsers.isEmpty()) {
            return new ResponseEntity<>(fetchUsers, HttpStatus.OK);
        }else{
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/createUser")
    public ResponseEntity<UserDTO> createUser(@Valid @RequestBody User user){
        log.info("UserController : createUser()");
        UserDTO userDto = userService.createUser(user);
        return new ResponseEntity<>(userDto, HttpStatus.CREATED);
    }

    @PutMapping("/updateUser/{Id}")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDto, @PathVariable("Id") Integer Id){
        log.info("UserController : updateUser()");
        UserDTO userDTO = userService.updateUser(userDto, Id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/updateUserPwd/{Id}")
    public ResponseEntity<?> updateUserPwd(@RequestBody PasswordClass password, @PathVariable("Id") Integer Id){
        log.info("UserController : updateUserPwd()");
        userService.updateUserPwd(Id, password);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/deleteUser/{Id}")
    public ResponseEntity<UserDTO> deleteUser(@PathVariable("Id") Integer Id){
        log.info("UserController : deleteUser()");
        UserDTO userDTO = userService.deleteUserById(Id);
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }
}
