package com.example.demo;

import com.example.demo.dtos.SuccessResponse;
import com.example.demo.entities.Phone;
import com.example.demo.entities.User;
import com.example.demo.entities.UserAdmin;
import com.example.demo.services.JwtService;
import com.example.demo.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = DemoApplication.class)
@AutoConfigureMockMvc
public class UserControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserService userService;

    @InjectMocks
    private JwtService jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    private static String jwtToken;

    Iterable<User> allUsers;
    User user;
    SuccessResponse response;

    Set<Phone> phones = new HashSet<>();

    @BeforeEach
    public void setUp() {
        ReflectionTestUtils.setField(jwtUtil, "secretKey", "3cfa76ef14937c1c0ea519f8fc057a80fcd04a7420f8e8bcd0a7567c272e007b");



        response = new SuccessResponse();
        response.setToken(jwtToken);

        Phone phone = Phone.builder().countryCode("56").cityCode("32").number("123").build();
        phones.add(phone);

        user = User.builder().name("Test name").email("user@email.com").password("password").phones(phones).build();
        allUsers = List.of(user);

    }

    @Test
    public void givenToken_whenGetSecureRequest_thenOK() throws Exception {

        mvc.perform(get("/api/users")
            .header("Authorization", "Bearer " + jwtToken)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON)
        )
        .andExpect(status().isOk() );
    }


    @Test
    public void listAllUsers_whenGetMethod()
            throws Exception {

        User user = User.builder().name("Test name").email("user@email.com").password("password").phones(phones).build();
        Iterable<User> allUsers = List.of(user);

        given(userService.findAll()).willReturn(allUsers);

        RequestBuilder request = MockMvcRequestBuilders
                .get("/api/users")
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions result = mvc.perform(request);

        result.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is( ((List)allUsers).size()  ) ));

    }

    @Test
    public void saveUserTest() throws Exception{
        User user = User.builder().name("Test name").email("user@email.com").password("password").phones(phones).build();

        given(userService.create(user, jwtToken)).willReturn(response);

        RequestBuilder request = MockMvcRequestBuilders
                .post("/api/users").content(objectMapper.writeValueAsString(user)).contentType(MediaType.APPLICATION_JSON_VALUE).accept(MediaType.APPLICATION_JSON).header("Authorization", "Bearer " + jwtToken)
                .with(SecurityMockMvcRequestPostProcessors.jwt());

        ResultActions response = mvc.perform(request);


        response.andDo(print()).
                andExpect(status().isOk());
    }
}
