package com.dumblthon.messenger.auth.repository;

import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.repository.impl.UserRepositoryImpl;
import com.dumblthon.messenger.auth.repository.impl.UserSecretRepositoryImpl;
import liquibase.exception.LiquibaseException;
import org.jooq.DSLContext;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.testcontainers.containers.PostgreSQLContainer;

import java.sql.SQLException;
import java.util.*;

import static com.dumblthon.messenger.auth.userinfo.tables.UserSecret.USER_SECRET;

@RunWith(JUnit4.class)
public class UserSecretRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(UserSecretRepositoryTest.class);

    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>();

    private static NamedParameterJdbcTemplate jdbcTemplate;
    private static UserSecretRepositoryImpl secretRepository;
    private static final DSLContext create = DSL.using(SQLDialect.POSTGRES);

    @BeforeClass
    public static void init() throws LiquibaseException, SQLException {
        jdbcTemplate = DbIntegrationTest.initialize(postgresContainer);
        secretRepository = new UserSecretRepositoryImpl(jdbcTemplate);

        UserRepositoryImpl userRepository = new UserRepositoryImpl(jdbcTemplate);
        userRepository.save(new User(1L, "5687974593"));
        userRepository.save(new User(2L, "9083157832"));
    }

    @Before
    public void truncate() {
        DbIntegrationTest.truncate(jdbcTemplate.getJdbcTemplate(),
                USER_SECRET.getSchema().getName(), USER_SECRET.getName());
    }

    @Test
    public void testSaveNew() {
        UserSecret newRecord = new UserSecret(1L, "1", "dasghwjghaw_gh6as4a4");
        UserSecret savedRecord = secretRepository.save(newRecord);

        log.info("Saved {}", savedRecord);
        Assert.assertEquals(newRecord, savedRecord);
    }

    @Test
    public void testSaveNewDevice()  {
        UserSecret recordOne = new UserSecret(1L, "1", "dasghwjghaw_gh6as4a4");
        secretRepository.save(recordOne);

        UserSecret recordTwo = new UserSecret(1L, "2", "da364ashaaw_g*3464a4");
        secretRepository.save(recordTwo);

        Assert.assertEquals(2L, getCount());
    }

    @Test
    public void testSaveExisting()  {
        UserSecret newRecord = new UserSecret(1L, "1", "dasghwjghaw_gh6as4a4");
        UserSecret savedRecord = secretRepository.save(newRecord);

        UserSecret existRecord = new UserSecret(savedRecord.getUserId(), savedRecord.getDeviceId(),
                "da364ashaaw_g*3464a4");
        UserSecret updatedRecord = secretRepository.save(existRecord);

        log.info("Updated {} to {}", savedRecord, updatedRecord);
        Assert.assertEquals(existRecord, updatedRecord);
        Assert.assertEquals(1L, getCount());
    }

    @Test
    public void testFindByPrimaryKeyExisting() {
        List<UserSecret> records = Arrays.asList(
                new UserSecret(1L, "1", "264gea46736y2q44ta34"),
                new UserSecret(1L, "2", "34tg34wtg537ua327qa7"),
                new UserSecret(2L, "1", "hsw784wh7894qat37w35"),
                new UserSecret(2L, "2", "794ezsery582w435gsy5")
        );
        records.forEach(secretRepository::save);

        UserSecret expected = records.get(2);
        Optional<UserSecret> found = secretRepository.findByUserIdAndDeviceId(
                expected.getUserId(), expected.getDeviceId());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(expected, found.get());
    }

    @Test
    public void testFindByPrimaryKeyAbsent() {
        Optional<UserSecret> found = secretRepository
                .findByUserIdAndDeviceId(1L, "3");
        Assert.assertFalse(found.isPresent());
    }



    private long getCount() {
        String sql = create.selectCount().from(USER_SECRET).getSQL();
        return Objects.requireNonNull(secretRepository.getNamedParamJdbcOperations()
                .queryForObject(sql, Collections.emptyMap(), Long.class));
    }

}
