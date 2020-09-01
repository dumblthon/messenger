package com.dumblthon.messenger.auth.repository.impl;

import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.repository.NamedParamJdbcOps;
import com.dumblthon.messenger.auth.repository.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.Query;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.dumblthon.messenger.auth.userinfo.Tables.USER;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.param;

@Repository
public class UserRepositoryImpl implements UserRepository, NamedParamJdbcOps<User> {

    private final NamedParameterJdbcOperations namedJdbcOperations;

    private final DSLContext create = DSL.using(SQLDialect.POSTGRES);

    private final RowMapper<User> mapper = (rs, rowNum) -> new User(
            rs.getLong(USER.ID.getName()),
            rs.getString(USER.PHONE_NUMBER.getName())
    );

    @Autowired
    public UserRepositoryImpl(NamedParameterJdbcOperations namedJdbcOperations) {
        this.namedJdbcOperations = namedJdbcOperations;
    }

    @Override
    public NamedParameterJdbcOperations getNamedParamJdbcOperations() {
        return namedJdbcOperations;
    }

    @Override
    public RowMapper<User> getMapper() {
        return mapper;
    }

    @Override
    public Optional<User> findById(Long userId) {
        Param<Long> paramUserId = param("userId", userId);

        Query query = create.select().from(USER)
                .where(USER.ID.eq(paramUserId));

        return queryForOptionalObject(query);
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        Param<String> paramPhoneNumber = param("phoneNumber", phoneNumber);

        Query query = create.select().from(USER)
                .where(USER.PHONE_NUMBER.eq(paramPhoneNumber));

        return queryForOptionalObject(query);
    }

    @Override
    public User save(User user) {
        return user.getId() == null ? saveNew(user) : saveExisting(user);
    }

    private User saveNew(User user) {
        Param<String> paramPhoneNumber =
                param(USER.PHONE_NUMBER.getName(), user.getPhoneNumber());

        Query query = create.insertInto(USER)
                .set(USER.PHONE_NUMBER, paramPhoneNumber)
                .returningResult(asterisk());

        return queryForObject(query);
    }

    private User saveExisting(User user) {
        Param<Long> paramId = param(USER.ID.getName(), user.getId());
        Param<String> paramPhoneNumber =
                param(USER.PHONE_NUMBER.getName(), user.getPhoneNumber());

        Query query = create.insertInto(USER)
                .set(USER.ID, paramId)
                .set(USER.PHONE_NUMBER, paramPhoneNumber)
                .onConflictOnConstraint(USER.getPrimaryKey())
                .doUpdate()
                .set(USER.PHONE_NUMBER, paramPhoneNumber)
                .returningResult(asterisk());

        return queryForObject(query);
    }

}
