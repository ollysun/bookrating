package com.book.bookrating.domain.controller;

import com.book.bookrating.domain.exception.ResourceNotFoundException;
import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.models.Rating;
import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.repositories.BookRepository;
import com.book.bookrating.domain.repositories.RatingRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@Api(tags = "Ratings", description = "Ratings API")
public class RatingController {

    public static final Logger logger = LoggerFactory.getLogger(RatingController.class);


    @Autowired
    BookRepository bookRepository;

    @Autowired
    RatingRepository ratingRepository;

    @GetMapping("/books/{bookId}/ratings")
    @ApiOperation(value = "List all Ratings",notes = "Find the Rating by bookId")
    @ApiResponses(value = {
            @ApiResponse(code = 200,message = "Ratings  found"),
            @ApiResponse(code = 404,message = "Ratings not found"),
    })
    public ResponseEntity<List<Rating>> getAllRatingsByBookId(@PathVariable (value = "bookId") Long bookId) {
        List<Rating> ratingsl = ratingRepository.findRatingsByBookId(bookId);
        if (ratingsl.isEmpty()) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<List<Rating>>(ratingsl, HttpStatus.OK);
    }


    @PostMapping("/books/{bookId}/rating")
    @ApiOperation(value = "Create Rating",notes = "It permits to create a new rating")
    @ApiResponses(value = {
            @ApiResponse(code = 201,message = "Ratings created successfully"),
            @ApiResponse(code = 400,message = "Invalid request")
    })
    public ResponseEntity<?> createRating(
                             @PathVariable(value = "bookId") Long bookId,
                           @RequestBody Rating rating) {

        Book book = bookRepository.findBookById(bookId);
        if (book == null) {
            logger.error("Unable to create rating with book id {} not found.", bookId);
            return new ResponseEntity<>(new ResourceNotFoundException("Unable to find Book with id "
                    + bookId + " not found."),
                    HttpStatus.NOT_FOUND);
        }
        rating.setBook(book);
        return new ResponseEntity<>(ratingRepository.save(rating), HttpStatus.CREATED);
    }
}
