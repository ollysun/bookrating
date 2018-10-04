package com.book.bookrating.domain.services;

import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.models.UserDto;

import java.util.List;

public interface IUserService {
    User save(UserDto user);

    List<User> findAll();

    void delete(Long id);

    User findOne(String username);

    User findById(Long id);

}
