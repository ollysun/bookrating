package com.book.bookrating.domain.repositories;

import com.book.bookrating.domain.models.Book;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findBooksByUserId(Long userId);
    Book findBookById(Long id);
}
