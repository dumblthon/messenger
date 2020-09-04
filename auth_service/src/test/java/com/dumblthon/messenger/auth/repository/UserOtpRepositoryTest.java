package com.dumblthon.messenger.auth.repository;

import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.model.UserOtp;
import com.dumblthon.messenger.auth.repository.impl.UserOtpRepositoryImpl;
import com.dumblthon.messenger.auth.repository.impl.UserRepositoryImpl;
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

import static com.dumblthon.messenger.auth.userinfo.tables.UserOtp.USER_OTP;

@RunWith(JUnit4.class)
public class UserOtpRepositoryTest {

    private static final Logger log = LoggerFactory.getLogger(UserOtpRepositoryTest.class);

    @ClassRule
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>();

    private static NamedParameterJdbcTemplate jdbcTemplate;
    private static UserOtpRepositoryImpl otpRepository;
    private static final DSLContext create = DSL.using(SQLDialect.POSTGRES);

    @BeforeClass
    public static void init() throws LiquibaseException, SQLException {
        jdbcTemplate = DbIntegrationTest.initialize(postgresContainer);
        otpRepository = new UserOtpRepositoryImpl(jdbcTemplate);

        UserRepositoryImpl userRepository = new UserRepositoryImpl(jdbcTemplate);
        userRepository.save(new User(1L, "5687974593"));
        userRepository.save(new User(2L, "9083157832"));
    }

    @Before
    public void truncate() {
        DbIntegrationTest.truncate(jdbcTemplate.getJdbcTemplate(),
                USER_OTP.getSchema().getName(), USER_OTP.getName());
    }

    @Test
    public void testSaveNew() {
        UserOtp newRecord = new UserOtp(1L, "1", "567890", new Date());
        UserOtp savedRecord = otpRepository.save(newRecord);

        log.info("Saved {}", savedRecord);
        Assert.assertEquals(newRecord, savedRecord);
    }

    @Test
    public void testSaveNewDevice()  {
        UserOtp recordOne = new UserOtp(1L, "1", "567890", new Date());
        otpRepository.save(recordOne);

        UserOtp recordTwo = new UserOtp(1L, "2", "235406", new Date());
        otpRepository.save(recordTwo);

        Assert.assertEquals(2L, getCount());
    }

    @Test
    public void testSaveExisting()  {
        UserOtp newRecord = new UserOtp(1L, "1", "567890", new Date());
        UserOtp savedRecord = otpRepository.save(newRecord);

        UserOtp existRecord = new UserOtp(savedRecord.getUserId(), savedRecord.getDeviceId(),
                "1569524", new Date());
        UserOtp updatedRecord = otpRepository.save(existRecord);

        log.info("Updated {} to {}", savedRecord, updatedRecord);
        Assert.assertEquals(existRecord, updatedRecord);
        Assert.assertEquals(1L, getCount());
    }

    @Test
    public void testFindByPrimaryKeyExisting() {
        List<UserOtp> records = Arrays.asList(
                new UserOtp(1L, "1", "567890", new Date()),
                new UserOtp(1L, "2", "574337", new Date()),
                new UserOtp(2L, "1", "895635", new Date()),
                new UserOtp(2L, "2", "163484", new Date())
        );
        records.forEach(otpRepository::save);

        UserOtp expected = records.get(1);
        Optional<UserOtp> found = otpRepository.findByUserIdAndDeviceId(
                expected.getUserId(), expected.getDeviceId());
        Assert.assertTrue(found.isPresent());
        Assert.assertEquals(expected, found.get());
    }

    @Test
    public void testFindByPrimaryKeyAbsent() {
        List<UserOtp> records = Arrays.asList(
                new UserOtp(1L, "1", "567890", new Date()),
                new UserOtp(1L, "2", "574337", new Date()),
                new UserOtp(2L, "1", "895635", new Date()),
                new UserOtp(2L, "2", "163484", new Date())
        );
        records.forEach(otpRepository::save);

        Optional<UserOtp> found = otpRepository
                .findByUserIdAndDeviceId(1L, "3");
        Assert.assertFalse(found.isPresent());
    }



    private long getCount() {
        String sql = create.selectCount().from(USER_OTP).getSQL();
        return Objects.requireNonNull(otpRepository.getNamedParamJdbcOperations()
                .queryForObject(sql, Collections.emptyMap(), Long.class));
    }

}
