package com.dumblthon.messenger.auth.repository.impl;

import com.dumblthon.messenger.auth.model.User;
import com.dumblthon.messenger.auth.repository.UserRepository;
import org.jooq.DSLContext;
import org.jooq.Param;
import org.jooq.SQLDialect;
import org.jooq.impl.DSL;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import static com.dumblthon.messenger.auth.userinfo.Tables.USER;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.param;

@Repository
public class UserRepositoryImpl implements UserRepository {

    private final NamedParameterJdbcOperations namedJdbcOperations;

    private final DSLContext create = DSL.using(SQLDialect.POSTGRES);

    private final RowMapper<User> mapper =
            (rs, rowNum) -> new User(
                    rs.getLong(USER.ID.getName()),
                    rs.getString(USER.PHONE_NUMBER.getName())
            );

    @Autowired
    public UserRepositoryImpl(NamedParameterJdbcOperations namedJdbcOperations) {
        this.namedJdbcOperations = namedJdbcOperations;
    }

    @Override
    public Optional<User> findById(Long userId) {
        Param<Long> paramUserId = param("userId", Long.class);

        String sql = create.select().from(USER)
                .where(USER.ID.eq(paramUserId))
                .getSQL();

        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue(paramUserId.getName(), userId);

        return Optional.ofNullable(namedJdbcOperations
                .queryForObject(sql, paramSource, mapper));
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        Param<String> paramPhoneNumber = param("phoneNumber", String.class);

        String sql = create.select().from(USER)
                .where(USER.PHONE_NUMBER.eq(paramPhoneNumber))
                .getSQL();

        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue(paramPhoneNumber.getName(), phoneNumber);

        return Optional.ofNullable(namedJdbcOperations
                .queryForObject(sql, paramSource, mapper));
    }

    @Override
    public User save(User user) {
        Param<Long> paramUserId = param("userId", Long.class);
        Param<String> paramPhoneNumber = param("phoneNumber", String.class);

        String sql = create.insertInto(USER)
                .set(USER.ID, paramUserId)
                .set(USER.PHONE_NUMBER, paramPhoneNumber)
                .onConflictOnConstraint(USER.getPrimaryKey())
                .doUpdate()
                .set(USER.PHONE_NUMBER, paramPhoneNumber)
                .returningResult(asterisk())
                .getSQL();

        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue(paramUserId.getName(), user.getId())
                .addValue(paramPhoneNumber.getName(), user.getPhoneNumber());

        return namedJdbcOperations.queryForObject(sql, paramSource, mapper);
    }
}
