package com.dumblthon.messenger.auth.repository.impl;

import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.repository.UserSecretRepository;
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

import static com.dumblthon.messenger.auth.userinfo.Tables.USER_SECRET;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.param;

@Repository
public class UserSecretRepositoryImpl implements UserSecretRepository {

    private final NamedParameterJdbcOperations namedJdbcOperations;

    private final DSLContext create = DSL.using(SQLDialect.POSTGRES);

    private final RowMapper<UserSecret> mapper =
            (rs, rowNum) -> new UserSecret(
                    rs.getLong(USER_SECRET.USER_ID.getName()),
                    rs.getString(USER_SECRET.DEVICE_ID.getName()),
                    rs.getString(USER_SECRET.SECRET.getName())
            );

    @Autowired
    public UserSecretRepositoryImpl(NamedParameterJdbcOperations namedJdbcOperations) {
        this.namedJdbcOperations = namedJdbcOperations;
    }

    @Override
    public Optional<UserSecret> findByUserIdAndDeviceId(long userId, String deviceId) {
        Param<Long> paramUserId = param("userId", Long.class);
        Param<String> paramDeviceId = param("deviceId", String.class);

        String sql = create.select().from(USER_SECRET)
                .where(USER_SECRET.USER_ID.eq(paramUserId)
                        .and(USER_SECRET.DEVICE_ID.eq(paramDeviceId)))
                .getSQL();

        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue(paramUserId.getName(), userId)
                .addValue(paramDeviceId.getName(), deviceId);

        return Optional.ofNullable(namedJdbcOperations
                .queryForObject(sql, paramSource, mapper));
    }

    @Override
    public UserSecret save(UserSecret secret) {
        Param<Long> paramUserId = param("userId", Long.class);
        Param<String> paramDeviceId = param("deviceId", String.class);
        Param<String> paramSecret = param("secret", String.class);

        String sql = create.insertInto(USER_SECRET)
                .set(USER_SECRET.USER_ID, paramUserId)
                .set(USER_SECRET.DEVICE_ID, paramDeviceId)
                .set(USER_SECRET.SECRET, paramSecret)
                .onConflictOnConstraint(USER_SECRET.getPrimaryKey())
                .doUpdate()
                .set(USER_SECRET.SECRET, paramSecret)
                .returningResult(asterisk())
                .getSQL();

        SqlParameterSource paramSource = new MapSqlParameterSource()
                .addValue(paramUserId.getName(), secret.getUserId())
                .addValue(paramDeviceId.getName(), secret.getDeviceId())
                .addValue(paramSecret.getName(), secret.getSecret());

        return namedJdbcOperations.queryForObject(sql, paramSource, mapper);
    }
}
