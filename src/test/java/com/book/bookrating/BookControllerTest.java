package com.book.bookrating;

import com.book.bookrating.domain.controller.UserController;
import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import javax.ws.rs.core.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookratingApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {

    @LocalServerPort
    private int port;

    private MockMvc mockMvc;

    //@Autowired
    private ObjectMapper mapper;

    @MockBean
    private UserController userController;


    @Test
    public void createNewUser() throws Exception {
        Book book = new Book();
        book.setTitle("mike");
        book.setDescription("book meant for developer");
        book.setName("babadbook");

        mockMvc.perform(post("/api/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk());
    }

}
