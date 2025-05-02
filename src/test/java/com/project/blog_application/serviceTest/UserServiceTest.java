package com.project.blog_application.serviceTest;

import com.project.blog_application.Entities.User;
import com.project.blog_application.Exceptions.UserNotFoundException;
import com.project.blog_application.payloads.PasswordClass;
import com.project.blog_application.payloads.UserDTO;
import com.project.blog_application.repositories.UserRepo;
import com.project.blog_application.services.InterfaceImpl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import javax.swing.text.html.Option;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    UserRepo userRepo;

    @Mock
    ModelMapper modelMapper;

    @InjectMocks
    UserServiceImpl userService;

    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateUser(){
        User mockUser = new User(1, "Test User", "test_user@gmail.com", "password", "I am mocked user");
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "test_user@gmail.com", "I am mocked user");

        Mockito.when(userRepo.save(mockUser)).thenReturn(mockUser);
        Mockito.when(userService.convertUserToUserDTO(mockUser)).thenReturn(mockUserDTO);

        UserDTO savedUserDTO = userService.createUser(mockUser);

        Assertions.assertNotNull(savedUserDTO);
        Assertions.assertEquals(1, savedUserDTO.getId());
        Assertions.assertEquals("Test User", savedUserDTO.getName());
        Assertions.assertEquals("test_user@gmail.com", savedUserDTO.getEmail());
        Assertions.assertEquals("I am mocked user", savedUserDTO.getAbout());

        Mockito.verify(userRepo).save(mockUser);
    }

    @Test
    public void testUpdateUser_WhenUserExist(){
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "testUser@gmail.com", "I am mock test user");
        User mockUser = new User(1, "Test User", "testUser@gmail.com", "1234abcd","I am mock test user");
        Integer userId = 1;

        Mockito.when(userRepo.existsById(userId)).thenReturn(Boolean.TRUE);
        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(userRepo.save(mockUser)).thenReturn(mockUser);
        Mockito.when(userService.convertUserToUserDTO(mockUser)).thenReturn(mockUserDTO);

        UserDTO updatedUserDTO = userService.updateUser(mockUserDTO, userId);

        Assertions.assertNotNull(updatedUserDTO);
        Assertions.assertEquals(1, updatedUserDTO.getId());
        Assertions.assertEquals("Test User", updatedUserDTO.getName());
        Assertions.assertEquals("testUser@gmail.com", updatedUserDTO.getEmail());
        Assertions.assertEquals("I am mock test user", updatedUserDTO.getAbout());

        Mockito.verify(userRepo).existsById(userId);
        Mockito.verify(userRepo).findById(userId);
        Mockito.verify(userRepo).save(mockUser);

    }

    @Test
    public void testUpdateUser_WhenUserNotExist(){
        Integer userId = 1;
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "testUser@gmail.com", "I am mock test user");

        Mockito.when(userRepo.existsById(userId)).thenReturn(Boolean.FALSE);
        UserNotFoundException thrownException = Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUser(mockUserDTO, userId));

        Assertions.assertEquals("User with ID 1 not exists...", thrownException.getMessage());

        Mockito.verify(userRepo).existsById(userId);
    }

    @Test
    public void testUpdateUserPassword_WhenUserExist(){
        Integer userId = 1;
        PasswordClass password = new PasswordClass("abcd1234");
        User mockUser = new User(1, "Test User", "testUser@gmail.com", "1234abcd","I am mock test user");

        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(mockUser));

        userService.updateUserPwd(userId, password);

        Assertions.assertEquals(password.getNewPassword(), mockUser.getPassword());

        Mockito.verify(userRepo).findById(userId);
    }

    @Test
    public void testUpdateUserPassword_WhenUserNotExist(){
        Integer userId = 1;
        PasswordClass password = new PasswordClass("abcd1234");

        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.empty());

        UserNotFoundException thrownException = Assertions.assertThrows(UserNotFoundException.class, () -> userService.updateUserPwd(userId, password));
        Assertions.assertEquals("User with ID 1 not exists...", thrownException.getMessage());

        Mockito.verify(userRepo).findById(userId);
    }

    @Test
    public void testGetUserById_WhenUserExist(){
        Integer userId = 1;
        User mockUser = new User(1, "Test User", "testUser@gmail.com", "1234abcd","I am mock test user");
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "testUser@gmail.com", "I am mock test user");

        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(userService.convertUserToUserDTO(mockUser)).thenReturn(mockUserDTO);

        UserDTO response = userService.getUserById(userId);

        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals("Test User", response.getName());
        Assertions.assertEquals("testUser@gmail.com", response.getEmail());
        Assertions.assertEquals("I am mock test user", response.getAbout());

        Mockito.verify(userRepo).findById(userId);
    }

    @Test
    public void testGetUserById_WhenUserNotExist(){
        Integer userId = 2;

        UserNotFoundException thrownException = Assertions.assertThrows(UserNotFoundException.class, () -> userService.getUserById(userId));

        Assertions.assertEquals("User with ID 2 not exists...", thrownException.getMessage());
    }

    @Test
    public void testGetAllUsers(){
        User mockUser1 = new User(1, "Test User 1", "testUser1@gmail.com", "1234abcd", "I am mock test user1");
        User mockUser2 = new User(2, "Test User 2", "testUser2@gmail.com", "0987xyzw","I am mock test user2");
        User mockUser3 = new User(3, "Test User 3", "testUser3@gmail.com", "abcd1234","I am mock test user3");
        User mockUser4 = new User(4, "Test User 4", "testUser4@gmail.com", "567788998","I am mock test user4");

        List<User> mockUsers = Arrays.asList(mockUser1, mockUser2, mockUser3, mockUser4);

        UserDTO mockUserDTO1 = new UserDTO(1, "Test User 1", "testUser1@gmail.com",  "I am mock test user1");
        UserDTO mockUserDTO2 = new UserDTO(2, "Test User 2", "testUser2@gmail.com", "I am mock test user2");
        UserDTO mockUserDTO3 = new UserDTO(3, "Test User 3", "testUser3@gmail.com", "I am mock test user3");
        UserDTO mockUserDTO4 = new UserDTO(4, "Test User 4", "testUser4@gmail.com", "I am mock test user4");

        List<UserDTO> mockUserDTOs = Arrays.asList(mockUserDTO1, mockUserDTO2, mockUserDTO3, mockUserDTO4);

        Mockito.when(userRepo.findAll()).thenReturn(mockUsers);
        Mockito.when(userService.convertUserToUserDTO(mockUser1)).thenReturn(mockUserDTO1);
        Mockito.when(userService.convertUserToUserDTO(mockUser2)).thenReturn(mockUserDTO2);
        Mockito.when(userService.convertUserToUserDTO(mockUser3)).thenReturn(mockUserDTO3);
        Mockito.when(userService.convertUserToUserDTO(mockUser4)).thenReturn(mockUserDTO4);

        List<UserDTO> response = userService.getAllUsers();

        Assertions.assertNotNull(response);
        Assertions.assertFalse(response.isEmpty());
        Assertions.assertEquals(mockUserDTOs.size(), response.size());
        Assertions.assertEquals(mockUserDTOs.get(0).getName(), response.get(0).getName());

        Mockito.verify(userRepo).findAll();
    }

    @Test
    public void testDeleteUserById_WhenUserExist(){
        Integer userId = 1;
        User mockUser = new User(1, "Test User", "testUser@gmail.com", "1234abcd","I am mock test user");
        UserDTO mockUserDTO = new UserDTO(1, "Test User", "testUser@gmail.com", "I am mock test user");

        Mockito.when(userRepo.findById(userId)).thenReturn(Optional.of(mockUser));
        Mockito.when(userService.convertUserToUserDTO(mockUser)).thenReturn(mockUserDTO);

        UserDTO response = userService.deleteUserById(userId);

        Assertions.assertNotNull(response);
        Assertions.assertEquals(1, response.getId());
        Assertions.assertEquals("Test User", response.getName());
        Assertions.assertEquals("testUser@gmail.com", response.getEmail());
        Assertions.assertEquals("I am mock test user", response.getAbout());

        Mockito.verify(userRepo).findById(userId);
    }

    @Test
    public void testDeleteUserById_WhenUserNotExist(){
        Integer userId = 1;

        UserNotFoundException thrownException = Assertions.assertThrows(UserNotFoundException.class, () -> userService.deleteUserById(userId));
        Assertions.assertEquals("User with ID 1 not exists...", thrownException.getMessage());
    }
}
