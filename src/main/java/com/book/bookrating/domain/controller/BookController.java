package com.book.bookrating.domain.controller;

import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.services.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.book.bookrating.domain.exception.ResourceNotFoundException;
import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.repositories.BookRepository;
import com.book.bookrating.domain.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/books")
@Api(tags = "Book", description = "Book API")
public class BookController {

    public static final Logger logger = LoggerFactory.getLogger(BookController.class);


    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserService userService;


    @GetMapping("/users/{userId}")
    @ApiOperation(value = "List all Books by userId",notes = "Find the Book by userId")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Book  found"),
            @ApiResponse(code = 404,message = "Book not found"),
    })
    public ResponseEntity<List<Book>> getAllBooksByUserId(@PathVariable (value = "userId") Long userId) {
        List<Book> books = bookRepository.findBooksByUserId(userId);
        if (books.isEmpty()) {
            return new ResponseEntity("UserID " + userId + "cannot be found" ,HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Book>>(books, HttpStatus.OK);
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Find Book",notes = "Find the Book by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Book found"),
            @ApiResponse(code = 404,message = "Book not found"),
    })
    public ResponseEntity<?> getByBookId(@PathVariable final long id) {
        Book books = bookRepository.findBookById(id);
        if (books == null) {
            logger.error("Book with id {} not found.", id);
            return new ResponseEntity<>(new ResourceNotFoundException("BookId  " + id + "not found"),
                    HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Book>(books, HttpStatus.OK);

    }


    @PostMapping("/users/{userId}/books")
    @ApiOperation(value = "Create Book",notes = "It permits to create a new book")
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "Book created successfully"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public ResponseEntity<?> createBook(@PathVariable (value = "userId") Long userId,
                                 @Valid @RequestBody Book book) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            logger.error("Unable to update. User with id {} not found.", userId);
            return new ResponseEntity<>(new ResourceNotFoundException("Unable to find User with id " + userId + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        book.setUser(user);
        return new ResponseEntity<>(bookRepository.save(book), HttpStatus.CREATED);
    }



    @PutMapping("/users/{userId}/books/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update book",notes = "It permits to update a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Book update successfully"),
            @ApiResponse(code = 404,message = "Book not found"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public ResponseEntity<?> put(
            @PathVariable (value = "userId") Long userId,
            @PathVariable (value = "bookId") Long bookId,
            @RequestBody Book bookRequest) {

        User user = userRepository.findUserById(userId);
        if (user == null) {
            logger.error("Unable to update. User with id {} not found.", userId);
            return new ResponseEntity<>(new ResourceNotFoundException("Unable to find User with id " + userId + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        Book book = bookRepository.findBookById(bookId);
        if (user == null) {
            logger.error("Unable to update. Book with id {} not found.", userId);
            return new ResponseEntity<>(new ResourceNotFoundException("Unable to find Book with id " + userId + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        book.setTitle(bookRequest.getTitle());
        book.setDescription(bookRequest.getTitle());
        book.setName(bookRequest.getName());
        bookRepository.save(book);
        return new ResponseEntity<>(book, HttpStatus.OK);
    }


    @DeleteMapping("/users/userId}/books/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Remove book by UserId",notes = "It permits to remove a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Book removed successfully"),
            @ApiResponse(code = 404,message = "Book not found")
    })
    public ResponseEntity<?> deleteBook(@PathVariable (value = "userId") Long userId,
                                           @PathVariable (value = "bookId") Long bookId) {
        User user = userRepository.findUserById(userId);
        if (user == null) {
            logger.error("Unable to locate User with id {} not found.", userId);
            return new ResponseEntity<>(new ResourceNotFoundException("Unable to locate User with id "
                    + userId + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            logger.error("Unable to locate Book with id {} not found.", userId);
            return new ResponseEntity<>(new ResourceNotFoundException("Unable to locate Book with id " + bookId + " not found."),
                    HttpStatus.NOT_FOUND);
        }

        bookRepository.delete(book);
        return new ResponseEntity<User>(HttpStatus.NO_CONTENT);
    }

}
