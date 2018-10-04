package com.book.bookrating.domain.resources;

import com.book.bookrating.domain.controller.BookController;
import com.book.bookrating.domain.controller.UserController;
import com.book.bookrating.domain.models.Book;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class BookResource extends ResourceSupport {

    private final Book book;

    public BookResource(final Book book) {
        this.book = book;
        final long bookId = book.getId();
        final long userId = book.getUser().getId();
        add(new Link(String.valueOf(bookId), "bookId"));
        add(linkTo(methodOn(BookController.class).getAllBooksByUserId(userId)).withRel("book"));
        add(linkTo(methodOn(UserController.class).getbyId(userId)).withRel("book"));
        add(linkTo(methodOn(BookController.class).getByUserIdAndBookId(userId, bookId)).withSelfRel());
    }
}
