package com.book.bookrating.domain.repositories;

import com.book.bookrating.domain.models.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepository extends JpaRepository<Book, Long> {
    //Page<Book> findByUserId(Long postId);
}
