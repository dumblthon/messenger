package com.dumblthon.messenger.auth.repository;

import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.repository.impl.UserRepositoryImpl;
import liquibase.exception.LiquibaseException;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.dumblthon.messenger.auth.userinfo.tables.User.USER;

@RunWith(JUnit4.class)
public class UserRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(UserRepositoryTest.class);

    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>();

    private static NamedParameterJdbcTemplate jdbcTemplate;
    private static UserRepositoryImpl userRepositoryImpl;

    @BeforeClass
    public static void init() throws LiquibaseException, SQLException {
        jdbcTemplate = DbIntegrationTest.initialize(postgresContainer);
        userRepositoryImpl = new UserRepositoryImpl(jdbcTemplate);
    }

    @Before
    public void truncate() {
        DbIntegrationTest.truncate(jdbcTemplate.getJdbcTemplate(),
                USER.getSchema().getName(), USER.getName());
    }

    @Test
    public void testSaveNew() {
        User newUser = new User("79145667132");
        User savedUser = userRepositoryImpl.save(newUser);

        log.info("Saved {}", savedUser);
        Assert.assertEquals(newUser.getPhoneNumber(), savedUser.getPhoneNumber());
    }

    @Test
    public void testSaveExistingId()  {
        User newUser = new User("79145667132");
        User savedUser = userRepositoryImpl.save(newUser);

        User existUser = new User(savedUser.getId(), "70000000");
        User updatedUser = userRepositoryImpl.save(existUser);

        log.info("Updated {} to {}", savedUser, updatedUser);
        User expected = new User(savedUser.getId(), existUser.getPhoneNumber());
        Assert.assertEquals(expected, updatedUser);
    }

    @Test(expected = DataAccessException.class)
    public void testSaveExistingPhoneNumber()  {
        User newUser = new User("79145667132");
        userRepositoryImpl.save(newUser);
        userRepositoryImpl.save(newUser);
    }

    @Test
    public void testFindByIdExisting() {
        List<User> users = randomUsers(5);
        List<User> savedUsers = users.stream()
                .map(userRepositoryImpl::save)
                .collect(Collectors.toList());

        Optional<User> found = userRepositoryImpl
                .findById(savedUsers.get(4).getId());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(savedUsers.get(4), found.get());
    }

    @Test
    public void testFindByIdAbsent() {
        Optional<User> found = userRepositoryImpl
                .findById(2L);
        Assert.assertFalse(found.isPresent());
    }

    @Test
    public void testFindByPhoneNumberExisting() {
        User newUser = new User("79145667132");
        User savedUser = userRepositoryImpl.save(newUser);

        Optional<User> found = userRepositoryImpl
                .findByPhoneNumber(newUser.getPhoneNumber());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(savedUser, found.get());
    }

    @Test
    public void testFindByPhoneNumberAbsent() {
        Optional<User> found = userRepositoryImpl
                .findByPhoneNumber("79145667132");
        Assert.assertFalse(found.isPresent());
    }



    private List<User> randomUsers(int count) {
        return Stream
                .generate(() -> randomNumber(10))
                .limit(count)
                .map(User::new)
                .collect(Collectors.toList());
    }

    private String randomNumber(int length) {
        Random r = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < length; i++)
            sb.append(r.nextInt(9));
        return sb.toString();
    }

}
