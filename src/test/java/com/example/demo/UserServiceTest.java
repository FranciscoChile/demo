package com.example.demo;


import com.example.demo.controllers.exceptions.DataValidationException;
import com.example.demo.controllers.exceptions.UserNotFoundException;
import com.example.demo.dtos.SuccessResponse;
import com.example.demo.entities.Phone;
import com.example.demo.entities.User;
import com.example.demo.repositories.PhoneRepository;
import com.example.demo.repositories.UserRepository;
import com.example.demo.services.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;


@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    
    final String token = "eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJmcmFuY2lzY28iLCJleHAiOjE2NTk2MDgwNzksImlhdCI6MTY1OTU5MDA3OX0.pmmmfcgJjnUe4bJkVfi4FaAKVebxQruZiU5P9UUJXkl09xKD1EibunQYBc_EDJ6ozmLfp4n-OnH7AvEYkNrzfA";

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PhoneRepository phoneRepository;

    
    @Test
    public void whenGetAllUser_thenOK() {
        List<User> users = new ArrayList<>();
        users.add(new User());

        given(userRepository.findAll()).willReturn(users);

        Iterable<User> expected = userService.findAll();

        assertEquals(expected, users);
        verify(userRepository).findAll();
    }

    @Test
    public void givenUserObject_whenCreateUser_thenReturnSavedUser()  {
        
        User user = createUser();
        
        SuccessResponse expected = new SuccessResponse();

        expected.setId(user.getId());
        expected.setCreated(user.getCreated());
        expected.setModified(user.getModified());
        expected.setLastLogin(user.getLastLogin());
        expected.setToken(user.getToken());
        expected.setActive(true);

        when(userRepository.save(ArgumentMatchers.any(User.class))).thenReturn(user);

        SuccessResponse created = userService.create(user, "Bearer " + token);

        assertEquals(created.getToken(), user.getToken());
        verify(userRepository).save(user);

    }

    @Test
    public void whenGivenName_shouldReturnUser_ifFound() {
        List<User> users = new ArrayList<>();        
        User user = createUser();
        users.add(user);

        when(userRepository.findByName(user.getName())).thenReturn(users);

        List<User> expected = userService.findByName(user.getName());

        assertEquals(expected, users);
        verify(userRepository).findByName(user.getName());
    }


    @Test
    public void whenGivenId_shouldReturnUser_ifFound() {
        User user = createUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));

        User expected = userService.findById(user.getId());

        assertEquals(expected, user);
        verify(userRepository).findById(user.getId());
    }
    

    @Test
    public void givenInvalidName_whenSearch_thenReturnEmpty() {
        given(userRepository.findByName("name")).willReturn(Collections.emptyList());
        
        List<User> expected = userService.findByName("name");

        assertEquals(0, expected.size());
    }

    @Test
    public void should_throw_exception_when_user_no_exist()  {
        User user = new User();
        user.setId(null);
        user.setName("Test Name");

        given(userRepository.findById(any())).willReturn(Optional.empty());

        assertThrows(UserNotFoundException.class, () -> userService.findById(user.getId()));
    }

    @Test
    public void should_throw_exception_when_email_malformed()  {
        User user = new User();        
        user.setEmail("email");

        assertThrows(DataValidationException.class, () -> userService.create(user, "token"));
    }

    @Test
    public void should_throw_exception_when_password_malformed()  {
        User user = new User();        
        user.setEmail("hola@mundo.com");
        user.setPassword("123");

        assertThrows(DataValidationException.class, () -> userService.create(user, "token"));
    }
    
    private User createUser() {

        Phone phone = Phone.builder()
            .number("111")
            .cityCode("111")
            .countryCode("countryCode")
            .build();

            Set<Phone> s = new HashSet<>();
            s.add(phone);

        return User.builder()
            .id(UUID.randomUUID())
            .name("name")
            .email("hola@mundo.com")
            .password("Aaaaa12")
            .token("Bearer " + token)
            .phones(s)
            .build();
    }
}