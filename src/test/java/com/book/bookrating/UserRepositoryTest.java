package com.book.bookrating;

import com.book.bookrating.domain.models.Book;
import com.book.bookrating.domain.models.User;
import com.book.bookrating.domain.repositories.UserRepository;
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

    @Test
    public void whenFindByUsername_thenReturnUser() {
        //given
        User user1 = new User();
        user1.setUsername("moses");
        user1.setPassword("password");
        user1.setEmail("ollysun@gmail.com");

        entityManager.persist(user1);
        entityManager.flush();

        //when
        User users = userRepository.findByUsername(user1.getUsername());

        //then
        assertThat(users.getUsername())
                .isEqualTo(user1.getUsername());
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
        //given
        User user1 = new User();
        user1.setUsername("moses");
        user1.setPassword("password");
        user1.setEmail("ollysun@gmail.com");
        entityManager.persist(user1);


        //when
        User testUser = userRepository.findUserById(user1.getId());

        assertThat(testUser).isEqualTo(user1);

    }

}
