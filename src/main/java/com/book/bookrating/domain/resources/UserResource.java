package com.book.bookrating.domain.resources;

import com.book.bookrating.domain.controller.UserController;
import com.book.bookrating.domain.models.User;
import org.springframework.hateoas.ResourceSupport;
import lombok.Getter;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import static org.springframework.hateoas.jaxrs.JaxRsLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Getter
public class UserResource extends ResourceSupport {
    private final User user;

    public UserResource(final User user) {
        this.user = user;
        final long id = user.getId();
        add(linkTo(UserController.class).withRel("user"));
        add(ControllerLinkBuilder.linkTo(methodOn(UserController.class).getbyId(id)).withRel("users"));
        add(ControllerLinkBuilder.linkTo(methodOn(UserController.class).getbyId(id)).withSelfRel());
    }
}
