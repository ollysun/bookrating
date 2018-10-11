package com.book.bookrating;

import com.book.bookrating.domain.controller.UserController;
import com.book.bookrating.domain.repositories.UserRepository;
import com.book.bookrating.domain.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.book.bookrating.domain.models.User;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import javax.ws.rs.core.MediaType;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertNotNull;

import static org.hamcrest.CoreMatchers.equalTo;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookratingApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class UserControllerITtest {

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    private MockMvc mockMvc;

    //@Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserController userController;

    @Autowired
    private BCryptPasswordEncoder bcryptEncoder;

    @Autowired
    UserService userService;

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");

    private static String BASE_PATH = "/api/users";
    private static final long ID = 25;
    private User user;

    @Before
    public void setupMockMvc() {
        mockMvc = webAppContextSetup(context).build();
        user = new User("moses","ollysun@gmail.com",bcryptEncoder.encode("password"));
        user.setId(1L);
    }

    @Test
    public void getUserByIdNotFound() throws Exception {
        given(userController.getbyId(1L)).willReturn(ResponseEntity.ok(user));
        mockMvc.perform(get(BASE_PATH + "/getbyId/" + 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void createNewUser() throws Exception {
        mockMvc.perform(post(BASE_PATH + "/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isOk());
    }

    @Test
    public void listAllUsers() throws Exception {
        User user1 = new User();
        user1.setUsername("moses");
        user1.setPassword(bcryptEncoder.encode("password"));
        user1.setEmail("ollysun@gmail.com");

        List<User> addnew = Arrays.asList(user,user1);
        given(userController.getAllUsers()).willReturn((ResponseEntity.ok(addnew)));
        mockMvc.perform(get(BASE_PATH + "/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(addnew.size())));
    }


}
