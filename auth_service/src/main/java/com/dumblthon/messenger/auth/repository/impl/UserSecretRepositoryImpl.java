package com.dumblthon.messenger.auth.repository.impl;

import com.dumblthon.messenger.auth.model.UserSecret;
import com.dumblthon.messenger.auth.repository.NamedParamJdbcOps;
import com.dumblthon.messenger.auth.repository.UserSecretRepository;
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

import static com.dumblthon.messenger.auth.userinfo.Tables.USER_SECRET;
import static org.jooq.impl.DSL.asterisk;
import static org.jooq.impl.DSL.param;

@Repository
public class UserSecretRepositoryImpl implements UserSecretRepository, NamedParamJdbcOps<UserSecret> {

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
    public NamedParameterJdbcOperations getNamedParamJdbcOperations() {
        return namedJdbcOperations;
    }

    @Override
    public RowMapper<UserSecret> getMapper() {
        return mapper;
    }

    @Override
    public Optional<UserSecret> findByUserIdAndDeviceId(long userId, String deviceId) {
        Param<Long> paramUserId = param("userId", userId);
        Param<String> paramDeviceId = param("deviceId", deviceId);

        Query query = create.select().from(USER_SECRET)
                .where(USER_SECRET.USER_ID.eq(paramUserId)
                        .and(USER_SECRET.DEVICE_ID.eq(paramDeviceId)));

        return queryForOptionalObject(query);
    }

    @Override
    public UserSecret save(UserSecret secret) {
        Param<Long> paramUserId = param("userId", secret.getUserId());
        Param<String> paramDeviceId = param("deviceId", secret.getDeviceId());
        Param<String> paramSecret = param("secret", secret.getSecret());

        Query query = create.insertInto(USER_SECRET)
                .set(USER_SECRET.USER_ID, paramUserId)
                .set(USER_SECRET.DEVICE_ID, paramDeviceId)
                .set(USER_SECRET.SECRET, paramSecret)
                .onConflictOnConstraint(USER_SECRET.getPrimaryKey())
                .doUpdate()
                .set(USER_SECRET.SECRET, paramSecret)
                .returningResult(asterisk());

        return queryForObject(query);
    }
}
