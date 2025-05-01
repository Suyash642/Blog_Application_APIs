package com.project.blog_application.services;

import com.project.blog_application.Entities.User;
import com.project.blog_application.payloads.PasswordClass;
import com.project.blog_application.payloads.UserDTO;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService {
    UserDTO createUser(User user);
    UserDTO updateUser(UserDTO userDto, Integer userId);
    void updateUserPwd(Integer userId, PasswordClass password);
    UserDTO getUserById(Integer userId);
    List<UserDTO> getAllUsers();
    UserDTO deleteUserById(Integer userId);
}
