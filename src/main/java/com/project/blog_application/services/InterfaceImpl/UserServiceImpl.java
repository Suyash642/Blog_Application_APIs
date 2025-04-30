package com.project.blog_application.services.InterfaceImpl;

import com.project.blog_application.Entities.User;
import com.project.blog_application.Exceptions.UserNotFoundException;
import com.project.blog_application.payloads.UserDTO;
import com.project.blog_application.repositories.UserRepo;
import com.project.blog_application.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDTO createUser(User user) {
        log.info("Start createUser() method...");
        User savedUser = userRepo.save(user);
        log.info("createUser() : saved user "+ this.convertUserToUserDTO(savedUser));
        return this.convertUserToUserDTO(savedUser);
    }

    @Override
    public UserDTO updateUser(UserDTO userDto, Integer userId) {
        log.info("Start updateUser() method...");
        if(userRepo.existsById(userDto.getId())){
            log.info("updateUser() : user exists in DB...");
            User fetchedUser = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("no user found with id "+userId));
            fetchedUser.setName(userDto.getName());
            fetchedUser.setEmail(userDto.getEmail());
            fetchedUser.setAbout(userDto.getAbout());
            User modifiedUser = userRepo.save(fetchedUser);
            log.info("updateUser() : user with Id "+modifiedUser.getId()+" is updated...");
            log.info("updateUser() : updated user "+this.convertUserToUserDTO(modifiedUser));
            return this.convertUserToUserDTO(fetchedUser);
        }else{
            log.error("updateUser() : user not found...");
            throw new UserNotFoundException("User with ID "+userId+" not exists...");
        }
    }

    @Override
    public void updateUserPwd(Integer userId, String newPassword) {
        log.info("Start updateUserPwd() method...");
        User fetchedUser = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID "+userId+" not exists..."));
        fetchedUser.setPassword(newPassword);
        log.info("updateUserPwd() : password of user with ID "+userId+" is updated...");
    }

    @Override
    public UserDTO getUserById(Integer userId) {
        log.info("Start getUserById() method...");
        return this.convertUserToUserDTO(userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID "+userId+" not exists...")));
    }

    @Override
    public List<UserDTO> getAllUsers() {
        log.info("Start getAllUsers() method...");
        List<User> fetchedUsers = userRepo.findAll();
        List<UserDTO> fetchedUserDTOs = new ArrayList<>();
        for(User user : fetchedUsers){
            fetchedUserDTOs.add(this.convertUserToUserDTO(user));
        }
        log.info("getAllUsers() : fetched users list "+fetchedUserDTOs);
        return fetchedUserDTOs;
    }

    @Override
    public UserDTO deleteUserById(Integer userId) {
        log.info("Start deleteUserById() method...");
        User userGoingToDelete = userRepo.findById(userId).orElseThrow(()-> new UserNotFoundException("User with ID "+userId+" not exists..."));
        UserDTO userDTO = this.convertUserToUserDTO(userGoingToDelete);
        log.info("deleteUserById() : deleted user "+userDTO);
        userRepo.delete(userGoingToDelete);
        return userDTO;
    }

    public UserDTO convertUserToUserDTO(User user){
        log.info("Start convertUserToUserDTO() method...");
        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setEmail(user.getEmail());
        userDTO.setAbout(user.getAbout());
        return userDTO;
    }

    public User convertUserDTOToUser(UserDTO userDto){
        log.info("Start convertUserDTOToUser() method...");
        User user = new User();
        user.setId(userDto.getId());
        user.setName(userDto.getName());
        user.setEmail(userDto.getEmail());
        user.setAbout(userDto.getAbout());
        return user;
    }
}
