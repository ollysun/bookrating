package com.book.bookrating;

import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.models.Rating;
import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.PersistenceContext;
import java.util.List;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace.NONE;

@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@DataJpaTest
@AutoConfigureTestDatabase(replace = NONE)
public class UserRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired private JdbcTemplate jdbcTemplate;

    @Autowired
    private UserRepository userRepository;

    User user;

    @Before
    public void setUp() {
        //given
        user = new User("moses","moses@gmail.com","description");
    }

    @Test
    public void whenFindByUsername_thenReturnUser() {
        entityManager.persist(user);
        entityManager.flush();
        //when
        User testUser = userRepository.findByUsername(user.getUsername());
        //then
        assertThat(testUser.getUsername())
                .isEqualTo(user.getUsername());
    }

    @Test
    public void should_delete_all_user() {
        entityManager.persist(new User("Jack", "Smith@gmail.com","password"));
        entityManager.persist(new User("Adam", "Johnson@gmail.com", "password"));
        userRepository.deleteAll();
        assertThat(userRepository.findAll()).isEmpty();
    }

    @Test
    public void whenFindUserById() {
        entityManager.persist(user);
        //when
        User testUser = userRepository.findUserById(user.getId());
        assertThat(testUser).isEqualTo(user);
    }

    @Test
    public void testSaveBookRating() {
        Book book = new Book("facebook","mighty facebook","book description");
        user.addBook(book);
        user = userRepository.save(user);
        // rating added is not null
        assertThat(userRepository.findUserById(user.getId()).getBooks()).isNotNull();
        // check the one rating we added.
        assertThat(userRepository.findUserById(user.getId()).getBooks().size()).isEqualTo(1);
    }

}
