package com.book.bookrating;

import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.repositories.BookRepository;
import com.book.bookrating.domain.repositories.UserRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class bookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;


    @Autowired
    private BookRepository bookRepository;

    @Test
    public void whenFindByUsername_thenReturnBook() {
        //given
        Book book = new Book();
        book.setName("moses");
        book.setDescription("good book");
        book.setTitle("ancient book");
        entityManager.persist(book);
        entityManager.flush();
        //when
        Book book1 = bookRepository.findBookById(book.getId());

        //then
        assertThat(book1.getName())
                .isEqualTo(book.getName());
    }

    @Test
    public void should_delete_all_book() {
        entityManager.persist(new Book("Jack", "Smith@gmail.com","password"));
        entityManager.persist(new Book("Adam", "Johnson@gmail.com", "password"));

        bookRepository.deleteAll();

        assertThat(bookRepository.findAll()).isEmpty();
    }

    @Test
    public void whenFindBookById() {
        //given
        Book book = new Book();
        book.setName("moses");
        book.setDescription("good book");
        book.setTitle("ancient book");
        entityManager.persist(book);


        //when
        Book testBook = bookRepository.findBookById(book.getId());

        assertThat(testBook).isEqualTo(book);

    }
}
