package com.book.bookrating.domain.controller;

import com.book.bookrating.domain.models.User;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.book.bookrating.domain.exception.ResourceNotFoundException;
import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.repositories.BookRepository;
import com.book.bookrating.domain.repositories.UserRepository;
import com.book.bookrating.domain.resources.BookResource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/books")
@Api(tags = "Book", description = "Book API")
public class BookController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    UserRepository userRepository;


    @GetMapping("/users/{userId}")
    @ApiOperation(value = "List all Books by userId",notes = "Find the Book by userId")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Book  found"),
            @ApiResponse(code = 404,message = "Book not found"),
    })
    public ResponseEntity<Resources<BookResource>> getAllBooksByUserId(@PathVariable (value = "userId") Long userId) {
        final List<BookResource> collection = getBooksForUser(userId);
        final Resources<BookResource> resources = new Resources<>(collection);
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resources.add(new Link(uriString, "self"));
        return ResponseEntity.ok(resources);
    }

    private List<BookResource> getBooksForUser(final long userId) {
        return userRepository
                .findById(userId)
                .map(
                        u ->
                            u.getBooks()
                                    .stream()
                                    .map(BookResource::new)
                                    .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("users " + userId + "not found"));
    }

    @GetMapping("/{id}")
    @ApiOperation(value = "Find Book",notes = "Find the Book by ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Book found"),
            @ApiResponse(code = 404,message = "Book not found"),
    })
    public ResponseEntity<BookResource> getByBookId(@PathVariable final long id) {
        return bookRepository
                .findById(id)
                .map(p -> ResponseEntity.ok(new BookResource(p)))
                .orElseThrow(() -> new ResourceNotFoundException("Books " + id + "not found"));
    }


    @GetMapping("/users/{userId}/books/{bookId}")
    @ApiOperation(value = "Find Book",notes = "Find the Book by userId and bookId")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Book found"),
            @ApiResponse(code = 404,message = "Book not found"),
    })
    public ResponseEntity<BookResource> getByUserIdAndBookId(
            @PathVariable final long userId, @PathVariable final long bookId) {
        return userRepository
                .findById(userId)
                .map(
                        u ->
                            u.getBooks()
                                    .stream()
                                    .filter(m -> m.getId().equals(bookId))
                                    .findAny()
                                    .map(m -> ResponseEntity.ok(new BookResource(m)))
                                    .orElseThrow(() -> new ResourceNotFoundException("Books " + bookId + "not found")))
                .orElseThrow(() -> new ResourceNotFoundException("users " + userId + "not found"));
    }


    @PostMapping("/users/{userId}/books")
    @ApiOperation(value = "Create Book",notes = "It permits to create a new book")
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "Book created successfully"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public Book createBook(@PathVariable (value = "userId") Long userId,
                                 @Valid @RequestBody Book book) {
        return userRepository.findById(userId).map(user -> {
            book.setUser(user);
            return bookRepository.save(book);
        }).orElseThrow(() -> new ResourceNotFoundException("UserId " + userId + " not found"));
    }



    @PutMapping("/users/{userId}/books/{bookId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Update book",notes = "It permits to update a book")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Book update successfully"),
            @ApiResponse(code = 404,message = "Book not found"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public ResponseEntity<BookResource> put(
            @PathVariable (value = "userId") Long userId,
            @PathVariable (value = "bookId") Long bookId,
            @RequestBody Book bookRequest) {
        return userRepository
                .findById(userId)
                .map(
                        u -> {
                            final Book book = updateBook(u, bookId, bookRequest);
                            final URI uri =
                                    URI.create(ServletUriComponentsBuilder.fromCurrentRequest().toUriString());
                            return ResponseEntity.created(uri).body(new BookResource(book));
                        })
                .orElseThrow(() -> new ResourceNotFoundException("Users " + userId + "Not found"));
    }

    private Book updateBook(
            final User user, final long id, final Book book) {
        return bookRepository.save(
                new Book(user, book.getName(), book.getTitle(), book.getDescription()));
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
        return userRepository
                .findById(userId)
                .map(
                        u ->
                            u.getBooks()
                                    .stream()
                                    .filter(m -> m.getId().equals(bookId))
                                    .findAny()
                                    .map(
                                            m -> {
                                                bookRepository.delete(m);
                                                return ResponseEntity.noContent().build();
                                            })
                                    .orElseThrow(() -> new ResourceNotFoundException("books " + bookId + "not found")))
                .orElseThrow(() -> new ResourceNotFoundException("Users " + userId + "not found "));
    }

}
