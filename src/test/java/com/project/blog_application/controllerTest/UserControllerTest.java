package com.project.blog_application.controllerTest;

import com.project.blog_application.Entities.User;
import com.project.blog_application.controllers.UserController;
import com.project.blog_application.payloads.PasswordClass;
import com.project.blog_application.payloads.UserDTO;
import com.project.blog_application.services.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
public class UserControllerTest {
    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Test
    public void testFetchUserById(){
        Integer userId = 1;
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "testUser@gmail.com", "I am mock test user");

        Mockito.when(userService.getUserById(userId)).thenReturn(mockUserDTO);
        ResponseEntity<UserDTO> response = userController.getUserById(userId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals("Test User", response.getBody().getName());
        Assertions.assertEquals("testUser@gmail.com", response.getBody().getEmail());
        Assertions.assertEquals("I am mock test user", response.getBody().getAbout());
    }

    @Test
    public void testFetchUsersIfNotEmpty(){
        List<UserDTO> mockUserDTOs = getUserDTOS();
        Mockito.when(userService.getAllUsers()).thenReturn(mockUserDTOs);
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        int i = 0;
        for(UserDTO mockUserDTO : mockUserDTOs){
            Assertions.assertEquals(mockUserDTO.getId(), response.getBody().get(i).getId());
            Assertions.assertEquals(mockUserDTO.getName(), response.getBody().get(i).getName());
            Assertions.assertEquals(mockUserDTO.getEmail(), response.getBody().get(i).getEmail());
            Assertions.assertEquals(mockUserDTO.getAbout(), response.getBody().get(i).getAbout());
            i++;
        }
    }

    @Test
    public void testFetchUsersIfEmpty(){
        List<UserDTO> emptyMockUserDTOList = List.of();

        Mockito.when(userService.getAllUsers()).thenReturn(emptyMockUserDTOList);
        ResponseEntity<List<UserDTO>> response = userController.getAllUsers();

        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testCreateUser(){
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "testUser@gmail.com", "I am mock test user");
        User mockUser = new User(1, "Test User", "testUser@gmail.com", "1234abcd", "I am mock test user");
        Mockito.when(userService.createUser(mockUser)).thenReturn(mockUserDTO);
        ResponseEntity<UserDTO> response = userController.createUser(mockUser);

        Assertions.assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals("Test User", response.getBody().getName());
        Assertions.assertEquals("testUser@gmail.com", response.getBody().getEmail());
        Assertions.assertEquals("I am mock test user", response.getBody().getAbout());
    }

    @Test
    public void testUpdateUser(){
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "testUser@gmail.com", "I am mock test user");
        Integer userId = 1;

        Mockito.when(userService.updateUser(mockUserDTO, userId)).thenReturn(mockUserDTO);
        ResponseEntity<UserDTO> response = userController.updateUser(mockUserDTO, userId);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals("Test User", response.getBody().getName());
        Assertions.assertEquals("testUser@gmail.com", response.getBody().getEmail());
        Assertions.assertEquals("I am mock test user", response.getBody().getAbout());
    }

    @Test
    public void testUpdateUserPwd(){
        PasswordClass newPwd = new PasswordClass("Pass09877");
        Integer userId = 1;
        ResponseEntity<?> response = userController.updateUserPwd(newPwd, userId);
        Assertions.assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    public void testDeleteUser(){
        Integer userId = 1;
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "testUser@gmail.com", "I am mock test user");

        Mockito.when(userService.deleteUserById(userId)).thenReturn(mockUserDTO);
        ResponseEntity<UserDTO> response = userController.deleteUser(userId);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        Assertions.assertEquals(1, response.getBody().getId());
        Assertions.assertEquals("Test User", response.getBody().getName());
        Assertions.assertEquals("testUser@gmail.com", response.getBody().getEmail());
        Assertions.assertEquals("I am mock test user", response.getBody().getAbout());
    }

    private static List<UserDTO> getUserDTOS() {
        UserDTO mockUserDTO1 = new UserDTO(1, "Test User 1", "testUser1@gmail.com", "I am mock test user1");
        UserDTO mockUserDTO2 = new UserDTO(2, "Test User 2", "testUser2@gmail.com", "I am mock test user2");
        UserDTO mockUserDTO3 = new UserDTO(3, "Test User 3", "testUser3@gmail.com", "I am mock test user3");
        UserDTO mockUserDTO4 = new UserDTO(4, "Test User 4", "testUser4@gmail.com", "I am mock test user4");
        return List.of(mockUserDTO1, mockUserDTO2, mockUserDTO3, mockUserDTO4);
    }
}
