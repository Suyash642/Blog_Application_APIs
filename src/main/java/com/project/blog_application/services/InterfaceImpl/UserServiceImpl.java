package com.project.blog_application.services.InterfaceImpl;

import com.project.blog_application.Entities.User;
import com.project.blog_application.Exceptions.InvalidPasswordException;
import com.project.blog_application.Exceptions.UserNotFoundException;
import com.project.blog_application.payloads.PasswordClass;
import com.project.blog_application.payloads.UserDTO;
import com.project.blog_application.repositories.UserRepo;
import com.project.blog_application.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private ModelMapper modelMapper;

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
        if(userRepo.existsById(userId)){
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
            log.info("updateUser() : user not found...");
            throw new UserNotFoundException("User with ID "+userId+" not exists...");
        }
    }

    @Override
    public void updateUserPwd(Integer userId, PasswordClass newPassword) {
        log.info("Start updateUserPwd() method...");
        User fetchedUser = userRepo.findById(userId).orElseThrow(() -> new UserNotFoundException("User with ID "+userId+" not exists..."));
        if ((newPassword.getNewPassword()!=null) && (newPassword.getNewPassword().length() >= 4 && newPassword.getNewPassword().length() <= 10)) {
            fetchedUser.setPassword(newPassword.getNewPassword());
            userRepo.save(fetchedUser);
            log.info("updateUserPwd() : password of user with ID " + userId + " is updated...");
        }else {
            log.info("updateUserPwd(): password is not valid, update password operation is failed....");
            throw new InvalidPasswordException("Password is not valid...");
        }
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
        log.info("getAllUsers(): converted User to UserDTO...");
        log.info("getAllUsers(): fetched userDTOs "+fetchedUserDTOs);
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
        return this.modelMapper.map(user, UserDTO.class);
    }

    public User convertUserDTOToUser(UserDTO userDto){
        log.info("Start convertUserDTOToUser() method...");
        return this.modelMapper.map(userDto, User.class);
    }
}
