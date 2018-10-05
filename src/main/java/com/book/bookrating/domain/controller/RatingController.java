package com.book.bookrating.domain.controller;

import com.book.bookrating.domain.exception.ResourceNotFoundException;
import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.models.Rating;
import com.book.bookrating.domain.repositories.BookRepository;
import com.book.bookrating.domain.repositories.RatingRepository;
import com.book.bookrating.domain.repositories.UserRepository;
import com.book.bookrating.domain.resources.BookResource;
import com.book.bookrating.domain.resources.RatingResource;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/ratings")
@Api(tags = "Book", description = "Book API")
public class RatingController {

    @Autowired
    BookRepository bookRepository;

    @Autowired
    RatingRepository ratingRepository;

    @GetMapping("/books/{bookId}/ratings")
    public ResponseEntity<Resources<RatingResource>> getAllRatingsByBookId(@PathVariable (value = "bookId") Long bookId) {
        final List<RatingResource> collection = getRatingsForBook(bookId);
        final Resources<RatingResource> resources = new Resources<>(collection);
        final String uriString = ServletUriComponentsBuilder.fromCurrentRequest().build().toUriString();
        resources.add(new Link(uriString, "self"));
        return ResponseEntity.ok(resources);
    }

    private List<RatingResource> getRatingsForBook(final long bookId) {
        return bookRepository
                .findById(bookId)
                .map(
                        u ->
                                u.getRatings()
                                        .stream()
                                        .map(RatingResource::new)
                                        .collect(Collectors.toList()))
                .orElseThrow(() -> new ResourceNotFoundException("books " + bookId + "not found"));
    }

    @GetMapping("/books/{bookId}/ratings/{ratingId}")
    public ResponseEntity<RatingResource> getByBookIdAndRatingId(
            @PathVariable final long bookId, @PathVariable final long ratingId) {
        return bookRepository
                .findById(bookId)
                .map(
                        u ->
                                u.getRatings()
                                        .stream()
                                        .filter(m -> m.getId().equals(ratingId))
                                        .findAny()
                                        .map(m -> ResponseEntity.ok(new RatingResource(m)))
                                        .orElseThrow(() -> new ResourceNotFoundException("Ratings " + ratingId + "not found")))
                .orElseThrow(() -> new ResourceNotFoundException("books " + bookId + "not found"));
    }


    @PostMapping("/books/{bookId}/rating")
    public Rating createRating(
                             @PathVariable(value = "bookId") Long bookId,
                           @Valid @RequestBody Rating rating) {
        return bookRepository.findById(bookId).map(book -> {
            rating.setBook(book);
            return ratingRepository.save(rating);
        }).orElseThrow(() -> new ResourceNotFoundException("BookId " + bookId + " not found"));

    }
}
