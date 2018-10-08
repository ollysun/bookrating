package com.book.bookrating.domain.repositories;

import com.book.bookrating.domain.models.Rating;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    List<Rating> findAllRatingsByBookId(Long Id);

    Rating findRatingById(Long Id);
}
