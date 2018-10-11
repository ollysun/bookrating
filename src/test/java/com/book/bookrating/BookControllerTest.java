package com.book.bookrating;

import com.book.bookrating.domain.controller.BookController;
import com.book.bookrating.domain.controller.UserController;
import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.models.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import javax.ws.rs.core.MediaType;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = BookratingApplication.class,
        webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class BookControllerTest {

    @Autowired
    private WebApplicationContext context;

    @LocalServerPort
    private int port;

    private MockMvc mockMvc;

    //@Autowired
    private ObjectMapper mapper;

    @MockBean
    private BookController bookController;

    private Book book;

    @Before
    public void setupMockMvc() {
        mockMvc = webAppContextSetup(context).build();
        book = new Book("my book","good book","get it for your growth");
        book.setId(1L);
    }

    @Test
    public void listAllBooksByUserId() throws Exception {
        Book book1 = new Book("java","essential java","Daily life Java");
        List<Book> addnew = Arrays.asList(book,book1);
        given(bookController.getAllBooksByUserId(1L)).willReturn((ResponseEntity.ok(addnew)));
        mockMvc.perform(get("/api/books/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.*", hasSize(addnew.size())));
    }


    @Test
    public void createNewBook() throws Exception {
        mockMvc.perform(post("/api/books/users/1/books")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(book)))
                .andExpect(status().isOk());
    }

    @Test
    public void getUserByIdNotFound() throws Exception {
        given(bookController.getByBookId(1L)).willReturn(ResponseEntity.ok(book));
        mockMvc.perform(get("/api/books/" + 1L)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }



}
