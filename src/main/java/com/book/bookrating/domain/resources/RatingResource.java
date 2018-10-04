package com.book.bookrating.domain.resources;

import com.book.bookrating.domain.controller.BookController;
import com.book.bookrating.domain.controller.RatingController;
import com.book.bookrating.domain.controller.UserController;
import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.models.Rating;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class RatingResource extends ResourceSupport {

    private final Rating rating;

    public RatingResource(final Rating rating) {
        this.rating = rating;
        final long ratingId = rating.getId();
        final long bookId = rating.getBook().getId();
        add(new Link(String.valueOf(ratingId), "ratingId"));
        add(linkTo(methodOn(RatingController.class).getAllRatingsByBookId(bookId)).withRel("rating"));
        add(linkTo(methodOn(BookController.class).getByBookId(bookId)).withRel("rating"));
        add(linkTo(methodOn(RatingController.class).getByBookIdAndRatingId(bookId,ratingId)).withSelfRel());
    }
}
